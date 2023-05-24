package com.bigv.app.yocc.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.AddressBookMenuAdapter;
import com.bigv.app.yocc.utils.MyBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MiTHUN on 20/11/17.
 * updated by Rahul 12/01/22.
 */

public class AddressBookMenuActivity extends MyBaseActivity {

    private ListView listView;

    @Override
    protected void genrateId() {

        setContentView(R.layout.setting_activity);
        listView = findViewById(R.id.settings_lv);
        //setToolbarTitle("A D D R E S S  B O O K");
        setToolbarTitle(getResources().getString(R.string.str_title_address_book));
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
                startActivity(new Intent(AddressBookMenuActivity.this, GroupAddressBookActivity.class));
                break;
            case 1:
                startActivity(new Intent(AddressBookMenuActivity.this, ContactManagementActivity.class));
                break;
            case 2:
                startActivity(new Intent(AddressBookMenuActivity.this, AddressBookDownloadActivity.class));
                break;
            case 3:
//                startActivity(new Intent(AddressBookMenuActivity.this, ChangePasswordActivity.class));
                break;
        }
    }

    @Override
    protected void initData() {

        setDataToAdapter();
    }

    private void setDataToAdapter() {

        List<String> settingItemList = new ArrayList<String>();

        settingItemList.add("Group");
        settingItemList.add("Contact management");
        settingItemList.add("Download");

        AddressBookMenuAdapter addressBookMenuAdapter = new AddressBookMenuAdapter(this, settingItemList);
        listView.setAdapter(addressBookMenuAdapter);
    }
}
