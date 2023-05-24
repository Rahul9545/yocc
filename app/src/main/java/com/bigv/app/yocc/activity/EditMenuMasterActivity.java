package com.bigv.app.yocc.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.RoutingPatternAdapter;
import com.bigv.app.yocc.adapter.RoutingTypeAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.MenuMasterPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.pojo.RoutingPatternPojo;
import com.bigv.app.yocc.pojo.RoutingTypePojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.componants.Toasty;

import java.lang.reflect.Type;
import java.util.List;

import quickutils.core.QuickUtils;

/**
 * Created by MiTHUN on 20/11/17.
 */

public class EditMenuMasterActivity extends BaseActivity {

    private static final String TAG = "EditAgentMasterActivity";
    private EditText menuNameEditText;
    private EditText dialTimeoutEditText;
    private EditText menuDescEditText;
    private Button saveButton;
    private Toolbar toolbar;
    private boolean isEdit = false;
    private MenuMasterPojo menuMasterPojo = null;
    private CheckBox activeAgentCheckBox;
    private Spinner routingPatternSpinner;
    private Spinner routingTypeSpinner;
    private RoutingPatternAdapter routingPatternAdapter;
    private RoutingTypeAdapter routingTypeAdapter;
    private String routingPatternId = "";
    private String routingTypeId = "";
    private List<RoutingPatternPojo> routingPatternPojoList;
    private List<RoutingTypePojo> routingTypePojoList;


    @Override
    protected void genrateId() {

        setContentView(R.layout.edit_menu_master_activity);

        menuNameEditText = findViewById(R.id.menu_mst_menu_name_et);
        dialTimeoutEditText = findViewById(R.id.menu_mst_dial_timeout_et);
        menuDescEditText = findViewById(R.id.menu_mst_menu_desc_et);
        saveButton = findViewById(R.id.save_btn);
        activeAgentCheckBox = findViewById(R.id.agent_mst_agent_active_cb);
        routingPatternSpinner = findViewById(R.id.routing_pattern_sp);
        routingTypeSpinner = findViewById(R.id.routing_type_sp);

        initRoutingPatternSpinner();
        initRoutingTypeSpinner();
        initIntentData();
        initToolbar();
    }

    private void initRoutingTypeSpinner() {

        Type type = new TypeToken<List<RoutingTypePojo>>() {
        }.getType();

        routingTypePojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.ROUTING_TYPE_LIST, null), type);

        if (!AUtils.isNull(routingTypePojoList) && !routingTypePojoList.isEmpty()) {
            routingTypeAdapter = new RoutingTypeAdapter(this, android.R.layout.simple_spinner_item,
                    routingTypePojoList);
            routingTypeSpinner.setAdapter(routingTypeAdapter);
        }
    }

    private void initRoutingPatternSpinner() {

        Type type = new TypeToken<List<RoutingPatternPojo>>() {
        }.getType();

        routingPatternPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.ROUTING_PATTERN_LIST, null), type);

        if (!AUtils.isNull(routingPatternPojoList) && !routingPatternPojoList.isEmpty()) {
            routingPatternAdapter = new RoutingPatternAdapter(this, android.R.layout.simple_spinner_item,
                    routingPatternPojoList);
            routingPatternSpinner.setAdapter(routingPatternAdapter);
        }
    }

    private void initIntentData() {

        Intent intent = getIntent();
        isEdit = intent.getExtras().getBoolean(AUtils.IS_EDIT);
        menuMasterPojo = (MenuMasterPojo) intent.getSerializableExtra(AUtils.MENU_MASTER_POJO);
        if (AUtils.isNull(menuMasterPojo)) {

            Toasty.error(this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
            EditMenuMasterActivity.this.finish();
        } else {
            if (!AUtils.isNullString(menuMasterPojo.getMenuName())) {
                menuNameEditText.setText(menuMasterPojo.getMenuName());
            } else {
                menuNameEditText.setText("(No Name)");
            }
            if (!AUtils.isNullString(menuMasterPojo.getPatId())) {

                RoutingPatternPojo routingPatternPojoTemp = null;
                for (RoutingPatternPojo routingPatternPojo : routingPatternPojoList) {

                    if (routingPatternPojo.getId().equals(menuMasterPojo.getPatId())) {

                        routingPatternPojoTemp = routingPatternPojo;
                        break;
                    }
                }
                routingPatternSpinner.setSelection(routingPatternAdapter.getPosition(routingPatternPojoTemp));
            }
            if (!AUtils.isNullString(menuMasterPojo.getTypeId())) {

                RoutingTypePojo routingTypePojoTemp = null;
                for (RoutingTypePojo routingTypePojo : routingTypePojoList) {

                    if (routingTypePojo.getId().equals(menuMasterPojo.getTypeId())) {

                        routingTypePojoTemp = routingTypePojo;
                        break;
                    }
                }
                routingTypeSpinner.setSelection(routingTypeAdapter.getPosition(routingTypePojoTemp));
            }
            if (!AUtils.isNullString(menuMasterPojo.getDialTimeout())) {
                dialTimeoutEditText.setText(menuMasterPojo.getDialTimeout());
            }
            if (!AUtils.isNullString(menuMasterPojo.getMenuDescription())) {
                menuDescEditText.setText(menuMasterPojo.getMenuDescription());
            } else {
                menuDescEditText.setText("(No Desceription)");
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

    public void setSupportActionBar(Toolbar toolbar){
        if (isEdit) {
            //toolbar.setTitle("EDIT MENU");
            toolbar.setTitle(getResources().getString(R.string.str_title_edt_menu));
        } else {
            //toolbar.setTitle("NEW MENU");
            toolbar.setTitle(getResources().getString(R.string.str_title_new_menu));
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

        routingPatternSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                routingPatternId = routingPatternAdapter.getItem(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        routingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                routingTypeId = routingTypeAdapter.getItem(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    private void saveButtonOnClick() {
        if (isFormValid()) {

            menuMasterPojo.setPatId(routingPatternId);
            menuMasterPojo.setTypeId(routingTypeId);

            if (AUtils.isNullString(dialTimeoutEditText.getText().toString())) {
                menuMasterPojo.setDialTimeout(dialTimeoutEditText.getText().toString());
            } else {
                menuMasterPojo.setDialTimeout("");
            }

            new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {

                public ResultPojo result = null;

                @Override
                public void doInBackgroundOpration(SyncServer syncServer) {

                    result = syncServer.saveMenuMaster(menuMasterPojo);

                    if (!AUtils.isNull(result) && result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        syncServer.getMenuList();
                    }
                }

                @Override
                public void onFinished() {
                    if (!AUtils.isNull(result)) {

                        if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                            Toasty.success(EditMenuMasterActivity.this, "Menu saved successfully", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK, new Intent());
                            EditMenuMasterActivity.this.finish();
                        } else {
                            Toasty.error(EditMenuMasterActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toasty.error(EditMenuMasterActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();
        }
    }

    private boolean isFormValid() {

        if (AUtils.isNullString(dialTimeoutEditText.getText().toString())) {
            Toasty.warning(this, "Please enter dial timeout", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        EditMenuMasterActivity.this.finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED, new Intent());
    }
}
