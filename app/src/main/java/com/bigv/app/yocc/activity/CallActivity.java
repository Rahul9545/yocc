package com.bigv.app.yocc.activity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.AgentReplacerAgentListAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.AgentMasterPojo;
import com.bigv.app.yocc.pojo.CallDetailsPojo;
import com.bigv.app.yocc.pojo.CallifyPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.bigv.app.yocc.utils.MyBaseActivity;
import com.bigv.app.yocc.webservices.ActionWebservices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.componants.Toasty;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import quickutils.core.QuickUtils;

/**
 * Created by MiTHUN on 20/11/17.
 * update by Rahul on 23/11/2022
 */

public class CallActivity extends MyBaseActivity {

    private static final String TAG = "CallActivity";
    private EditText callCallerNumberEditText;
    private EditText agentNumberEditText;
    private Spinner agentNameSpinner;
    private Button saveButton;
    private CallDetailsPojo callDetailsPojo;
    private CallifyPojo callifyPojo;
    private CheckBox enterAgentNumberCheckBox;
    private List<AgentMasterPojo> agentMasterPojoList;
    private AgentReplacerAgentListAdapter agentReplacerAgentListAdapter;
    private String updateNumber = "";
    private Boolean fabButton;
    private ResultPojo resultPojo = null;
    private String callerGetNumber;

    @Override
    protected void genrateId() {

        setContentView(R.layout.call_activity);

        callCallerNumberEditText = findViewById(R.id.call_caller_number_et);
        agentNumberEditText = findViewById(R.id.call_agent_number_et);
        agentNameSpinner = findViewById(R.id.call_agent_name_sp);
        saveButton = findViewById(R.id.save_btn);
        enterAgentNumberCheckBox = findViewById(R.id.call_enter_agent_number_cb);
        callifyPojo = new CallifyPojo();

        resultPojo = new ResultPojo();
        fabButton = getIntent().getBooleanExtra("fab_button", false);

        initIntentData();
       // setToolbarTitle("CALL BACK");
        setToolbarTitle(getResources().getString(R.string.str_title_call_back));
        getDateFromServer();
    }

    private void initIntentData() {

        callDetailsPojo = (CallDetailsPojo) getIntent().getSerializableExtra(AUtils.CALL_DETAILS_POJO);
        if (!AUtils.isNull(callDetailsPojo)) {
            callerGetNumber =  callDetailsPojo.getCallerNumber();
            Log.i("TAG", "callDetailsPojo callerGetNumber: "+callerGetNumber);

            disableEditText();

            deleteDatabase(callDetailsPojo.getCallerNumber().toString());

        }
        enableEditText();

        if (QuickUtils.prefs.getBoolean(AUtils.IS_LOGIN_BY_AGENT, false)) {

            agentNameSpinner.setEnabled(false);
            enterAgentNumberCheckBox.setSelected(true);
            enterAgentNumberCheckBox.setEnabled(false);
            agentNumberEditText.setText(QuickUtils.prefs.getString(AUtils.USER_LOGIN_ID, ""));
            enterAgentNumberCheckBox.setChecked(true);
            agentNumberEditText.setEnabled(false);
        }
    }

    private void getDateFromServer() {

        new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                syncServer.getAgentMasterList();
            }

            @Override
            public void onFinished() {

                initAgentNameSpinner();
            }
        }).execute();
    }

    private void initAgentNameSpinner() {

        Type type = new TypeToken<List<AgentMasterPojo>>() {
        }.getType();

        agentMasterPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.AGENT_MASTER_LIST, null), type);

        AgentMasterPojo agentMasterPojo = new AgentMasterPojo();
        agentMasterPojo.setOperatorId(0);
        agentMasterPojo.setOperatorName("Select Agent");

        if (AUtils.isNull(agentMasterPojoList) || agentMasterPojoList.isEmpty()) {

            agentMasterPojoList = new ArrayList<AgentMasterPojo>();
        }

        agentMasterPojoList.add(0, agentMasterPojo);
        agentReplacerAgentListAdapter = new AgentReplacerAgentListAdapter(this, android.R.layout.simple_spinner_item,
                agentMasterPojoList);
        agentNameSpinner.setAdapter(agentReplacerAgentListAdapter);
    }

    @Override
    protected void registerEvents() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabButton = false){
                    saveButtonOnClick();
                }else {
                    Log.i("TAG", "fab button msg: "+ true);
                    if (isFormValid()){
                        callDetailsPojo = new CallDetailsPojo();
                        if (enterAgentNumberCheckBox.isChecked()) {
                            callDetailsPojo.setOperatorNumber(agentNumberEditText.getText().toString().trim());
                        } else {
                            AgentMasterPojo agentMasterPojo = (AgentMasterPojo) agentNameSpinner.getSelectedItem();
                            Log.i("TAG", "operator number: "+agentMasterPojo.getOperatorNo());
                            callDetailsPojo.setOperatorNumber(agentMasterPojo.getOperatorNo());
                        }

                        CallifyPojo callifyPojo = new CallifyPojo();

                        if (callDetailsPojo == null){
                            callifyPojo.setCallerNumber(callCallerNumberEditText.getText().toString().trim());
                        }else {
                            callifyPojo.setCallerNumber(callerGetNumber);
                        }
                        callifyPojo.setCallerNumber(callCallerNumberEditText.getText().toString().trim());
                        Log.i("TAG", "caller number: "+callifyPojo.getCallerNumber());
                        //  callifyPojo.setCallerNumber("8999539845"); //added by rahul hard code
                        callifyPojo.setOperatorNumber(callDetailsPojo.getOperatorNumber().trim());
                        Log.i("TAG", "operator number: "+callDetailsPojo.getOperatorNumber().trim());

                        ActionWebservices service = AUtils.createService(ActionWebservices.class, AUtils.SERVER_URL);

                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try  {
                                    resultPojo = service.callIVR(QuickUtils.prefs.getString(AUtils.KEY, ""),
                                            callifyPojo).execute().body();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toasty.success(CallActivity.this, "Call connected successfully", Toast.LENGTH_SHORT).show();
                                           // CallActivity.this.finish();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();
                    }
                }

            }
        });

        enterAgentNumberCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    agentNameSpinner.setSelection(0);
                    agentNameSpinner.setEnabled(false);
                    agentNumberEditText.setEnabled(true);
                } else {
                    agentNameSpinner.setEnabled(true);
                    agentNumberEditText.setEnabled(false);
                    agentNumberEditText.setText("");
                }
            }
        });
    }

    private void saveButtonOnClick() {
        if (isFormValid()) {

            if (enterAgentNumberCheckBox.isChecked()) {
                callDetailsPojo.setOperatorNumber(agentNumberEditText.getText().toString().trim());
            } else {
                AgentMasterPojo agentMasterPojo = (AgentMasterPojo) agentNameSpinner.getSelectedItem();
                Log.i("TAG", "operator number: "+agentMasterPojo.getOperatorNo());
                callDetailsPojo.setOperatorNumber(agentMasterPojo.getOperatorNo());
                Log.i("TAG", "Caller Details for operator number: "+callDetailsPojo.getOperatorNumber());
            }

            new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {

                public ResultPojo result = null;

                @Override
                public void doInBackgroundOpration(SyncServer syncServer) {

                    CallifyPojo callifyPojo = new CallifyPojo();
                    callifyPojo.setCallerNumber(callDetailsPojo.getCallerNumber().trim());
                    callifyPojo.setCallerNumber(callCallerNumberEditText.getText().toString().trim());
                    Log.i("TAG", "caller number: "+callifyPojo.getCallerNumber());
                  //  callifyPojo.setCallerNumber("8999539845"); //added by rahul hard code
                    callifyPojo.setOperatorNumber(callDetailsPojo.getOperatorNumber().trim());
                    Log.i("TAG", "operator number: "+callDetailsPojo.getOperatorNumber().trim());
                    result = syncServer.callIVR(callifyPojo);
                }

                @Override
                public void onFinished() {
                    if (!AUtils.isNull(result)) {

                        if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                            Toasty.success(CallActivity.this, "Call successfully", Toast.LENGTH_SHORT).show();
                            CallActivity.this.finish();
                        } else {
                            Toasty.error(CallActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toasty.error(CallActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();
        }
    }

    private boolean isFormValid() {

        if (enterAgentNumberCheckBox.isChecked()) {
            if (AUtils.isNullString(agentNumberEditText.getText().toString())) {
                Toasty.warning(this, "Please enter agent number", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!AUtils.isNullString(agentNumberEditText.getText().toString())) {
                if (agentNumberEditText.getText().toString().length() < 10) {
                    Toasty.warning(this, "Please enter valid agent number", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }else if (callCallerNumberEditText.getText().toString().trim().isEmpty()){
            Toasty.warning(this, "Please enter caller mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }else if (callCallerNumberEditText.getText().toString().length() <10){
            Toasty.warning(this, "Please enter valid caller number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if (agentNameSpinner.getSelectedItemPosition() == 0) {
                Toasty.warning(this, "Please select agent", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void initData() {

    }
//added by Rahul
    private void enableEditText() {
        callCallerNumberEditText.setFocusableInTouchMode(true);
        callCallerNumberEditText.setFocusable(true);
        callCallerNumberEditText.setEnabled(true);
        updateNumber = callCallerNumberEditText.getText().toString().trim();
        if (!updateNumber.isEmpty()){
            callDetailsPojo.setCallerNumber(updateNumber);
        }
    }

    private void disableEditText() {
        callCallerNumberEditText.setEnabled(false);
        callCallerNumberEditText.setFocusable(false);
        callCallerNumberEditText.setFocusableInTouchMode(false);
        callCallerNumberEditText.setText(callDetailsPojo.getCallerNumber());
    }
}
