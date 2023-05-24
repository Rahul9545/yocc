package com.bigv.app.yocc.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.DownloadListAdapter;
import com.bigv.app.yocc.adapter.MenuListAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.MenuMasterPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.componants.Toasty;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import quickutils.core.QuickUtils;

/**
 * Created by MiTHUN on 20/11/17.
 * updated by Rahul 12/01/22.
 */

public class MenuMasterActivity extends BaseActivity {

    private static final int NEW_EDIT_ACTIVITY = 33;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 256;
    private Toolbar toolbar;
    private ListView menuListView;
    private ImageView imgDownLoad;
    private MenuListAdapter menuListAdapter;

    @Override
    protected void genrateId() {

        setContentView(R.layout.menu_master_activity);
        menuListView = findViewById(R.id.menu_master_lv);
        initToolbar();
    }

    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);
        imgDownLoad = findViewById(R.id.img_download);
        imgDownLoad.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setSupportActionBar(Toolbar toolbar) {
        //toolbar.setTitle("MENU MASTER");
        toolbar.setTitle(getResources().getString(R.string.str_title_menu_master));
    }

    @Override
    protected void registerEvents() {

        menuListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                agentListViewOnItemClick(position, view);
                return false;
            }
        });

        imgDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadDialog();
            }
        });
    }


    @Override
    protected void initData() {

//        setDataToList();
        getDateFromServer();
    }


    private void agentListViewOnItemClick(final int position, View view) {

        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.oprations_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_item:
                        editOnClick(position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();

    }

    private void editOnClick(int position) {

        Intent intent = new Intent(this, EditMenuMasterActivity.class);
        intent.putExtra(AUtils.IS_EDIT, true);
        intent.putExtra(AUtils.MENU_MASTER_POJO, menuListAdapter.getItem(position));
        startActivityForResult(intent, NEW_EDIT_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_EDIT_ACTIVITY) {

//            if (resultCode == Activity.RESULT_CANCELED) {
//
//            } else
            if (resultCode == Activity.RESULT_OK) {

                setDataToList();
            }
        }
    }

    private void getDateFromServer() {

        new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                syncServer.getMenuList();
                syncServer.getRoutindPatternList();
                syncServer.getRoutindTypeList();
            }

            @Override
            public void onFinished() {

                setDataToList();
            }
        }).execute();

    }

    private void setDataToList() {

        Type type = new TypeToken<List<MenuMasterPojo>>() {
        }.getType();

        List<MenuMasterPojo> menuMasterPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.MENU_MASTER_LIST, null), type);

        if (!AUtils.isNull(menuMasterPojoList) && !menuMasterPojoList.isEmpty()) {

            menuListAdapter = new MenuListAdapter(this, menuMasterPojoList);
            menuListView.setAdapter(menuListAdapter);
        } else {
            Toasty.info(this, "" + getString(R.string.noData), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_download:
                downloadDialog();
                return true;

            default:
                MenuMasterActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.download, menu);
        return true;
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
                    resultPojo = syncServer.downloadMenuMasterXlsFile();
                } else {
                    resultPojo = syncServer.downloadMenuMasterPdfFile();
                }
            }

            @Override
            public void onFinished() {

                if (!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                        downloadFile(resultPojo.getMessage(), fileType);
                    } else {
                        Toasty.error(MenuMasterActivity.this, "" + resultPojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(MenuMasterActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();

    }

    private void downloadFile(String fileUrl, String fileType) {

        if (isPermissionGiven()) {

           // AUtils.downloadFile(this, fileUrl, fileType);
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
}
