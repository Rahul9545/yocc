package com.bigv.app.yocc.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mithsoft.lib.componants.MyProgressDialog;
import com.mithsoft.lib.componants.Toasty;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.controller.SyncServer;

/**
 * Created by mithun on 5/9/17.
 */

public abstract class MyAsyncTask321 extends AsyncTask {


    public SyncServer syncServer;
    private Context context;
    private boolean isNetworkAvail = false;
    private boolean isShowPrgressDialog;
    private MyProgressDialog myProgressDialog;


    public MyAsyncTask321(Context context, boolean isShowPrgressDialog) {

        this.context = context;
        this.syncServer = new SyncServer(context);
        this.isShowPrgressDialog = isShowPrgressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myProgressDialog = new MyProgressDialog(context, R.drawable.ic_info_outline_white_48dp, false);
        if (isShowPrgressDialog) {
            myProgressDialog.show();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        if (AUtils.isNetWorkAvailable(context)) {
            isNetworkAvail = true;
            doInBackgroundOpration();
        }
        return null;
    }

    protected abstract void doInBackgroundOpration();

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
        if (isNetworkAvail) {

            onFinished();
        } else {

            Toasty.warning(context, context.getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
        }
    }

    abstract public void onFinished();
}
