package com.bigv.app.yocc.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.GroupAddressBookAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.GroupAddressBookPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.bigv.app.yocc.utils.MyBaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.componants.Toasty;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import quickutils.core.QuickUtils;

/**
 * Created by MiTHUN on 20/11/17.
 */

public class GroupAddressBookActivity extends MyBaseActivity {

    private static final int NEW_EDIT_ACTIVITY = 11;
    private ListView groupAddressBookListView;
    private FloatingActionButton addAddressBookFAB;
    private GroupAddressBookAdapter groupAddressBookAdapter;

    @Override
    protected void genrateId() {

        setContentView(R.layout.group_address_book_activity);
        //setToolbarTitle("GROUP");
        setToolbarTitle(getResources().getString(R.string.str_title_group));
        groupAddressBookListView = findViewById(R.id.group_address_book_lv);
        addAddressBookFAB = findViewById(R.id.group_address_book_add_fab);
    }

    @Override
    protected void registerEvents() {

        addAddressBookFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAgentFABOnClick();
            }
        });
        groupAddressBookListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                addressBookOnItemClick(position, view);
                return false;
            }
        });
    }


    @Override
    protected void initData() {

//        setDataToList();
        getDateFromServer();
    }


    private void addressBookOnItemClick(final int position, View view) {

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

        final GroupAddressBookPojo groupAddressBookPojo = groupAddressBookAdapter.getItem(position);

        new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {
            public ResultPojo resultPojo = null;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                resultPojo = syncServer.deleteGroupAddress(groupAddressBookPojo);
                if (!AUtils.isNull(resultPojo) && resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                    syncServer.getGroupAddressBookList();
                }
            }

            @Override
            public void onFinished() {

                if (!AUtils.isNull(resultPojo)) {
                    if (resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                        setDataToList();
                        Toasty.success(GroupAddressBookActivity.this, "" + getString(R.string.deleteSuccess), Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.error(GroupAddressBookActivity.this, "" + resultPojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(GroupAddressBookActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();

    }

    private void editOnClick(int position) {

        Intent intent = new Intent(this, EditGroupAddressBookActivity.class);
        intent.putExtra(AUtils.IS_EDIT, true);
        intent.putExtra(AUtils.GROUP_ADDRESS_BOOK_POJO, groupAddressBookAdapter.getItem(position));
        startActivityForResult(intent, NEW_EDIT_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_EDIT_ACTIVITY) {

//            if (resultCode == Activity.RESULT_CANCELED) {
//            } else
            if (resultCode == Activity.RESULT_OK) {

                setDataToList();
            }
        }
    }

    private void addAgentFABOnClick() {

        Intent intent = new Intent(this, EditGroupAddressBookActivity.class);
        intent.putExtra(AUtils.IS_EDIT, false);
        startActivityForResult(intent, NEW_EDIT_ACTIVITY);
    }

    private void getDateFromServer() {

        new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                syncServer.getGroupAddressBookList();
            }

            @Override
            public void onFinished() {

                setDataToList();
            }
        }).execute();
    }

    private void setDataToList() {

        Type type = new TypeToken<List<GroupAddressBookPojo>>() {
        }.getType();

        List<GroupAddressBookPojo> groupAddressBookPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.GROUP_ADDRESS_BOOK_LIST, null), type);

        if (!AUtils.isNull(groupAddressBookPojoList) && !groupAddressBookPojoList.isEmpty()) {

            Collections.sort(groupAddressBookPojoList, new Comparator<GroupAddressBookPojo>() {
                public int compare(GroupAddressBookPojo o1, GroupAddressBookPojo o2) {

                    if (o1.getDepartmentId() == 0)
                        return 0;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        return Long.compare(o2.getDepartmentId(), o1.getDepartmentId());
                    } else {
                        return 0;
                    }
                }
            });
            groupAddressBookAdapter = new GroupAddressBookAdapter(this, groupAddressBookPojoList);
            groupAddressBookListView.setAdapter(groupAddressBookAdapter);
        } else {
            Toasty.info(this, "" + getString(R.string.noData), Toast.LENGTH_SHORT).show();
        }
    }
}
