package com.bigv.app.yocc.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.componants.MyDateTimePicker;
import com.mithsoft.lib.componants.Toasty;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import quickutils.core.QuickUtils;
import com.bigv.app.yocc.R;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.CallDetailsPojo;
import com.bigv.app.yocc.pojo.CallDetailsSummeryPojo;
import com.bigv.app.yocc.pojo.HomeScreenPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;

/**
 * Created by MiTHUN on 22/11/17.
 */

public class WeeklyReport extends BaseActivity {

    private static final String TAG = "WeeklyReport";
    private Context context;
    private TextView timeTextView;
    //    private HomeScreenPojo homeScreenPojo;
    private LinearLayout listLinearLayout;
    private Button filterButton;
    private Button searchButton;
    private SlidingDrawer slidingDrawer;
    private EditText startDateTimeEditText;
    private EditText endDateEditTimeText;
    private String startDateTime = null;
    private String endDateTime = null;


    @Override
    protected void genrateId() {

        setContentView(R.layout.weekly_report_activty);

        timeTextView = findViewById(R.id.today_date_TV);
        listLinearLayout = findViewById(R.id.call_details_list_layout);
        filterButton = findViewById(R.id.call_details_filter_btn);
        searchButton = findViewById(R.id.call_details_search_btn);
        slidingDrawer = findViewById(R.id.call_details_sliding_drawer);
        startDateTimeEditText = findViewById(R.id.start_date_time_et);
        endDateEditTimeText = findViewById(R.id.end_date_time_et);
    }

    @Override
    protected void registerEvents() {

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {

                startDateTime = null;
                endDateTime = null;
                filterButton.setText("Close");
                startDateTimeEditText.setText("00-00-0000 00:00:00");
                endDateEditTimeText.setText("00-00-0000 00:00:00");
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
        endDateEditTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateTimeSelecterOnClick("END");
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonOnClick();
            }
        });

    }

    private void dateTimeSelecterOnClick(final String from) {

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
        dateAndTimePicker.showDialog();
    }

    private void setDateSelected(Date dateSelected, String from) {

        SimpleDateFormat formatForServer = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        if (from.equals("START")) {
            startDateTime = formatForServer.format(dateSelected);
            startDateTimeEditText.setText(format.format(dateSelected));
        } else {
            endDateTime = formatForServer.format(dateSelected);
            endDateEditTimeText.setText(format.format(dateSelected));
        }
    }

    private void searchButtonOnClick() {

        if (!AUtils.isNullString(startDateTime) && !AUtils.isNullString(endDateTime)) {

            slidingDrawer.close();

            new MyAsyncTask(context, true, new MyAsyncTask.AsynTaskListener() {
                public boolean result = false;

                @Override
                public void doInBackgroundOpration(SyncServer syncServer) {

                    result = syncServer.getCallDetailsList(startDateTime, endDateTime);
                    syncServer.getCallDetailsSummery(startDateTime, endDateTime);
                }

                @Override
                public void onFinished() {

                    if (result) {
                        timeTextView.setText(startDateTimeEditText.getText().toString() + " to " + endDateEditTimeText.getText().toString());
//                        Toasty.info(context, "" + getString(R.string.dataUpdated), Toast.LENGTH_SHORT).show();
                        setDataToList(true);
                    } else {
                        Toasty.error(context, "" + getString(R.string.noData), Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();

        } else {
            Toasty.warning(context, "" + getString(R.string.selectDateTime), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void initData() {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String stringDate = format.format(new Date());
        timeTextView.setText("Today  " + stringDate);
    }

    private View getTopView(boolean topViewDataStatus) {

        View view = getLayoutInflater().inflate(R.layout.call_details_top_view, null);

        if (topViewDataStatus) {

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
            }
        } else {
            if (!AUtils.isNull(QuickUtils.prefs.getString(AUtils.HOME_SCREEN_POJO, null))) {
                HomeScreenPojo homeScreenPojo = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.HOME_SCREEN_POJO, null), HomeScreenPojo.class);

                TextView totalCallsTextView = view.findViewById(R.id.count_total_call);
                TextView answeredCallsTextView = view.findViewById(R.id.count_answer_call);
                TextView unansweredCallsTextView = view.findViewById(R.id.count_unanswered_call);
                TextView unoptedCallsTextView = view.findViewById(R.id.count_unopted_call);

                totalCallsTextView.setText(homeScreenPojo.getTotalCall());
                answeredCallsTextView.setText(homeScreenPojo.getTotalAnsweredCallCount());
                unansweredCallsTextView.setText(homeScreenPojo.getTotalNotAnsweredCallCount());
                unoptedCallsTextView.setText(homeScreenPojo.getTotalUnoptedCallCount());
            }
        }
        return view;
    }


    private void setDataToList(boolean topViewDataStatus) {

        listLinearLayout.removeAllViews();

        listLinearLayout.addView(getTopView(topViewDataStatus));

        if (!AUtils.isNullString(QuickUtils.prefs.getString(AUtils.CALL_DETAILS_POJO_LIST, null))) {

            Type type = new TypeToken<List<CallDetailsPojo>>() {
            }.getType();

            List<CallDetailsPojo> callDetailsPojoList = new Gson().fromJson(
                    QuickUtils.prefs.getString(AUtils.CALL_DETAILS_POJO_LIST, null), type);

            for (CallDetailsPojo callDetailsPojo : callDetailsPojoList) {

                listLinearLayout.addView(getListViewItem(callDetailsPojo));
            }
        }
        if (topViewDataStatus) {

            Toasty.success(context, "" + getString(R.string.dataUpdated), Toast.LENGTH_SHORT).show();
        }
    }

    private View getListViewItem(final CallDetailsPojo callDetailsPojo) {

        final View view = getLayoutInflater().inflate(R.layout.call_details_list_view_items, null);


        final LinearLayout outerLayout = view.findViewById(R.id.call_details_outer_layout);
        TextView nameTextView = view.findViewById(R.id.call_details_caller_name_number);
        TextView agentNameNumberTextView = view.findViewById(R.id.call_details_agent_name_number);
        TextView dateTimeTextView = view.findViewById(R.id.call_details_date);
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
            dateTimeTextView.setText(callDetailsPojo.getDate());
        }
        if (!AUtils.isNullString(callDetailsPojo.getMenuName())) {
            menuNameNumberTextView.setText(callDetailsPojo.getMenuName() + "-");
        }
        if (!AUtils.isNullString(callDetailsPojo.getMenuDescription())) {
            menuNameNumberTextView.setText(menuNameNumberTextView.getText().toString() + callDetailsPojo.getMenuDescription());
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

    private void listItemOnLongPress(CallDetailsPojo callDetailsPojo) {

        if (!AUtils.isNullString(callDetailsPojo.getCallType())) {
            Toast.makeText(context, "" + callDetailsPojo.getCallType(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "none", Toast.LENGTH_SHORT).show();
        }
    }
}
