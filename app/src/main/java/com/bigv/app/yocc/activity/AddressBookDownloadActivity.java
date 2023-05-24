package com.bigv.app.yocc.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.DownloadListAdapter;
import com.bigv.app.yocc.adapter.GroupAddressBookListAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.GroupAddressBookPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.bigv.app.yocc.utils.MyBaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.componants.Toasty;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import quickutils.core.QuickUtils;

/**
 * Created by MiTHUN on 5/1/18.
 * updated by Rahul 12/01/22.
 */

public class AddressBookDownloadActivity extends MyBaseActivity {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 354;
    private Spinner groupSpinner;
    private Button downloadButton;
    private List<GroupAddressBookPojo> groupAddressBookPojoList;
    private GroupAddressBookListAdapter groupAddressBookListAdapter;
    private String groupId = "";

    @Override
    protected void genrateId() {
        setContentView(R.layout.address_book_download_activity);
        groupSpinner = findViewById(R.id.download_group_name_sp);
        downloadButton = findViewById(R.id.download_btn);
        setToolbarTitle(getResources().getString(R.string.str_title_download));
        initGroupNameSpinner();
    }

    private void initGroupNameSpinner() {

        Type type = new TypeToken<List<GroupAddressBookPojo>>() {
        }.getType();

        groupAddressBookPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.GROUP_ADDRESS_BOOK_LIST, null), type);

        GroupAddressBookPojo groupAddressBookPojo = new GroupAddressBookPojo();
        groupAddressBookPojo.setDepartmentId(0);
        groupAddressBookPojo.setDepartmentName("Select Group");

        if (AUtils.isNull(groupAddressBookPojoList) || groupAddressBookPojoList.isEmpty()) {

            groupAddressBookPojoList = new ArrayList<GroupAddressBookPojo>();
        }

        groupAddressBookPojoList.add(0, groupAddressBookPojo);
        groupAddressBookListAdapter = new GroupAddressBookListAdapter(this,
                android.R.layout.simple_spinner_item, groupAddressBookPojoList);
        groupSpinner.setAdapter(groupAddressBookListAdapter);
    }

    @Override
    protected void registerEvents() {

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                groupId = "" + groupAddressBookListAdapter.getItem(position).getDepartmentId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValidate()) {
                    downloadDialog();
                }
            }
        });
    }

    private void downloadDialog() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.options_dialog, null);

        ListView optionsListView = view.findViewById(R.id.options_lv);

        List<String> options = new ArrayList<String>();
        options.add("Excel");
        options.add("Pdf");

        DownloadListAdapter downloadListAdapter = new DownloadListAdapter(this, options);
        optionsListView.setAdapter(downloadListAdapter);

        optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                alertDialog.dismiss();
                optionsListViewOnItemClick(position);
            }
        });

        TextView title = new TextView(this);
        title.setPadding(20, 20, 20, 20);
        title.setTextSize(22);
        title.setText("Download");
        title.setTextColor(Color.WHITE);
        title.setBackgroundColor((Color.parseColor("#4989c7")));
        alertDialog.setCustomTitle(title);
//        alertDialog.setTitle("Option");

        alertDialog.setView(view);
        alertDialog.show();
    }

    private void optionsListViewOnItemClick(int position) {
        switch (position) {
            case 0:
                downloadButtonOnClick("xls");
                break;
            case 1:
                downloadButtonOnClick("pdf");
                break;
        }
    }

    private void downloadButtonOnClick(final String fileType) {

        new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {

            public ResultPojo resultPojo = null;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                if (fileType.equals("xls")) {
                    resultPojo = syncServer.downloadXlsFile(groupId);
                } else {
                    resultPojo = syncServer.downloadPdfFile(groupId);
                }
            }

            @Override
            public void onFinished() {

                if (!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                        downloadFile(resultPojo.getMessage(), fileType);
                    } else {
                        Toasty.error(AddressBookDownloadActivity.this, "" + resultPojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(AddressBookDownloadActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();

    }

    private void downloadFile(String fileUrl, String fileType) {

        if (isPermissionGiven()) {

          //  AUtils.downloadFile(this, fileUrl, fileType);
            AUtils.downloadMediaFileType(this,fileUrl,fileType);
        }
    }

    private boolean isPermissionGiven() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                CrUtil.showMessageGoToSettingsDialog(context, "In order to work properly, " + context.getString(R.string.app_name) + " need a permission to access your Extenal Storage  to save file",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                CrUtil.goToAppSettings(context);
//                            }
//                        });
//            } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
//            }
        } else {

            // permission granted
            return true;
        }
        return false;
    }

    private boolean isFormValidate() {

        if (groupSpinner.getSelectedItemPosition() == 0) {
            Toasty.warning(this, "Please select Group", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void initData() {

        if (AUtils.isNull(groupAddressBookPojoList) || groupAddressBookPojoList.isEmpty()) {
            getDateFromServer(true);
        } else {
            getDateFromServer(false);
        }
    }

    private void getDateFromServer(boolean isProgressDialog) {

        new MyAsyncTask(this, isProgressDialog, new MyAsyncTask.AsynTaskListener() {
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                syncServer.getGroupAddressBookList();
            }

            @Override
            public void onFinished() {

                initGroupNameSpinner();
            }
        }).execute();
    }
}
