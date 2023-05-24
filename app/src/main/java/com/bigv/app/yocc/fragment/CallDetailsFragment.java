package com.bigv.app.yocc.fragment;

/**
 * Created by MiTHUN on 12/9/17.
 * updated by Rahul 12/01/22.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.activity.CallActivity;
import com.bigv.app.yocc.activity.EditCallDetailsActivity;
import com.bigv.app.yocc.activity.HomeActivity;
import com.bigv.app.yocc.activity.LoginActivity;
import com.bigv.app.yocc.activity.PlayAudioFileActivity;
import com.bigv.app.yocc.adapter.CallTranscriptionAdapter;
import com.bigv.app.yocc.adapter.OptionListAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.CallBlockPojo;
import com.bigv.app.yocc.pojo.CallDetailsPojo;
import com.bigv.app.yocc.pojo.CallDetailsSummeryPojo;
import com.bigv.app.yocc.pojo.CallTranscriptionPojo;
import com.bigv.app.yocc.pojo.HomeScreenPojo;
import com.bigv.app.yocc.pojo.PasswordCheckPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.bigv.app.yocc.webservices.LoginWebservices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.componants.MyDateTimePicker;
import com.mithsoft.lib.componants.MyProgressDialog;
import com.mithsoft.lib.componants.Toasty;
import com.mithsoft.lib.utils.MsUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CallDetailsFragment extends Fragment {

    private static final String TAG = "CallDetailsFragment";
    private static final int NEW_EDIT_ACTIVITY = 333;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    private File toMedia = null;
    private long downloadId = 0;
    private View view;
    private Context context;
    private TextView timeTextView;
    //    private HomeScreenPojo homeScreenPojo;
    private LinearLayout listLinearLayout;
    private Button filterButton;
    private Button searchButton;
    private SlidingDrawer slidingDrawer;
    private EditText startDateTimeEditText;
    private EditText endDateTimeEditText;
    private String startDateTime = null;
    private String endDateTime = null;
    private String startDateTimeShow = null;
    private String endDateTimeShow = null;



    public static Fragment newInstance() {
        CallDetailsFragment fragment = new CallDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.call_details_fragment, container, false);
        context = container.getContext();
        initComponants();
        return view;
    }

    private void initComponants() {
        genrateId();
        registerEvents();
        initData();
        setDataToList(false);
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {

            if (QuickUtils.prefs.getBoolean(AUtils.UPDATE_CALL_LIST, false)) {
            /*
                if fragment called from the home fragment as todays calls then update
                call list to todays call
            */
                QuickUtils.prefs.save(AUtils.UPDATE_CALL_LIST, false);
                initData();
                new GetCallDetailList().execute(false);
            }
        }
    }

    private void genrateId() {

        timeTextView = view.findViewById(R.id.today_date_TV);
        listLinearLayout = view.findViewById(R.id.call_details_list_layout);
        filterButton = view.findViewById(R.id.call_details_filter_btn);
        searchButton = view.findViewById(R.id.call_details_search_btn);
        slidingDrawer = view.findViewById(R.id.call_details_sliding_drawer);
        startDateTimeEditText = view.findViewById(R.id.start_date_time_et);
        endDateTimeEditText = view.findViewById(R.id.end_date_time_et);
    }

    private void registerEvents() {

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterButton.getText().equals("Filter Calls")) {
                    slidingDrawer.open();
                } else if (filterButton.getText().equals("Close")) {
                    slidingDrawer.close();
                }
            }
        });
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {

                filterButton.setText("Close");
                startDateTimeEditText.setText("00/00/0000 00:00:00");
                endDateTimeEditText.setText("00/00/0000 00:00:00");
            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {

                filterButton.setText("Filter Calls");
            }
        });
        startDateTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateTimeSelecterOnClick("START");
            }
        });
        endDateTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateTimeSelecterOnClick("END");
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonOnClick(true);
            }
        });

    }

    private void dateTimeSelecterOnClick(final String from) {

        int hours = 0;
        int minits = 0;

        if (from.equals("START")) {
            hours = 0;
            minits = 1;

        } else {
            hours = 23;
            minits = 59;
        }


        MyDateTimePicker dateAndTimePicker = new MyDateTimePicker(context,
                new MyDateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog,
                                      Calendar calendarSelected,
                                      Date dateSelected, int year,
                                      String monthFullName,
                                      String monthShortName, int monthNumber,
                                      int date, String weekDayFullName,
                                      String weekDayShortName, int hour24,
                                      int hour12, int min, int sec, String AM_PM) {

//                        setTimeSelected(hour24, min);
                        setDateSelected(dateSelected, from);
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        dateAndTimePicker.set24HourFormat(false);
//        dateAndTimePicker.setTimeIn24HourFormat(hours, minits);
        dateAndTimePicker.showDialog();
    }

    private void setDateSelected(Date dateSelected, String from) {

        SimpleDateFormat formatServer = new SimpleDateFormat(AUtils.SERVER_DATE_TIME_FORMATE);
        SimpleDateFormat formatMobile = new SimpleDateFormat(AUtils.MOBILE_DATE_TIME_FORMATE);

        if (from.equals("START")) {
            startDateTime = formatServer.format(dateSelected);
            startDateTimeShow = formatMobile.format(dateSelected);
            startDateTimeEditText.setText(startDateTimeShow);
        } else {
            endDateTime = formatServer.format(dateSelected);
            endDateTimeShow = formatMobile.format(dateSelected);
            endDateTimeEditText.setText(endDateTimeShow);
        }
    }

    private void searchButtonOnClick(boolean isCheckValidation) {

        if (validateFilelds(isCheckValidation)) {

            if (slidingDrawer.isOpened()) {
                slidingDrawer.close();
            }

            new GetCallDetailList().execute(true);

//            new MyAsyncTask(context, true, new MyAsyncTask.AsynTaskListener() {
//                public boolean result = false;
//
//                @Override
//                public void doInBackgroundOpration(SyncServer syncServer) {
//
//                    result = syncServer.getCallDetailsList(startDateTime, endDateTime);
//                    syncServer.getCallDetailsSummery(startDateTime, endDateTime);
//                }
//
//                @Override
//                public void onFinished() {
//
//                    if (result) {
//                        timeTextView.setText(startDateTimeShow + " to " + endDateTimeShow);
////                        Toasty.info(context, "" + getString(R.string.dataUpdated), Toast.LENGTH_SHORT).show();
//                        setDataToList(true);
//                    } else {
//                        Toasty.error(context, "" + getString(R.string.noData), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }).execute();
        }
    }

    private boolean validateFilelds(boolean isCheckValidation) {

        if (!isCheckValidation) {
            return true;
        } else {
            if (AUtils.isNullString(startDateTime) || AUtils.isNullString(endDateTime)) {

                Toasty.warning(context, "" + getString(R.string.selectDateTime), Toast.LENGTH_SHORT).show();
                return false;
            }
            if (AUtils.isNullString(startDateTimeEditText.getText().toString()) || AUtils.isNullString(endDateTimeEditText.getText().toString())) {

                Toasty.warning(context, "" + getString(R.string.selectDateTime), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    private void initData() {

//        SimpleDateFormat mobileDateFormate = new SimpleDateFormat(AUtils.MOBILE_DATE_FORMATE);
//        SimpleDateFormat serverDateFormate = new SimpleDateFormat(AUtils.SERVER_DATE_FORMATE);

        timeTextView.setText("Today  " + AUtils.getTodaysDate(AUtils.MOBILE_DATE_FORMATE));
        startDateTimeShow = ("Today  " + AUtils.getTodaysDate(AUtils.MOBILE_DATE_FORMATE));
        endDateTimeShow = "";

        String todayDate = (AUtils.getTodaysDate(AUtils.SERVER_DATE_FORMATE));
        startDateTime = todayDate + " 00:00:00";
        endDateTime = todayDate + " 23:59:59";

    }

    private View getTopView() {

        View view = getLayoutInflater().inflate(R.layout.call_details_top_view, null);

        if (!AUtils.isNull(QuickUtils.prefs.getString(AUtils.CALL_DETAILS_SUMMERY_POJO, null))) {
            CallDetailsSummeryPojo callDetailsSummeryPojo = new Gson().fromJson(
                    QuickUtils.prefs.getString(AUtils.CALL_DETAILS_SUMMERY_POJO, null), CallDetailsSummeryPojo.class);

            TextView totalCallsTextView = view.findViewById(R.id.count_total_call);
            TextView answeredCallsTextView = view.findViewById(R.id.count_answer_call);
            TextView unansweredCallsTextView = view.findViewById(R.id.count_unanswered_call);
            TextView unoptedCallsTextView = view.findViewById(R.id.count_unopted_call);

            totalCallsTextView.setText(callDetailsSummeryPojo.getTotalCall());
            answeredCallsTextView.setText(callDetailsSummeryPojo.getAnswered());
            unansweredCallsTextView.setText(callDetailsSummeryPojo.getUnanswered());
            unoptedCallsTextView.setText(callDetailsSummeryPojo.getUnoptedCall());
        } else {
            TextView totalCallsTextView = view.findViewById(R.id.count_total_call);
            TextView answeredCallsTextView = view.findViewById(R.id.count_answer_call);
            TextView unansweredCallsTextView = view.findViewById(R.id.count_unanswered_call);
            TextView unoptedCallsTextView = view.findViewById(R.id.count_unopted_call);

            totalCallsTextView.setText("0");
            answeredCallsTextView.setText("0");
            unansweredCallsTextView.setText("0");
            unoptedCallsTextView.setText("0");
        }
        return view;
    }


    private void setDataToList(boolean showToastMsgStatus) {

        listLinearLayout.removeAllViews();

        listLinearLayout.addView(getTopView());

        if (!AUtils.isNullString(QuickUtils.prefs.getString(AUtils.CALL_DETAILS_POJO_LIST, null))) {

            Type type = new TypeToken<List<CallDetailsPojo>>() {
            }.getType();

            List<CallDetailsPojo> callDetailsPojoList = new Gson().fromJson(
                    QuickUtils.prefs.getString(AUtils.CALL_DETAILS_POJO_LIST, null), type);

            for (CallDetailsPojo callDetailsPojo : callDetailsPojoList) {

                listLinearLayout.addView(getListViewItem(callDetailsPojo));
            }
        }
        if (showToastMsgStatus) {

            Toasty.success(context, "" + getString(R.string.dataUpdated), Toast.LENGTH_SHORT).show();
        }
    }

    private View getListViewItem(final CallDetailsPojo callDetailsPojo) {

        final View view = getLayoutInflater().inflate(R.layout.call_details_list_view_items, null);

        final LinearLayout outerLayout = view.findViewById(R.id.call_details_outer_layout);
        TextView nameTextView = view.findViewById(R.id.call_details_caller_name_number);
        TextView agentNameNumberTextView = view.findViewById(R.id.call_details_agent_name_number);
        TextView dateTimeTextView = view.findViewById(R.id.call_details_date);
        TextView callTimeTextView = view.findViewById(R.id.call_details_start_end_time);
        TextView menuNameNumberTextView = view.findViewById(R.id.call_details_menu);
        TextView callDurationTextView = view.findViewById(R.id.call_details_call_duration);
        ImageView statusImageView = view.findViewById(R.id.call_details_icon_call_status);
        ImageView callTypeImageView = view.findViewById(R.id.call_details_call_type);

        if (!AUtils.isNullString(callDetailsPojo.getCallerName())) {
            nameTextView.setText(callDetailsPojo.getCallerName());
        }
        if (!AUtils.isNullString(callDetailsPojo.getCallerNumber())) {
            if (!AUtils.isNullString(nameTextView.getText().toString())) {

                nameTextView.setText(nameTextView.getText().toString() + " - " + callDetailsPojo.getCallerNumber());
            } else {

                nameTextView.setText(callDetailsPojo.getCallerNumber());
            }
        }
        if (!AUtils.isNullString(callDetailsPojo.getOperatorName())) {
            agentNameNumberTextView.setText(callDetailsPojo.getOperatorName());
        }
        if (!AUtils.isNullString(callDetailsPojo.getOperatorNumber())) {
            if (!AUtils.isNullString(agentNameNumberTextView.getText().toString())) {

                agentNameNumberTextView.setText(agentNameNumberTextView.getText().toString() + " - " + callDetailsPojo.getOperatorNumber());
            } else {
                agentNameNumberTextView.setText(callDetailsPojo.getOperatorNumber());
            }
        }
        if (!AUtils.isNullString(callDetailsPojo.getDate())) {
            dateTimeTextView.setText(callDetailsPojo.getDate() + ", ");
        }
        if (!AUtils.isNullString(callDetailsPojo.getStartTime())) {
            callTimeTextView.setText(callDetailsPojo.getStartTime());
        }
        if (!AUtils.isNullString(callDetailsPojo.getEndTime())) {
            if (!AUtils.isNullString(callDetailsPojo.getStartTime())) {
                callTimeTextView.setText(callDetailsPojo.getStartTime() + " - " + callDetailsPojo.getEndTime());
            } else {
                callTimeTextView.setText(callDetailsPojo.getEndTime());
            }
        }
        if (!AUtils.isNullString(callDetailsPojo.getMenuName())) {
            menuNameNumberTextView.setText(callDetailsPojo.getMenuName());
        }
        if (!AUtils.isNullString(callDetailsPojo.getMenuDescription())) {
            if (!AUtils.isNullString(callDetailsPojo.getMenuName())) {
                menuNameNumberTextView.setText(callDetailsPojo.getMenuName() + "-" + callDetailsPojo.getMenuDescription());
            } else {
                menuNameNumberTextView.setText(callDetailsPojo.getMenuDescription());
            }
        }
        if (!AUtils.isNullString(callDetailsPojo.getCallDuration())) {
            callDurationTextView.setText(callDetailsPojo.getCallDuration());
        }
        if (!AUtils.isNullString(callDetailsPojo.getStatus())) {
            switch (callDetailsPojo.getStatus()) {
                case "A":
                    statusImageView.setImageResource(R.drawable.ic_call_attended);
                    break;
                case "UA":
                    statusImageView.setImageResource(R.drawable.ic_missed_calls);
                    break;
                case "UO":
                    statusImageView.setImageResource(R.drawable.ic_unopted_icon);
                    break;
            }
        }
        if (!AUtils.isNullString(callDetailsPojo.getCallType())) {
            switch (callDetailsPojo.getCallType()) {
                case "HOT":
                    callTypeImageView.setImageResource(R.drawable.ic_hot_call);
                    break;
                case "WARM":
                    callTypeImageView.setImageResource(R.drawable.ic_warm_call);
                    break;
                case "COLD":
                    callTypeImageView.setImageResource(R.drawable.ic_cold_call);
                    break;
                case "INTERESTED":
                    callTypeImageView.setImageResource(R.drawable.ic_intrested_call);
                    break;
                case "NOT INTERESTED":
                    callTypeImageView.setImageResource(R.drawable.ic_not_intrested_call);
                    break;
            }
        }
//        outerLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!AUtils.isNullString(callDetailsPojo.getCallType())) {
//                    Toast.makeText(context, "" + callDetailsPojo.getCallType(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "none", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        outerLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                listItemOnLongPress(callDetailsPojo);
                return false;
            }
        });
        return view;
    }

    private void listItemOnLongPress(final CallDetailsPojo callDetailsPojo) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        View view = getLayoutInflater().inflate(R.layout.options_dialog, null);

        ListView optionsListView = view.findViewById(R.id.options_lv);

        List<String> options = new ArrayList<String>();
        options.add("Edit");
        options.add("Play File");
        options.add("Download File");
        options.add("Call Transcription");
        options.add("Block Caller");
//        options.add("Send SMS");
        options.add("Call");

        OptionListAdapter optionListAdapter = new OptionListAdapter(context, options);
        optionsListView.setAdapter(optionListAdapter);

        optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                alertDialog.dismiss();
                optionsListViewOnItemClick(position, callDetailsPojo);
            }
        });

        TextView title = new TextView(context);
        title.setPadding(20, 20, 20, 20);
        title.setTextSize(22);
        title.setText("Option");
        title.setTextColor(Color.WHITE);
        title.setBackgroundColor((Color.parseColor("#4989c7")));
        alertDialog.setCustomTitle(title);
//        alertDialog.setTitle("Option");

        alertDialog.setView(view);
        alertDialog.show();
    }

    private void optionsListViewOnItemClick(int position, CallDetailsPojo callDetailsPojo) {
        switch (position) {
            case 0:
                Intent intent = new Intent(context, EditCallDetailsActivity.class);
                intent.putExtra(AUtils.IS_EDIT, true);
                intent.putExtra(AUtils.CALL_DETAILS_POJO, callDetailsPojo);
                startActivityForResult(intent, NEW_EDIT_ACTIVITY);
                break;
            case 1:
                playFileOnClick(callDetailsPojo);
                break;
            case 2:
                downloadFileOnClick(callDetailsPojo);
                break;
            case 3:
                callTranscriptionOnClick(callDetailsPojo);
                break;
            case 4:
                if (!AUtils.isNull(callDetailsPojo.isCallBlock())) {
                    callBlockDialog(callDetailsPojo);
                } else {
                    Toasty.error(context, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                callClientOnClick(callDetailsPojo);
                break;
//            case 6:
//                Toast.makeText(context, "Send SMS", Toast.LENGTH_SHORT).show();
//                break;
        }
    }

    private void callTranscriptionOnClick(final CallDetailsPojo callDetailsPojo) {
        new MyAsyncTask(context, true, new MyAsyncTask.AsynTaskListener() {
            public List<CallTranscriptionPojo> callTranscriptionPojoList;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                callTranscriptionPojoList = syncServer.getCallTranscriptionList(callDetailsPojo.getCdTrNo());
            }

            @Override
            public void onFinished() {
                if (!AUtils.isNull(callTranscriptionPojoList) && !callTranscriptionPojoList.isEmpty()) {

                    showCallTranscriptionListDialog(callTranscriptionPojoList);
                } else {
                    Toasty.info(context, "No call transcription data available", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();


    }

    private void showCallTranscriptionListDialog(List<CallTranscriptionPojo> callTranscriptionPojoList) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        ListView listView = new ListView(context);
        listView.setCacheColorHint(Color.WHITE);
        CallTranscriptionAdapter callTranscriptionAdapter = new CallTranscriptionAdapter(context, callTranscriptionPojoList);
        listView.setAdapter(callTranscriptionAdapter);

        TextView title = new TextView(context);
        title.setPadding(20, 20, 20, 20);
        title.setTextSize(22);
        title.setText("Call Transcription");
        title.setTextColor(Color.WHITE);
        title.setBackgroundColor((Color.parseColor("#4989c7")));
        alert.setCustomTitle(title);
        alert.setView(listView);
        alert.show();
    }

    private void downloadFileOnClick(CallDetailsPojo callDetailsPojo) {

        if (isPermissionGiven()) {
            if (!AUtils.isNullString(callDetailsPojo.getStatus()) && callDetailsPojo.getStatus().equals("A")) {

                getAudioUrlFromServerToDownload(callDetailsPojo);

            } else {
                Toasty.info(context, getString(R.string.fileNotFound), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isPermissionGiven() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                CrUtil.showMessageGoToSettingsDialog(context, "In order to work properly, " + context.getString(R.string.app_name) + " need a permission to access your Extenal Storage  to save file",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                CrUtil.goToAppSettings(context);
//                            }
//                        });
//            } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
//            }
        } else {

            // permission granted
            return true;
        }
        return false;
    }

    private void getAudioUrlFromServerToDownload(final CallDetailsPojo callDetailsPojo) {

        new MyAsyncTask(context, true, new MyAsyncTask.AsynTaskListener() {
            ResultPojo resultPojo = null;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                resultPojo = syncServer.getFileDownloadData(callDetailsPojo.getCdTrNo());
            }

            @Override
            public void onFinished() {

                if (!AUtils.isNull(resultPojo) && resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                    Log.i("RahulCheck", "downloadFileUrl: "+resultPojo.getMessage());
                    downloadAudioFile(resultPojo.getMessage());
                  //  downloadMediaFile(resultPojo.getMessage());

                } else {
                    Toasty.error(context, getString(R.string.fileNotFound), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

    private void downloadAudioFile(String url) {
        Log.i("RahulCheck", "downloadFileUrl: "+url);

        File dir = new File(Environment.getExternalStorageDirectory().toString()
                + "/YOCC");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (MsUtils.isNetWorkAvailable(context)) {

            try {

//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://naasongsdownload.com/Telugu/2009-Naasongs.Audio/01%20File/Kick%20(2009)/Dil%20Kalaase%20-Naasongs.Audio.mp3"));
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("yocc media file Downloading....");
                request.setTitle("Yocc audio");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //request.setDestinationInExternalPublicDir("YOCC Audio", "yocc.wav");
                request.setDestinationInExternalFilesDir(context,"YOCC Audio", "yocc.wav");

                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Log.i("RahulCheck", "downloadFileUrl: "+manager );
                manager.enqueue(request);
                Toasty.success(context, "" + getString(R.string.fileDownloaded), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.i("RahulCheck", "downloadFileUrl: "+e );
                e.printStackTrace();
                Toasty.error(context, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
            }
        } else {

            Toasty.warning(context, "" + getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
        }

    }

    private void downloadMediaFile(String url) {


        String link = url;
        String s = link.substring(link.lastIndexOf("/"));

        Log.d(TAG, "downloadMedia: " + s);
        Log.i("RahulCheck", "downloadFileUrl: "+s );
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";

        toMedia = new File(destination, s);

        destination += s;
        Uri uri = Uri.parse("file://" + destination);

        Log.d(TAG, "downloadMedia: " + uri);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
        request.setDescription("Downloading new media.....");
        request.setTitle("Yocc audio");
        request.setDestinationUri(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        downloadId = manager.enqueue(request);
        Toasty.success(context, "" + getString(R.string.fileDownloaded), Toast.LENGTH_SHORT).show();

        Uri muri = manager.getUriForDownloadedFile(downloadId);
        Log.d(TAG, "downloadMediaRahul: " + toMedia);
    }

    private void callClientOnClick(CallDetailsPojo callDetailsPojo) {

        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(AUtils.CALL_DETAILS_POJO, callDetailsPojo);
        startActivity(intent);
    }

    private void playFileOnClick(CallDetailsPojo callDetailsPojo) {

        if (!AUtils.isNullString(callDetailsPojo.getStatus()) && callDetailsPojo.getStatus().equals("A")) {

            Intent intent = new Intent(context, PlayAudioFileActivity.class);
            intent.putExtra(AUtils.CALL_DETAILS_POJO, callDetailsPojo);
            startActivity(intent);

        } else {
            Toasty.info(context, getString(R.string.fileNotFound), Toast.LENGTH_SHORT).show();
        }
    }

    private void callBlockDialog(final CallDetailsPojo callDetailsPojo) {

        String title = "";
        String buttonText = "";
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        if (callDetailsPojo.isCallBlock()) {
            title = "Caller number " + callDetailsPojo.getCallerNumber() + " is already block";
            buttonText = "UNBLOCK";
        } else {
            title = "Caller number " + callDetailsPojo.getCallerNumber() + " is not block";
            buttonText = "BLOCK";
        }
        alert.setTitle(title);

        alert.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callDetailsPojo.isCallBlock()) {
                    blockCallerOnClick(callDetailsPojo.getCallerNumber(), false);
                } else {
                    blockCallerOnClick(callDetailsPojo.getCallerNumber(), true);
                }
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_EDIT_ACTIVITY) {

            if (resultCode == Activity.RESULT_OK) {

                searchButtonOnClick(false);
            }
        }
    }

    private void blockCallerOnClick(final String callerNumber, boolean status) {

        final CallBlockPojo callBlockPojo = new CallBlockPojo();
        callBlockPojo.setCallerId(callerNumber);
        callBlockPojo.setBlock(status);

        new MyAsyncTask(context, true, new MyAsyncTask.AsynTaskListener() {
            public ResultPojo result = null;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                result = syncServer.callBlock(callBlockPojo);
                if (!AUtils.isNull(result) && !AUtils.isNullString(result.getStatus())) {
                    syncServer.getCallDetailsList(startDateTime, endDateTime);
                }
            }

            @Override
            public void onFinished() {

                if (!AUtils.isNull(result) && !AUtils.isNullString(result.getStatus())) {
                    if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        Toasty.success(context, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                        setDataToList(false);
                    } else {
                        Toasty.error(context, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(context, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

    private class GetCallDetailList extends AsyncTask {

        private MyProgressDialog progressDialog;
        public SyncServer syncServer = new SyncServer(context);
        private boolean isNetworkAvail = false;
        public boolean result = false;
        private boolean showToastMsgStatus = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new MyProgressDialog(context, R.drawable.ic_progress, false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {


            showToastMsgStatus = (boolean) objects[0];

            if (AUtils.isNetWorkAvailable(context)) {

                result = syncServer.getCallDetailsList(startDateTime, endDateTime);
                if (result) {
                    syncServer.getCallDetailsSummery(startDateTime, endDateTime);
                }

                isNetworkAvail = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (isNetworkAvail) {
                if (result) {
                    if (!AUtils.isNullString(endDateTimeShow)) {
                        timeTextView.setText(startDateTimeShow + " to " + endDateTimeShow);
                    } else {
                        timeTextView.setText(startDateTimeShow);
                    }
//                        Toasty.info(context, "" + getString(R.string.dataUpdated), Toast.LENGTH_SHORT).show();
                    setDataToList(showToastMsgStatus);
                } else {
                    Toasty.error(context, "" + getString(R.string.noData), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toasty.warning(context, context.getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}