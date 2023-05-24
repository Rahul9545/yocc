package com.bigv.app.yocc.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.CallEditPriorityListAdapter;
import com.bigv.app.yocc.adapter.CallRemarkListAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.CallDetailsPojo;
import com.bigv.app.yocc.pojo.CallPriorityPojo;
import com.bigv.app.yocc.pojo.RemarkPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.componants.MyDateTimePicker;
import com.mithsoft.lib.componants.Toasty;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import quickutils.core.QuickUtils;

/**
 * Created by MiTHUN on 20/11/17.
 */

public class EditCallDetailsActivity extends BaseActivity {

    private EditText callerNumberEditText;
    private EditText firstNameEditText;
    private EditText middleNameEditText;
    private EditText lastNameEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private Spinner callTypeSpinner;
    private CheckBox followUpCheckBox;
    private EditText followUpDateTimeEditText;
    private EditText remarkEditText;
    private ImageView remarkListImageView;
    private Button saveButton;
    private Toolbar toolbar;
    private CallDetailsPojo callDetailsPojo;
    private List<CallPriorityPojo> callPriorityPojoList;
    private CallEditPriorityListAdapter callEditPriorityListAdapter;
    private String followUpDateTime = "";
    private SimpleDateFormat formatMobile;
    private SimpleDateFormat formatServer;


    @Override
    protected void genrateId() {

        setContentView(R.layout.edit_call_details_activity);

        callerNumberEditText = findViewById(R.id.edit_call_details_caller_no_et);
        firstNameEditText = findViewById(R.id.edit_call_details_first_name_et);
        middleNameEditText = findViewById(R.id.edit_call_details_middle_name_et);
        lastNameEditText = findViewById(R.id.edit_call_details_last_name_et);
        addressEditText = findViewById(R.id.edit_call_details_address_et);
        emailEditText = findViewById(R.id.edit_call_details_email_et);
        callTypeSpinner = findViewById(R.id.edit_call_details_call_type_sp);
        followUpCheckBox = findViewById(R.id.edit_call_details_followup_chk);
        followUpDateTimeEditText = findViewById(R.id.edit_call_details_select_follow_up_date);
        remarkEditText = findViewById(R.id.edit_call_details_remark);
        remarkListImageView = findViewById(R.id.edit_call_details_remark_list_iv);
        saveButton = findViewById(R.id.change_password_btn);

        formatServer = new SimpleDateFormat(AUtils.SERVER_DATE_TIME_FORMATE);
        formatMobile = new SimpleDateFormat(AUtils.MOBILE_DATE_TIME_FORMATE);

        callerNumberEditText.setKeyListener(null);
        callerNumberEditText.setTextIsSelectable(true);

        initCallTypeSpinner();
        initIntentData();
        initToolbar();
    }

    private void initCallTypeSpinner() {

        Type type = new TypeToken<List<CallPriorityPojo>>() {
        }.getType();

        callPriorityPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.CALL_PRIORITY_LIST, null), type);

        CallPriorityPojo callPriorityPojo = new CallPriorityPojo();
        callPriorityPojo.setCallPriority("");
        callPriorityPojo.setCallType("Select Call Type");

        if (AUtils.isNull(callPriorityPojoList) || callPriorityPojoList.isEmpty()) {
            callPriorityPojoList = new ArrayList<CallPriorityPojo>();
        }

        callPriorityPojoList.add(0, callPriorityPojo);
        callEditPriorityListAdapter = new CallEditPriorityListAdapter(this, android.R.layout.simple_spinner_item,
                callPriorityPojoList);
        callTypeSpinner.setAdapter(callEditPriorityListAdapter);
    }

    private void initIntentData() {

        Intent intent = getIntent();
        callDetailsPojo = (CallDetailsPojo) intent.getSerializableExtra(AUtils.CALL_DETAILS_POJO);

        if (AUtils.isNull(callDetailsPojo)) {

            Toasty.error(this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
            EditCallDetailsActivity.this.finish();
        } else {

            if (!AUtils.isNullString(callDetailsPojo.getCallerNumber())) {

                callerNumberEditText.setText(callDetailsPojo.getCallerNumber());
            }
            if (!AUtils.isNullString(callDetailsPojo.getFirstName())) {

                firstNameEditText.setText(callDetailsPojo.getFirstName());
            }
            if (!AUtils.isNullString(callDetailsPojo.getMiddleName())) {

                middleNameEditText.setText(callDetailsPojo.getMiddleName());
            }
            if (!AUtils.isNullString(callDetailsPojo.getLastName())) {

                lastNameEditText.setText(callDetailsPojo.getLastName());
            }
            if (!AUtils.isNullString(callDetailsPojo.getAddress())) {

                addressEditText.setText(callDetailsPojo.getAddress());
            }
            if (!AUtils.isNullString(callDetailsPojo.getEmail())) {

                emailEditText.setText(callDetailsPojo.getEmail());
            }
            if (!AUtils.isNullString(callDetailsPojo.getCallPriority())) {

                for (CallPriorityPojo callPriorityPojo : callPriorityPojoList) {
                    if (callPriorityPojo.getCallPriority().equals(callDetailsPojo.getCallPriority())) {

                        callTypeSpinner.setSelection(callEditPriorityListAdapter.getPosition(callPriorityPojo));
                        break;
                    }
                }
            }
//            if (!AUtils.isNullString(callDetailsPojo.getFollowUp())) {
//
//                followUpCheckBox.setChecked(true);
//                followUpDateTimeEditText.setEnabled(true);
//
//                followUpDateTime = callDetailsPojo.getFollowUp();
//                Date dateTime = null;
//                try {
//                    dateTime = formatServer.parse(followUpDateTime);
//                } catch (ParseException e) {
//                    dateTime = null;
//                }
//                if (!AUtils.isNull(dateTime)) {
//                    followUpDateTimeEditText.setText(formatMobile.format(dateTime));
//                }
//            }
//            if (!AUtils.isNullString(callDetailsPojo.getRemark())) {
//
//                remarkEditText.setText(callDetailsPojo.getRemark());
//            }
        }
    }


    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);

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
        //toolbar.setTitle("EDIT CALL");
        toolbar.setTitle(getResources().getString(R.string.str_title_edit_call));
    }


    @Override
    protected void registerEvents() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonOnClick();
            }
        });
        followUpCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    followUpDateTimeEditText.setEnabled(true);
                } else {
                    followUpDateTimeEditText.setText("");
                    followUpDateTimeEditText.setEnabled(false);
                }
            }
        });
        followUpDateTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUpDateTimeEditTextOnClick();
            }
        });
        remarkListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                remarkListImageViewOnClick();
            }
        });
    }

    private void remarkListImageViewOnClick() {

        new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {
            public List<RemarkPojo> remarkPojoList;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {


                remarkPojoList = syncServer.getRemarkList(callDetailsPojo.getCallerNumber());
            }

            @Override
            public void onFinished() {

                if (!AUtils.isNull(remarkPojoList) && !remarkPojoList.isEmpty()) {

                    showCallRemarkListDialog(remarkPojoList);
                } else {
                    Toasty.info(EditCallDetailsActivity.this, "No call remarks available", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();

    }

    private void showCallRemarkListDialog(List<RemarkPojo> remarkPojoList) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.call_remark_dialog, null);


        Collections.sort(remarkPojoList, new Comparator<RemarkPojo>() {
            public int compare(RemarkPojo o1, RemarkPojo o2) {
                if (o1.getTrNo() == 0)
                    return 0;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return Long.compare(o2.getTrNo(), o1.getTrNo());
                } else {
                    return 0;
                }
            }
        });
        ListView listView = view.findViewById(R.id.remark_dialog_list_lv);
        CallRemarkListAdapter remarkListAdapter = new CallRemarkListAdapter(this, remarkPojoList);
        listView.setAdapter(remarkListAdapter);


        TextView title = new TextView(this);
        title.setPadding(20, 20, 20, 20);
        title.setTextSize(22);
        title.setText("Remarks - " + callDetailsPojo.getCallerNumber());
        title.setTextColor(Color.WHITE);
        title.setBackgroundColor((Color.parseColor("#4989c7")));
        alert.setCustomTitle(title);
        alert.setView(view);
        alert.show();
    }

    private void followUpDateTimeEditTextOnClick() {

        MyDateTimePicker dateAndTimePicker = new MyDateTimePicker(this,
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

                        setDateSelected(dateSelected);
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        dateAndTimePicker.set24HourFormat(false);
        dateAndTimePicker.showDialog();
    }

    private void setDateSelected(Date dateSelected) {

        followUpDateTime = formatServer.format(dateSelected);
        followUpDateTimeEditText.setText(formatMobile.format(dateSelected));
    }

    private void saveButtonOnClick() {
        if (isFormValid()) {

            final CallDetailsPojo callDetailsPojo = getFormData();

            new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {

                public ResultPojo result = null;

                @Override
                public void doInBackgroundOpration(SyncServer syncServer) {

                    result = syncServer.saveCallDetails(callDetailsPojo);
                }

                @Override
                public void onFinished() {
                    if (!AUtils.isNull(result)) {

                        if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                            Toasty.success(EditCallDetailsActivity.this, "Call details save successfully", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK, new Intent());
                            EditCallDetailsActivity.this.finish();
                        } else {
                            Toasty.error(EditCallDetailsActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toasty.error(EditCallDetailsActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();

        }
    }

    private CallDetailsPojo getFormData() {

        if (!AUtils.isNullString(firstNameEditText.getText().toString())) {

            callDetailsPojo.setFirstName(firstNameEditText.getText().toString());
        } else {
            callDetailsPojo.setFirstName("");
        }
        if (!AUtils.isNullString(middleNameEditText.getText().toString())) {

            callDetailsPojo.setMiddleName(middleNameEditText.getText().toString());
        } else {
            callDetailsPojo.setMiddleName("");
        }
        if (!AUtils.isNullString(lastNameEditText.getText().toString())) {

            callDetailsPojo.setLastName(lastNameEditText.getText().toString());
        } else {
            callDetailsPojo.setLastName("");
        }
        if (!AUtils.isNullString(addressEditText.getText().toString())) {

            callDetailsPojo.setAddress(addressEditText.getText().toString());
        } else {
            callDetailsPojo.setAddress("");
        }
        if (!AUtils.isNullString(emailEditText.getText().toString())) {

            callDetailsPojo.setEmail(emailEditText.getText().toString());
        } else {
            callDetailsPojo.setEmail("");
        }
        if (callTypeSpinner.getSelectedItemPosition() > 0) {

            CallPriorityPojo callPriorityPojo = (CallPriorityPojo) callTypeSpinner.getSelectedItem();

            callDetailsPojo.setCallPriority(callPriorityPojo.getCallPriority());
            callDetailsPojo.setCallType(callPriorityPojo.getCallType());
        }
        if (followUpCheckBox.isChecked()) {

            callDetailsPojo.setFollowUp(followUpDateTime);
        } else {
            callDetailsPojo.setFollowUp("");
        }
        if (!AUtils.isNullString(remarkEditText.getText().toString())) {

            callDetailsPojo.setRemark(remarkEditText.getText().toString());
        } else {
            callDetailsPojo.setRemark("");
        }
        return callDetailsPojo;
    }

    private boolean isFormValid() {

        if (!AUtils.isNullString(emailEditText.getText().toString())) {
            if (!AUtils.isEmailValid(emailEditText.getText().toString())) {
                Toasty.warning(this, "Please enter valid email id", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (followUpCheckBox.isChecked()) {
            if (AUtils.isNullString(followUpDateTimeEditText.getText().toString())) {
                Toasty.warning(this, "Please select follow up date time", Toast.LENGTH_SHORT).show();
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
        EditCallDetailsActivity.this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED, new Intent());
    }
}
