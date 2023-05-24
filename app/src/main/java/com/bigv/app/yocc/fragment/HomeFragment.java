package com.bigv.app.yocc.fragment;

/**
 * Created by MiTHUN on 28/10/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.activity.HomeActivity;
import com.bigv.app.yocc.activity.LoginActivity;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.HomeScreenPojo;
import com.bigv.app.yocc.pojo.MonthWiseCallPojo;
import com.bigv.app.yocc.pojo.PasswordCheckPojo;
import com.bigv.app.yocc.pojo.WeekWiseCallDurationPojo;
import com.bigv.app.yocc.pojo.WeekWiseCallPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.bigv.app.yocc.webservices.LoginWebservices;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.componants.Toasty;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;

import java.lang.reflect.Type;
import java.util.List;

import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private View view;
    private Context context;
    private TabLayout tabLayout;
    private TextView liveCallsCountTextView;
    private TextView todaysCallsCountTextView;
    private TextView callsCreditTextView;
    private TextView smsTextView;
    private LinearLayout liveCallLinearLayout;
    private LinearLayout todaysCallLinearLayout;
    private HomeScreenPojo homeScreenPojo;
    private BarChart weekWiseCallDurationBarChart;
    private BarChart weekWiseCallBarChart;
    private StackedBarChart monthWiseCallBarChart;
    //    private com.github.mikephil.charting.charts.LineChart hourwiseCallLineChart;
    private ValueLineChart hourwiseCallLineChart;
    private ViewPager viewPager;
    private boolean isScreenVisible = false;
    private CardView outerBalabceCreditedCardView;
    private CardView outerCallDurationGraphCardView;
    private String uerName, password,key;


    public static Fragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        context = container.getContext();
        initComponants();
        return view;
    }

    private void initComponants() {
        chePassWordApi();
        //checkHomeStatus();
        genrateId();
        registerEvents();
        initTabLayout();
        initData();
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
            isScreenVisible = true;
//            Log.e(TAG, "Visible");
            getUpdatedData();
        } else {
//            Log.e(TAG, "InVisible");
            if (!AUtils.isNull(context)) {
                isScreenVisible = false;
            }
        }
    }

    private void getUpdatedData() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                getLiveDataFromServer();
            }
        }, 2 * 1000);
    }

    private void getLiveDataFromServer() {

        new MyAsyncTask(context, false, new MyAsyncTask.AsynTaskListener() {

            public HomeScreenPojo homeScreenItems = null;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                homeScreenItems = syncServer.getHomeScreenItems();
            }

            @Override
            public void onFinished() {

                if (isScreenVisible) {

                    if (!AUtils.isNull(homeScreenItems)) {
                        // check if the object is null then update
                        initHomeScreenData();
                    }
                    getUpdatedData();
                }
            }
        }).execute();
    }


    private void genrateId() {

        tabLayout = view.findViewById(R.id.tabLayoutHome);
        liveCallsCountTextView = view.findViewById(R.id.live_calls_count_home_tv);
        todaysCallsCountTextView = view.findViewById(R.id.todays_calls_count_home_tv);
        callsCreditTextView = view.findViewById(R.id.call_credit_home_tv);
        smsTextView = view.findViewById(R.id.sms_home_tv);
        liveCallLinearLayout = view.findViewById(R.id.live_call_ll);
        todaysCallLinearLayout = view.findViewById(R.id.todays_call_ll);
        weekWiseCallDurationBarChart = view.findViewById(R.id.week_wise_call_duration_bar_chart);
        weekWiseCallBarChart = view.findViewById(R.id.week_wise_call_bar_chart);
        monthWiseCallBarChart = view.findViewById(R.id.month_wise_stacked_bar_chart);
        hourwiseCallLineChart = view.findViewById(R.id.line_chart);
        viewPager = getActivity().findViewById(R.id.viewPagerTab);
        outerBalabceCreditedCardView = view.findViewById(R.id.outer_balance_credit_cv);
        outerCallDurationGraphCardView = view.findViewById(R.id.outer_call_duration_graph_cv);

        if (QuickUtils.prefs.getBoolean(AUtils.IS_LOGIN_BY_AGENT, false)) {

            outerBalabceCreditedCardView.setVisibility(View.GONE);
            outerCallDurationGraphCardView.setVisibility(View.GONE);
        }else if (QuickUtils.prefs.getString(AUtils.USER_TYPE_ID,"0").equals("3")){
            outerCallDurationGraphCardView.setVisibility(View.GONE);
        }
    }

    private void registerEvents() {

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        todaysCallLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                todaysCallLinearLayoutOnClick();
            }
        });
        liveCallLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                liveCallLinearLayoutOnClick();
            }
        });
    }

    private void liveCallLinearLayoutOnClick() {

//        AUtils.getDeviceDrawable(context);
        if (homeScreenPojo.getTotalLiveCall() > 0) {
            viewPager.setCurrentItem(1);
        } else {
            Toasty.info(context, "No calls available", Toast.LENGTH_SHORT).show();
        }
    }

    private void todaysCallLinearLayoutOnClick() {

        if (homeScreenPojo.getTotalCall() > 0) {
            // set the value true and check in calldetails frgment to update call list
            QuickUtils.prefs.save(AUtils.UPDATE_CALL_LIST, true);
            viewPager.setCurrentItem(3);
        } else {
            Toasty.info(context, "No calls available", Toast.LENGTH_SHORT).show();
        }
    }

    private void initTabLayout() {

        tabLayout.addTab(tabLayout.newTab().setText("Today Summary"), true);
        tabLayout.addTab(tabLayout.newTab().setText("Total Summary"));
    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(new TodaySummeryFragment());
                break;
            case 1:
                replaceFragment(new TotalSummeryFragment());
                chePassWordApi();
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void initData() {

        initHomeScreenData();

        setDataToWeekWiseCallDurationGraph();
        setDataToWeekWiseCallGraph();
        setDataToMonthWiseCallGraph();
//        setDataToHourWiseCallGraph();
    }

    private void setDataToMonthWiseCallGraph() {

        Type type = new TypeToken<List<MonthWiseCallPojo>>() {
        }.getType();

        List<MonthWiseCallPojo> monthWiseCallPojoList = new Gson().fromJson(
                QuickUtils.prefs.getString(AUtils.MONTH_WISE_CALL_LIST, null), type);

        if (!AUtils.isNull(monthWiseCallPojoList) && !monthWiseCallPojoList.isEmpty()) {

            StackedBarModel s1 = null;
            for (MonthWiseCallPojo monthWiseCallPojo : monthWiseCallPojoList) {

                s1 = new StackedBarModel("" + monthWiseCallPojo.getMonthName().substring(0, 3));

                s1.addBar(new BarModel(monthWiseCallPojo.getAnswered(), 0xFF20A7DF));
                s1.addBar(new BarModel(monthWiseCallPojo.getNotAnswered(), 0xFF85CCEC));
                s1.addBar(new BarModel(monthWiseCallPojo.getUnopted(), 0xFF15658A));
                monthWiseCallBarChart.addBar(s1);
            }

        }
    }

//    private void setDataToHourWiseCallGraph() {
//
//        ValueLineSeries series = new ValueLineSeries();
//        series.setColor(0xFF4989c7);
//        series.addPoint(new ValueLinePoint(4.4f));
//        series.addPoint(new ValueLinePoint(2.4f));
//        series.addPoint(new ValueLinePoint(3.2f));
//        series.addPoint(new ValueLinePoint(2.6f));
//        series.addPoint(new ValueLinePoint(5.0f));
//        series.addPoint(new ValueLinePoint(3.5f));
//        series.addPoint(new ValueLinePoint(2.4f));
//        series.addPoint(new ValueLinePoint(0.4f));
//        series.addPoint(new ValueLinePoint(3.4f));
//        series.addPoint(new ValueLinePoint(2.5f));
//        series.addPoint(new ValueLinePoint(1.4f));
//        series.addPoint(new ValueLinePoint(4.4f));
//        series.addPoint(new ValueLinePoint(2.4f));
//        series.addPoint(new ValueLinePoint(3.2f));
//        series.addPoint(new ValueLinePoint(2.6f));
//        series.addPoint(new ValueLinePoint(5.0f));
//        series.addPoint(new ValueLinePoint(3.5f));
//        series.addPoint(new ValueLinePoint(2.4f));
//        series.addPoint(new ValueLinePoint(0.4f));
//        series.addPoint(new ValueLinePoint(3.4f));
//        series.addPoint(new ValueLinePoint(2.5f));
//        series.addPoint(new ValueLinePoint(1.0f));
//        series.addPoint(new ValueLinePoint(4.4f));
//        series.addPoint(new ValueLinePoint(2.4f));
//
////        mValueLineChart.addStandardValue(new StandardValue(140f));
////        mValueLineChart.addStandardValue(new StandardValue(163.4f));
//        hourwiseCallLineChart.addSeries(series);
//        hourwiseCallLineChart.setOnPointFocusedListener(new IOnPointFocusedListener() {
//            @Override
//            public void onPointFocused(int _PointPos) {
//                Log.d("Test", "Pos: " + _PointPos);
//            }
//        });
//    }

    private void initHomeScreenData() {

        if (!AUtils.isNull(QuickUtils.prefs.getString(AUtils.HOME_SCREEN_POJO, null))) {
            homeScreenPojo = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.HOME_SCREEN_POJO, null), HomeScreenPojo.class);

            if (!AUtils.isNull(homeScreenPojo.getTotalLiveCall())) {
                liveCallsCountTextView.setText("" + homeScreenPojo.getTotalLiveCall());
            } else {
                liveCallsCountTextView.setText("0");
            }
            if (!AUtils.isNull(homeScreenPojo.getTotalCall())) {
                todaysCallsCountTextView.setText("" + homeScreenPojo.getTotalCall());
            } else {
                todaysCallsCountTextView.setText("0");
            }
            if (!AUtils.isNull(homeScreenPojo.getCallTransferBalance())) {
                callsCreditTextView.setText("" + homeScreenPojo.getCallTransferBalance());
            } else {
                callsCreditTextView.setText("0");
            }
            if (!AUtils.isNull(homeScreenPojo.getSmsBalanace())) {
                smsTextView.setText("" + homeScreenPojo.getSmsBalanace());
            } else {
                smsTextView.setText("0");
            }
        } else {

            liveCallsCountTextView.setText("0");
            todaysCallsCountTextView.setText("0");
            callsCreditTextView.setText("0");
            smsTextView.setText("0");
        }
    }

    private void setDataToWeekWiseCallDurationGraph() {

        Type type = new TypeToken<List<WeekWiseCallDurationPojo>>() {
        }.getType();

        List<WeekWiseCallDurationPojo> weekWiseCallDurationPojoList = new Gson().fromJson(
                QuickUtils.prefs.getString(AUtils.WEEK_WISE_CALL_DURATION_LIST, null), type);

        if (!AUtils.isNull(weekWiseCallDurationPojoList) && !weekWiseCallDurationPojoList.isEmpty()) {

            for (WeekWiseCallDurationPojo weekWiseCallDurationPojo : weekWiseCallDurationPojoList) {

                if (!AUtils.isNullString(weekWiseCallDurationPojo.getCallDuration())) {

                    String duration = weekWiseCallDurationPojo.getCallDuration().replace(":", ".");

                    weekWiseCallDurationBarChart.addBar(new BarModel("" + weekWiseCallDurationPojo.getDay(),
                            Float.parseFloat(duration), 0xFF4989c7));
                }
            }
            weekWiseCallDurationBarChart.startAnimation();
        }
    }

    private void setDataToWeekWiseCallGraph() {

        Type type = new TypeToken<List<WeekWiseCallPojo>>() {
        }.getType();

        List<WeekWiseCallPojo> weekWiseCallPojoList = new Gson().fromJson(
                QuickUtils.prefs.getString(AUtils.WEEK_WISE_CALL_LIST, null), type);

        if (!AUtils.isNull(weekWiseCallPojoList) && !weekWiseCallPojoList.isEmpty()) {

            for (WeekWiseCallPojo weekWiseCallPojo : weekWiseCallPojoList) {

                if (!AUtils.isNullString(weekWiseCallPojo.getTotalCall())) {

                    String totalCall = weekWiseCallPojo.getTotalCall();

                    weekWiseCallBarChart.addBar(new BarModel("" + weekWiseCallPojo.getDay(),
                            Float.parseFloat(totalCall), 0xFF4989c7));
                }
            }
            weekWiseCallBarChart.startAnimation();
        }
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
                Log.i("Rahul", "Password matched successfully");
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
                        Log.i("Rahul", "Password matched successfully Home fragment - check pass Api");
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