package com.bigv.app.yocc.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.GroupAddressBookPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.bigv.app.yocc.utils.MyBaseActivity;
import com.mithsoft.lib.componants.Toasty;

/**
 * Created by MiTHUN on 20/11/17.
 */

public class EditGroupAddressBookActivity extends MyBaseActivity {

    private EditText groupNameEditText;
    private Button saveButton;
    private GroupAddressBookPojo groupAddressBookPojo;

    @Override
    protected void genrateId() {

        setContentView(R.layout.edit_group_address_book_activity);

        groupNameEditText = findViewById(R.id.edit_address_book_caller_no_et);
        saveButton = findViewById(R.id.change_password_btn);

        initIntentData();
    }


    private void initIntentData() {

        Intent intent = getIntent();
        groupAddressBookPojo = (GroupAddressBookPojo) intent.getSerializableExtra(AUtils.GROUP_ADDRESS_BOOK_POJO);

        if (AUtils.isNull(groupAddressBookPojo)) {

            //setToolbarTitle("NEW GROUP");
            setToolbarTitle(getResources().getString(R.string.str_title_new_group));

            groupAddressBookPojo = new GroupAddressBookPojo();
        } else {

            //setToolbarTitle("EDIT GROUP");
            setToolbarTitle(getResources().getString(R.string.str_title_edit_group));

            if (!AUtils.isNullString(groupAddressBookPojo.getDepartmentName())) {

                groupNameEditText.setText(groupAddressBookPojo.getDepartmentName());
            }
        }
    }


    @Override
    protected void registerEvents() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonOnClick();
            }
        });
    }


    private void saveButtonOnClick() {
        if (isFormValid()) {

            getFormData();

            new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {

                public ResultPojo result = null;

                @Override
                public void doInBackgroundOpration(SyncServer syncServer) {

                    result = syncServer.saveGroupAddressBook(groupAddressBookPojo);
                    if (!AUtils.isNull(result) && result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        syncServer.getGroupAddressBookList();
                    }
                }

                @Override
                public void onFinished() {
                    if (!AUtils.isNull(result)) {

                        if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                            Toasty.success(EditGroupAddressBookActivity.this, "Call details save successfully", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK, new Intent());
                            EditGroupAddressBookActivity.this.finish();
                        } else {
                            Toasty.error(EditGroupAddressBookActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toasty.error(EditGroupAddressBookActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();

        }
    }

    private GroupAddressBookPojo getFormData() {

        if (!AUtils.isNullString(groupNameEditText.getText().toString())) {

            groupAddressBookPojo.setDepartmentName(groupNameEditText.getText().toString().trim());
        }
        return groupAddressBookPojo;
    }

    private boolean isFormValid() {

        if (AUtils.isNullString(groupNameEditText.getText().toString())) {
            Toasty.warning(this, "Please enter group name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    protected void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        setResult(Activity.RESULT_CANCELED, new Intent());
        EditGroupAddressBookActivity.this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED, new Intent());
    }
}
