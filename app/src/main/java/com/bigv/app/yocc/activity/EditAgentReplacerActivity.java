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

import androidx.appcompat.widget.Toolbar;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.AgentReplacerAgentListAdapter;
import com.bigv.app.yocc.adapter.AgentReplacerLanguageListAdapter;
import com.bigv.app.yocc.adapter.AgentReplacerMenuListAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.AgentMasterPojo;
import com.bigv.app.yocc.pojo.AgentReplacerPojo;
import com.bigv.app.yocc.pojo.LanguagePojo;
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
 */

public class EditAgentReplacerActivity extends BaseActivity {

    private static final String TAG = "EditAgentReplacActivity";
    private Spinner menuSpinner;
    private Spinner agentNameSpinner;
    private Spinner languageSpinner;
    private EditText agentSequenceNoEditText;
    private Button saveButton;
    private Toolbar toolbar;
    private boolean isEdit = false;
    private AgentReplacerPojo agentReplacerPojo = null;
    private List<AgentMasterPojo> agentMasterPojoList;
    private List<MenuMasterPojo> menuMasterPojoList;
    private AgentReplacerAgentListAdapter agentReplacerAgentListAdapter;
    private int agentId;
    private AgentReplacerMenuListAdapter agentReplacerMenuListAdapter;
    private String menuName;
    private List<LanguagePojo> languagePojoList;
    private AgentReplacerLanguageListAdapter agentReplacerLanguageListAdapter;
    private String menuId;

    @Override
    protected void genrateId() {

        setContentView(R.layout.edit_agent_replacer_activity);

        menuSpinner = findViewById(R.id.agent_replacer_menu_name_sp);
        agentNameSpinner = findViewById(R.id.agent_replacer_agent_name_sp);
        languageSpinner = findViewById(R.id.agent_replacer_language_sp);
        agentSequenceNoEditText = findViewById(R.id.agent_replacer_sequence_number_et);
        saveButton = findViewById(R.id.save_btn);

        initMenuNameSpinner();
        initAgentNameSpinner();
        initLanguageSpinner();
        initIntentData();
        initToolbar();
    }

    private void initMenuNameSpinner() {

        Type type = new TypeToken<List<MenuMasterPojo>>() {
        }.getType();

        menuMasterPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.MENU_MASTER_LIST, null), type);

        MenuMasterPojo menuMasterPojo = new MenuMasterPojo();
        menuMasterPojo.setMenuName("");
        menuMasterPojo.setMenuDescription("Select Menu");

        if (AUtils.isNull(menuMasterPojoList) || menuMasterPojoList.isEmpty()) {

            menuMasterPojoList = new ArrayList<MenuMasterPojo>();
        }

        menuMasterPojoList.add(0, menuMasterPojo);
        agentReplacerMenuListAdapter = new AgentReplacerMenuListAdapter(this, android.R.layout.simple_spinner_item,
                menuMasterPojoList);
        menuSpinner.setAdapter(agentReplacerMenuListAdapter);
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

    private void initLanguageSpinner() {

        Type type = new TypeToken<List<LanguagePojo>>() {
        }.getType();

        languagePojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.LANGUAGE_LIST, null), type);

        LanguagePojo languagePojo = new LanguagePojo();
        languagePojo.setId("");
        languagePojo.setLanguage("Select Language");

        if (AUtils.isNull(languagePojoList) || languagePojoList.isEmpty()) {

            languageSpinner.setVisibility(View.GONE);
            languagePojoList = new ArrayList<LanguagePojo>();
        }
        languagePojoList.add(0, languagePojo);
        agentReplacerLanguageListAdapter = new AgentReplacerLanguageListAdapter(this,
                android.R.layout.simple_spinner_item, languagePojoList);
        languageSpinner.setAdapter(agentReplacerLanguageListAdapter);
    }

    private void initIntentData() {

        Intent intent = getIntent();
        isEdit = intent.getExtras().getBoolean(AUtils.IS_EDIT);
        agentReplacerPojo = (AgentReplacerPojo) intent.getSerializableExtra(AUtils.AGENT_REPLACER_POJO);
        if (AUtils.isNull(agentReplacerPojo)) {
            agentReplacerPojo = new AgentReplacerPojo();
            agentSequenceNoEditText.setVisibility(View.GONE);
            menuSpinner.setEnabled(true);
        } else {
            agentSequenceNoEditText.setVisibility(View.VISIBLE);
            menuSpinner.setEnabled(false);

            if (!AUtils.isNullString(agentReplacerPojo.getMenuId())) {
                for (MenuMasterPojo menuMasterPojo : menuMasterPojoList) {
                    if (!AUtils.isNullString(menuMasterPojo.getMenuId())) {
                        if (menuMasterPojo.getMenuId().equals(agentReplacerPojo.getMenuId())) {

                            menuSpinner.setSelection(agentReplacerMenuListAdapter.getPosition(menuMasterPojo));
                            break;
                        }
                    }
                }
            }
            if (!AUtils.isNull(agentReplacerPojo.getOperatorId())) {

                if (!AUtils.isNull(agentMasterPojoList) && !agentMasterPojoList.isEmpty()) {
                    for (AgentMasterPojo agentMasterPojo : agentMasterPojoList) {
                        if (agentMasterPojo.getOperatorId() == agentReplacerPojo.getOperatorId()) {

                            agentNameSpinner.setSelection(agentReplacerAgentListAdapter.getPosition(agentMasterPojo));
                            break;
                        }
                    }
                }
            }
            if (!AUtils.isNullString(agentReplacerPojo.getLanguageKey())) {
                for (LanguagePojo languagePojo : languagePojoList) {
                    if (languagePojo.getId().equals(agentReplacerPojo.getLanguageKey())) {

                        languageSpinner.setSelection(agentReplacerLanguageListAdapter.getPosition(languagePojo));
                        break;
                    }
                }
            }
            if (!AUtils.isNullString(agentReplacerPojo.getSequence())) {

                agentSequenceNoEditText.setText(agentReplacerPojo.getSequence());
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
            //toolbar.setTitle("EDIT AGENT REPLACER");
            toolbar.setTitle(getResources().getString(R.string.str_title_edit_agent_replacer));
        } else {
            //toolbar.setTitle("NEW AGENT REPLACER");
            toolbar.setTitle(getResources().getString(R.string.str_title_new_agent_replacer));
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

        agentNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                agentId = agentReplacerAgentListAdapter.getItem(position).getOperatorId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                menuName = agentReplacerMenuListAdapter.getItem(position).getMenuName();
                menuId = agentReplacerMenuListAdapter.getItem(position).getMenuId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    private void saveButtonOnClick() {
        if (isFormValid()) {

            agentReplacerPojo.setOperatorId(agentId);
            agentReplacerPojo.setMenuId(menuId);
            agentReplacerPojo.setMenuName(menuName);
            agentReplacerPojo.setLanguageKey("");

            new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {

                public ResultPojo result = null;

                @Override
                public void doInBackgroundOpration(SyncServer syncServer) {

                    result = syncServer.saveAgentReplacer(agentReplacerPojo);

                    if (!AUtils.isNull(result) && result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                        syncServer.getAgentReplacerList();
                    }
                }

                @Override
                public void onFinished() {
                    if (!AUtils.isNull(result)) {

                        if (result.getStatus().equals(AUtils.STATUS_SUCCESS)) {
                            Toasty.success(EditAgentReplacerActivity.this, "Agent relacer saved successfully", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_OK, new Intent());
                            EditAgentReplacerActivity.this.finish();
                        } else {
                            Toasty.error(EditAgentReplacerActivity.this, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toasty.error(EditAgentReplacerActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();
        }
    }

    private boolean isFormValid() {


        if (isEdit) {

            if (agentNameSpinner.getSelectedItemPosition() == 0) {
                Toasty.warning(this, "Please select agent", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {

            if (menuSpinner.getSelectedItemPosition() == 0) {
                Toasty.warning(this, "Please select Menu", Toast.LENGTH_SHORT).show();
                return false;
            }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        EditAgentReplacerActivity.this.finish();
        setResult(Activity.RESULT_CANCELED, new Intent());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED, new Intent());
    }
}
