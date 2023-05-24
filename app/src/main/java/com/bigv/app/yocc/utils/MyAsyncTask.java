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

public class MyAsyncTask extends AsyncTask {

    private Context context;
    public SyncServer syncServer;
    private boolean isNetworkAvail = false;
    private boolean isShowPrgressDialog;
    private MyProgressDialog myProgressDialog;
    private AsynTaskListener asynTaskListener;


    public MyAsyncTask(Context context, boolean isShowPrgressDialog, AsynTaskListener asynTaskListener) {

        this.asynTaskListener = asynTaskListener;
        this.context = context;
        this.syncServer = new SyncServer(context);
        this.isShowPrgressDialog = isShowPrgressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myProgressDialog = new MyProgressDialog(context, R.drawable.ic_progress, false);
        if (isShowPrgressDialog) {
            myProgressDialog.show();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        if (AUtils.isNetWorkAvailable(context)) {

            try {

                isNetworkAvail = true;
                asynTaskListener.doInBackgroundOpration(syncServer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (myProgressDialog.isShowing()) {
            myProgressDialog.dismiss();
        }
        if (isNetworkAvail) {

            asynTaskListener.onFinished();
        } else {

            Toasty.warning(context, context.getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
        }
    }

    public interface AsynTaskListener {

        public void doInBackgroundOpration(SyncServer syncServer);

        public void onFinished();
    }
}
