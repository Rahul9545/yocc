package com.bigv.app.yocc.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.componants.Toasty;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.AgentMasterPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;

/**
 * Created by MiTHUN on 20/11/17.
 */

public class EditAgentMasterActivity extends BaseActivity {

    private static final String TAG = "EditAgentMasterActivity";
    private EditText agentNameEditText;
    private EditText agentNumberEditText;
    private EditText agentExtensionNoEditText;
    private Button saveButton;
    private Toolbar toolbar;
    private boolean isEdit = false;
    private AgentMasterPojo agentMasterPojo = null;
    private CheckBox activeAgentCheckBox;


    @Override
    protected void genrateId() {

        setContentView(R.layout.edit_agent_master_activity);

        agentNameEditText = findViewById(R.id.agent_mst_agent_name_et);
        agentNumberEditText = findViewById(R.id.agent_mst_agent_number_et);
        agentExtensionNoEditText = findViewById(R.id.agent_mst_extension_number_et);
        saveButton = findViewById(R.id.save_btn);
        activeAgentCheckBox = findViewById(R.id.agent_mst_agent_active_cb);

        initIntentData();
        initToolbar();
    }

    private void initIntentData() {

        Intent intent = getIntent();
        isEdit = intent.getExtras().getBoolean(AUtils.IS_EDIT);
        agentMasterPojo = (AgentMasterPojo) intent.getSerializableExtra(AUtils.AGENT_MASTER_POJO);
        if (AUtils.isNull(agentMasterPojo)) {
            agentMasterPojo = new AgentMasterPojo();
            activeAgentCheckBox.setVisibility(View.GONE);
        } else {
            activeAgentCheckBox.setVisibility(View.VISIBLE);
            if (!AUtils.isNullString(agentMasterPojo.getOperatorName())) {
                agentNameEditText.setText(agentMasterPojo.getOperatorName());
            }
            if (!AUtils.isNullString(agentMasterPojo.getOperatorNo())) {
                agentNumberEditText.setText(agentMasterPojo.getOperatorNo());
            }
            if (!AUtils.isNullString(agentMasterPojo.getExtension())) {
                agentExtensionNoEditText.setText(agentMasterPojo.getExtension());
            }
            if (!AUtils.isNull(agentMasterPojo.isActive())) {
                activeAgentCheckBox.setChecked(agentMasterPojo.isActive());
            }
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

    public void setSupportActionBar(Toolbar toolbar) {

        if (isEdit) {
            //toolbar.setTitle("EDIT AGENT");
            toolbar.setTitle(getResources().getString(R.string.str_title_edit_agent));
        } else {
            //toolbar.setTitle("NEW AGENT");
            toolbar.setTitle(getResources().getString(R.string.str_title_new_agent));
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

                    result = syncServer.saveAgentMaster(agentMasterPojo);

                    if (!AUtils.isNull(result) && result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        syncServer.getAgentMasterList();
                    }
                }

                @Override
                public void onFinished() {
                    if (!AUtils.isNull(result)) {

                        if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                            Toasty.success(EditAgentMasterActivity.this, "Agent saved successfully", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK, new Intent());
                            EditAgentMasterActivity.this.finish();
                        } else {
                            Toasty.error(EditAgentMasterActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toasty.error(EditAgentMasterActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();
        }
    }

    private void getFormData() {

        if (!AUtils.isNullString(agentNameEditText.getText().toString())) {

            agentMasterPojo.setOperatorName(agentNameEditText.getText().toString());
        } else {
            agentMasterPojo.setOperatorName("");
        }
        if (!AUtils.isNullString(agentNumberEditText.getText().toString())) {

            agentMasterPojo.setOperatorNo(agentNumberEditText.getText().toString());
        }
        if (!AUtils.isNullString(agentExtensionNoEditText.getText().toString())) {
            agentMasterPojo.setExtension(agentExtensionNoEditText.getText().toString());
        } else {
            agentMasterPojo.setExtension("");
        }
        agentMasterPojo.setActive(activeAgentCheckBox.isChecked());
    }

    private boolean isFormValid() {

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
        return true;
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        setResult(Activity.RESULT_CANCELED, new Intent());
        EditAgentMasterActivity.this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED, new Intent());
    }
}
