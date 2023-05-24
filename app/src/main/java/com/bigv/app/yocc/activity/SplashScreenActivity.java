package com.bigv.app.yocc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import quickutils.core.QuickUtils;
import com.bigv.app.yocc.R;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;

/**
 * Created by mithun on 17/8/17.
 */

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (QuickUtils.prefs.getBoolean(AUtils.IS_USER_LOGIN, false)) {

                    loadDataFromServer();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                }
            }
        }, 10);
    }

    private void loadDataFromServer() {

        new MyAsyncTask(SplashScreenActivity.this, false,
                new MyAsyncTask.AsynTaskListener() {
                    @Override
                    public void doInBackgroundOpration(SyncServer syncServer) {

                        syncServer.getHomeScreenItems();
                        String todaysDate = AUtils.getTodaysDate(AUtils.SERVER_DATE_FORMATE);
                        syncServer.getCallDetailsList(todaysDate + " 00:00:00", todaysDate + " 23:59:59");
                        syncServer.getCallDetailsSummery(todaysDate + " 00:00:00", todaysDate + " 23:59:59");
                        syncServer.getWeekWiseCallDuration();
                        syncServer.getWeekWiseCall();
                        syncServer.getMonthWiseCall();
//                        syncServer.getHourWiseCall();
                        syncServer.getCallPriorityList();
                    }

                    @Override
                    public void onFinished() {

                        startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                    }
                }).execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}