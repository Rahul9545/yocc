package com.bigv.app.yocc.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by MiTHUN on 1/12/17.
 */

public class LiveCallBroadcast extends BroadcastReceiver {

    private static final String TAG = "LiveCallBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG, "Inside onReceive");
        Toast.makeText(context, "LiveCallBroadcast", Toast.LENGTH_SHORT).show();
    }
}
