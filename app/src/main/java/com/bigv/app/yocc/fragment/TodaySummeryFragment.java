package com.bigv.app.yocc.fragment;

/**
 * Created by MiTHUN on 12/9/17.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.pojo.HomeScreenPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.google.gson.Gson;

import quickutils.core.QuickUtils;


public class TodaySummeryFragment extends Fragment {


    private static final String TAG = "TodaySummeryFragment";
    private View view;
    private Context context;
    private TextView totalCallsTextView;
    private TextView answeredCallsTextView;
    private TextView unansweredCallsTextView;
    private TextView unoptedCallsTextView;
    private HomeScreenPojo homeScreenPojo;

    public static Fragment newInstance() {
        TodaySummeryFragment fragment = new TodaySummeryFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.today_summery_fragment, container, false);
        context = container.getContext();
        initComponants();
        return view;
    }

    private void initComponants() {
        genrateId();
        registerEvents();
        initData();
    }

    private void genrateId() {

        totalCallsTextView = view.findViewById(R.id.total_calls_today_TV);
        answeredCallsTextView = view.findViewById(R.id.answered_call_today_TV);
        unansweredCallsTextView = view.findViewById(R.id.unanswered_call_today_TV);
        unoptedCallsTextView = view.findViewById(R.id.unopted_calls_today_TV);
    }

    private void initData() {

        if (!AUtils.isNull(QuickUtils.prefs.getString(AUtils.HOME_SCREEN_POJO, null))) {
            homeScreenPojo = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.HOME_SCREEN_POJO, null), HomeScreenPojo.class);
            initTodaysCallSummery();
            
        } else {

            totalCallsTextView.setText("0");
            answeredCallsTextView.setText("0");
            unansweredCallsTextView.setText("0");
            unoptedCallsTextView.setText("0");
        }
    }

    private void initTodaysCallSummery() {


        if (!AUtils.isNull(homeScreenPojo.getTotalCall())) {
            totalCallsTextView.setText("" + homeScreenPojo.getTotalCall());
        } else {
            totalCallsTextView.setText("0");
        }
        if (!AUtils.isNull(homeScreenPojo.getTotalAnsweredCallCount())) {
            answeredCallsTextView.setText("" + homeScreenPojo.getTotalAnsweredCallCount());
        } else {
            answeredCallsTextView.setText("0");
        }
        if (!AUtils.isNull(homeScreenPojo.getTotalNotAnsweredCallCount())) {
            unansweredCallsTextView.setText("" + homeScreenPojo.getTotalNotAnsweredCallCount());
        } else {
            unansweredCallsTextView.setText("0");
        }
        if (!AUtils.isNull(homeScreenPojo.getTotalUnoptedCallCount())) {
            unoptedCallsTextView.setText("" + homeScreenPojo.getTotalUnoptedCallCount());
        } else {
            unoptedCallsTextView.setText("0");
        }
    }

    private void registerEvents() {
    }
}