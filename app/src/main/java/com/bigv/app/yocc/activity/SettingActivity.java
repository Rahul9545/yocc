package com.bigv.app.yocc.activity;

import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import com.mithsoft.lib.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.SettingsListAdapter;

/**
 * Created by MiTHUN on 20/11/17.
 * updated by Rahul 12/01/22.
 */

public class SettingActivity extends BaseActivity {

    private Toolbar toolbar;
    private ListView listView;

    @Override
    protected void genrateId() {

        setContentView(R.layout.setting_activity);
        listView = findViewById(R.id.settings_lv);
        initToolbar();
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
        //toolbar.setTitle("SETTINGS");
        toolbar.setTitle(getResources().getString(R.string.str_title_setting));
    }

    @Override
    protected void registerEvents() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listViewOnClick(position);
            }
        });
    }

    private void listViewOnClick(int position) {

        switch (position) {
            case 0:
                startActivity(new Intent(SettingActivity.this, MenuMasterActivity.class));
                break;
            case 1:
                startActivity(new Intent(SettingActivity.this, AgentMasterActivity.class));
                break;
            case 2:
                startActivity(new Intent(SettingActivity.this, AgentReplacerActivity.class));
                break;
            case 3:
                startActivity(new Intent(SettingActivity.this, ChangePasswordActivity.class));
                break;
        }
    }


    @Override
    protected void initData() {

        setDataToAdapter();
    }

    private void setDataToAdapter() {


        List<String> settingItemList = new ArrayList<String>();

        settingItemList.add("Menu Master");
        settingItemList.add("Agent Master");
        settingItemList.add("Agent Replacer Master");
        settingItemList.add("Change Password");

        SettingsListAdapter settingsListAdapter = new SettingsListAdapter(this, settingItemList);
        listView.setAdapter(settingsListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

//        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
//        if (id == R.id.toolbar) {
//            return true;
//        }

        SettingActivity.this.finish();

        return super.onOptionsItemSelected(item);
    }


}
