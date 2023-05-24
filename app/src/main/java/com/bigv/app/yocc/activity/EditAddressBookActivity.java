package com.bigv.app.yocc.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.GroupAddressBookListAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.AddressBookPojo;
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
 * Created by MiTHUN on 20/11/17.
 */

public class EditAddressBookActivity extends MyBaseActivity {

    private EditText callerNumberEditText;
    private EditText firstNameEditText;
    private EditText middleNameEditText;
    private EditText lastNameEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private Spinner groupSpinner;
    private Button saveButton;
    private AddressBookPojo addressBookPojo;
    private List<GroupAddressBookPojo> groupAddressBookPojoList;
    private GroupAddressBookListAdapter groupAddressBookListAdapter;
    private int departmentId;

    @Override
    protected void genrateId() {

        setContentView(R.layout.edit_address_book_activity);

        callerNumberEditText = findViewById(R.id.edit_address_book_caller_no_et);
        firstNameEditText = findViewById(R.id.edit_address_book_first_name_et);
        middleNameEditText = findViewById(R.id.edit_address_book_middle_name_et);
        lastNameEditText = findViewById(R.id.edit_address_book_last_name_et);
        addressEditText = findViewById(R.id.edit_address_book_address_et);
        emailEditText = findViewById(R.id.edit_address_book_email_et);
        groupSpinner = findViewById(R.id.edit_address_book_group_name_sp);
        saveButton = findViewById(R.id.change_password_btn);

        initGroupNameSpinner();
        initIntentData();
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


    private void initIntentData() {

        Intent intent = getIntent();
        addressBookPojo = (AddressBookPojo) intent.getSerializableExtra(AUtils.ADDRESS_BOOK_POJO);

        if (AUtils.isNull(addressBookPojo)) {

            //setToolbarTitle("NEW ADDRESS BOOK");
            setToolbarTitle(getResources().getString(R.string.str_title_new_address_book));

            addressBookPojo = new AddressBookPojo();
        } else {

            //setToolbarTitle("EDIT ADDRESS BOOK");
            setToolbarTitle(getResources().getString(R.string.str_title_edit_address_book));

            if (!AUtils.isNullString(addressBookPojo.getCallerId())) {

                callerNumberEditText.setText(addressBookPojo.getCallerId());
            }
            if (!AUtils.isNullString(addressBookPojo.getDepartmentId())) {
                for (GroupAddressBookPojo groupAddressBookPojo : groupAddressBookPojoList) {
                    if (!AUtils.isNullString("" + groupAddressBookPojo.getDepartmentId())) {
                        if (addressBookPojo.getDepartmentId().equals("" + groupAddressBookPojo.getDepartmentId())) {

                            groupSpinner.setSelection(groupAddressBookListAdapter.getPosition(groupAddressBookPojo));
                            break;
                        }
                    }
                }
            }
            if (!AUtils.isNullString(addressBookPojo.getFirstName())) {

                firstNameEditText.setText(addressBookPojo.getFirstName());
            } else {
                firstNameEditText.setText("");
            }
            if (!AUtils.isNullString(addressBookPojo.getMiddleName())) {

                middleNameEditText.setText(addressBookPojo.getMiddleName());
            } else {
                middleNameEditText.setText("");
            }
            if (!AUtils.isNullString(addressBookPojo.getLastName())) {

                lastNameEditText.setText(addressBookPojo.getLastName());
            } else {
                lastNameEditText.setText("");
            }
            if (!AUtils.isNullString(addressBookPojo.getAddress())) {

                addressEditText.setText(addressBookPojo.getAddress());
            } else {
                addressEditText.setText("");
            }
            if (!AUtils.isNullString(addressBookPojo.getEmail())) {

                emailEditText.setText(addressBookPojo.getEmail());
            } else {
                emailEditText.setText("");
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

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                departmentId = groupAddressBookListAdapter.getItem(position).getDepartmentId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                    result = syncServer.saveAddressBook(addressBookPojo);
                    if (!AUtils.isNull(result) && result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        syncServer.getAddressBookList();
                    }
                }

                @Override
                public void onFinished() {
                    if (!AUtils.isNull(result)) {

                        if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                            Toasty.success(EditAddressBookActivity.this, "Call details save successfully", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK, new Intent());
                            EditAddressBookActivity.this.finish();
                        } else {
                            Toasty.error(EditAddressBookActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toasty.error(EditAddressBookActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();

        }
    }

    private AddressBookPojo getFormData() {

        if (!AUtils.isNullString(callerNumberEditText.getText().toString())) {

            addressBookPojo.setCallerId(callerNumberEditText.getText().toString().trim());
        }
        if (!AUtils.isNull(departmentId)) {

            addressBookPojo.setDepartmentId("" + departmentId);
        }
        if (!AUtils.isNullString(firstNameEditText.getText().toString())) {

            addressBookPojo.setFirstName(firstNameEditText.getText().toString().trim());
        }
        if (!AUtils.isNullString(middleNameEditText.getText().toString())) {

            addressBookPojo.setMiddleName(middleNameEditText.getText().toString().trim());
        }
        if (!AUtils.isNullString(lastNameEditText.getText().toString())) {

            addressBookPojo.setLastName(lastNameEditText.getText().toString().trim());
        }
        if (!AUtils.isNullString(addressEditText.getText().toString())) {

            addressBookPojo.setAddress(addressEditText.getText().toString().trim());
        }
        if (!AUtils.isNullString(emailEditText.getText().toString())) {

            addressBookPojo.setEmail(emailEditText.getText().toString().trim());
        }
        return addressBookPojo;
    }

    private boolean isFormValid() {

        if (AUtils.isNullString(callerNumberEditText.getText().toString())) {
            Toasty.warning(this, "Please enter caller number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AUtils.isNullString(callerNumberEditText.getText().toString())) {
            if (callerNumberEditText.getText().toString().length() < 10) {
                Toasty.warning(this, "Please enter valid caller number", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (groupSpinner.getSelectedItemPosition() == 0) {
            Toasty.warning(this, "Please select Group", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (AUtils.isNullString(firstNameEditText.getText().toString())) {
            Toasty.warning(this, "Please enter first name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AUtils.isNullString(emailEditText.getText().toString())) {
            if (!AUtils.isEmailValid(emailEditText.getText().toString())) {
                Toasty.warning(this, "Please enter valid email id", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    @Override
    protected void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        setResult(Activity.RESULT_CANCELED, new Intent());
        EditAddressBookActivity.this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED, new Intent());
    }
}
