package com.bigv.app.yocc.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.mithsoft.lib.activity.BaseActivity;

import com.bigv.app.yocc.R;

/**
 * Created by MiTHUN on 5/12/17.
 * updated by Rahul Rokade 28/11/2022
 */

public abstract class MyBaseActivity extends BaseActivity {


    public void setToolbarTitle(String title) {

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar,title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void setSupportActionBar(Toolbar toolbar, String title){
        toolbar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        setResult(Activity.RESULT_CANCELED, new Intent());
        finish();
        return super.onOptionsItemSelected(item);
    }

}
