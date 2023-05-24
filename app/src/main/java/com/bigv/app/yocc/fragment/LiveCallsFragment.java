package com.bigv.app.yocc.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.activity.LoginActivity;
import com.bigv.app.yocc.adapter.LiveCallAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.HomeScreenPojo;
import com.bigv.app.yocc.pojo.LiveCallPojo;
import com.bigv.app.yocc.pojo.PasswordCheckPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.bigv.app.yocc.webservices.LoginWebservices;
import com.google.gson.Gson;
import com.mithsoft.lib.componants.Toasty;

import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MiTHUN on 12/9/17.
 */

public class LiveCallsFragment extends Fragment {

    private static final String TAG = "LiveCallsFragment";
    private View view;
    private Context context;
    private ListView liveCallListView;
    private LiveCallAdapter liveCallAdapter;
    private boolean isScreenVisible = false;
    private List<LiveCallPojo> liveCallPojoList;
    private int firstTime = 0;
    private HomeScreenPojo homeScreenPojo;
    private String uerName, password,key;

    public static Fragment newInstance() {
        LiveCallsFragment fragment = new LiveCallsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.live_calls_fragment, container, false);
        context = container.getContext();
        initComponants();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        isScreenVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isScreenVisible = true;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            firstTime++;
//            Log.e(TAG, "Visible");
            isScreenVisible = true;
            getLiveCallData();
            if (firstTime == 1) {
                setDataToListAdapterFirstTime();
            }
        } else {
//            Log.e(TAG, "InVisible");
            firstTime = 0;
            if (!AUtils.isNull(context)) {
                isScreenVisible = false;
            }
        }
    }

    private void getLiveCallData() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                getLiveCallDataFromServer();
            }
        }, 2 * 1000);
    }

    private void getLiveCallDataFromServer() {

        new MyAsyncTask(context, false, new MyAsyncTask.AsynTaskListener() {

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                liveCallPojoList = syncServer.getLiveCallList();
            }

            @Override
            public void onFinished() {

                if (isScreenVisible) {
                    if (AUtils.isNull(liveCallPojoList) || liveCallPojoList.isEmpty()) {

                        liveCallPojoList = new ArrayList<LiveCallPojo>();
                    }
                    setDataToListAdapter();
                    getLiveCallData();
                }
            }
        }).execute();
    }


    private void initComponants() {
        chePassWordApi();
        //checkHomeStatus();
        genrateId();
        registerEvents();
    }

    private void genrateId() {

        liveCallListView = view.findViewById(R.id.call_details_LV);
    }

    private void registerEvents() {
    }


    private void setDataToListAdapter() {

//        Type type = new TypeToken<List<LiveCallPojo>>() {
//        }.getType();

//        List<LiveCallPojo> liveCallPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.LIVE_CALL_LIST, null), type);

//        if (!AUtils.isNull(liveCallPojoList) && !liveCallPojoList.isEmpty()) {

        liveCallAdapter = new LiveCallAdapter(context, liveCallPojoList);
        liveCallListView.setAdapter(liveCallAdapter);

//        }
    }

    private void setDataToListAdapterFirstTime() {

        List<LiveCallPojo> liveCallPojoList = new ArrayList<LiveCallPojo>();

        liveCallAdapter = new LiveCallAdapter(context, liveCallPojoList);
        liveCallListView.setAdapter(liveCallAdapter);
    }

    private void checkHomeStatus() {
        if (!AUtils.isNull(QuickUtils.prefs.getString(AUtils.HOME_SCREEN_POJO, null))) {
            homeScreenPojo = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.HOME_SCREEN_POJO, null), HomeScreenPojo.class);

            if (homeScreenPojo.isStatus() == false){
                QuickUtils.prefs.remove(AUtils.USER_DATA);
                QuickUtils.prefs.remove(AUtils.USERNAME);
                QuickUtils.prefs.remove(AUtils.PASSWORD);
                QuickUtils.prefs.remove(AUtils.IS_USER_LOGIN);
                QuickUtils.prefs.remove(AUtils.KEY);

                Toasty.success(getActivity(), "Logout success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
                getActivity().finish();
                getActivity().finishAffinity();
                Log.i("Rahul", "password not Match");

            }else if (homeScreenPojo.isStatus() == true){
                Log.i("Rahul", "Password matched successfully live call frag");

            }
        }
    }

    private void chePassWordApi(){
        LoginWebservices checkPass = AUtils.createService(LoginWebservices.class,AUtils.SERVER_URL);
        checkPass.checkPassApi(QuickUtils.prefs.getString(AUtils.PASSWORD,""),
                QuickUtils.prefs.getString(AUtils.KEY,""),QuickUtils.prefs.getString(AUtils.USERNAME,"")).enqueue(new Callback<PasswordCheckPojo>() {
            @Override
            public void onResponse(retrofit2.Call<PasswordCheckPojo> call, Response<PasswordCheckPojo> response) {
                if (response.body() != null){
                    if (response.body().isPasswordstatus() == false){
                        QuickUtils.prefs.remove(AUtils.USER_DATA);
                        QuickUtils.prefs.remove(AUtils.USERNAME);
                        QuickUtils.prefs.remove(AUtils.PASSWORD);
                        QuickUtils.prefs.remove(AUtils.IS_USER_LOGIN);
                        QuickUtils.prefs.remove(AUtils.KEY);

                        Toasty.success(getActivity(), "Logout success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                        getActivity().finishAffinity();
                        Log.i("Rahul", "password not Match");
                    }else if (response.body().isPasswordstatus() == true){
                        Log.i("Rahul", "Password matched successfully live call frag - check pass Api");

                    }
                }
            }

            @Override
            public void onFailure(Call<PasswordCheckPojo> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}