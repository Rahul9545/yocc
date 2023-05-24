package com.bigv.app.yocc.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
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
import com.bigv.app.yocc.adapter.AgentMasterAdapter;
import com.bigv.app.yocc.adapter.DownloadListAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.AgentMasterPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.componants.Toasty;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import quickutils.core.QuickUtils;

/**
 * Created by MiTHUN on 20/11/17.
 * updated by Rahul 12/01/22.
 */

public class AgentMasterActivity extends BaseActivity {

    private static final int NEW_EDIT_ACTIVITY = 11;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 654;
    private Toolbar toolbar;
    private ListView agentListView;
    private ImageView imgDownLoad;
    private FloatingActionButton addAgentFAB;
    private AgentMasterAdapter agentListAdapter;

    @Override
    protected void genrateId() {

        setContentView(R.layout.agent_master_activity);
        agentListView = findViewById(R.id.agent_master_lv);
        addAgentFAB = findViewById(R.id.agent_master_add_agent_fab);
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

    public void setSupportActionBar(Toolbar toolbar){
        toolbar.setTitle(getResources().getString(R.string.str_title_agent_master));
    }

    @Override
    protected void registerEvents() {

        addAgentFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAgentFABOnClick();
            }
        });
        agentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        popup.getMenuInflater().inflate(R.menu.oprations, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_item:
                        editOnClick(position);
                        return true;
                    case R.id.delete_item:
                        deleteDialog(position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();

    }

    private void deleteDialog(final int position) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Item will be delete?");

        alert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteOnClick(position);
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private void deleteOnClick(int position) {

        final AgentMasterPojo agentMasterPojo = agentListAdapter.getItem(position);

        new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {
            public ResultPojo resultPojo = null;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                resultPojo = syncServer.deleteAgent(agentMasterPojo);
                if (!AUtils.isNull(resultPojo) && resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                    syncServer.getAgentMasterList();
                }
            }

            @Override
            public void onFinished() {

                if (!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                        setDataToList();
                        Toasty.success(AgentMasterActivity.this, "" + getString(R.string.deleteSuccess), Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.warning(AgentMasterActivity.this, "" + resultPojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(AgentMasterActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();

    }

    private void editOnClick(int position) {

        Intent intent = new Intent(this, EditAgentMasterActivity.class);
        intent.putExtra(AUtils.IS_EDIT, true);
        intent.putExtra(AUtils.AGENT_MASTER_POJO, agentListAdapter.getItem(position));
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

    private void addAgentFABOnClick() {

        Intent intent = new Intent(this, EditAgentMasterActivity.class);
        intent.putExtra(AUtils.IS_EDIT, false);
        startActivityForResult(intent, NEW_EDIT_ACTIVITY);
    }


    private void getDateFromServer() {

        new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                syncServer.getAgentMasterList();
            }

            @Override
            public void onFinished() {

                setDataToList();
            }
        }).execute();

    }

    private void setDataToList() {

        Type type = new TypeToken<List<AgentMasterPojo>>() {
        }.getType();

        List<AgentMasterPojo> agentMasterPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.AGENT_MASTER_LIST, null), type);

        Collections.sort(agentMasterPojoList, new Comparator<AgentMasterPojo>() {
            public int compare(AgentMasterPojo o1, AgentMasterPojo o2) {
                if (o1.getOperatorId() == 0)
                    return 0;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return Long.compare(o2.getOperatorId(), o1.getOperatorId());
                } else {
                    return 0;
                }
            }
        });
        if (!AUtils.isNull(agentMasterPojoList) && !agentMasterPojoList.isEmpty()) {

            agentListAdapter = new AgentMasterAdapter(this, agentMasterPojoList);
            agentListView.setAdapter(agentListAdapter);
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
                AgentMasterActivity.this.finish();
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
                    resultPojo = syncServer.downloadAgentMasterXlsFile();
                } else {
                    resultPojo = syncServer.downloadAgentMasterPdfFile();
                }
            }

            @Override
            public void onFinished() {

                if (!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                        downloadFile(resultPojo.getMessage(), fileType);
                    } else {
                        Toasty.error(AgentMasterActivity.this, "" + resultPojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(AgentMasterActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
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
}
