package com.bigv.app.yocc.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.FeedbackAdapter;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.LiveCallPojo;
import com.bigv.app.yocc.pojo.RemarkResponsePojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.bigv.app.yocc.utils.RetrofitClient;
import com.bigv.app.yocc.webservices.RemarkApi;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.componants.Toasty;

import java.io.IOException;
import java.util.ArrayList;

import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends BaseActivity {
    private Context context;
    private Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FeedbackAdapter adapter;
    private TextView txtNoDataFound;
    private ArrayList<RemarkResponsePojo> remarkList;
    private String callerNumber, truncateString;
    private String key, userId;


    @Override
    protected void genrateId() {
        setContentView(R.layout.activity_notification);
        context = this;
        remarkList = new ArrayList<>();
        refreshLayout = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.recycler_view);
        txtNoDataFound = findViewById(R.id.txt_no_data);
        layoutManager = new LinearLayoutManager(context);
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

        toolbar.setTitle(getResources().getString(R.string.str_title_remark_notification));
    }

    @Override
    protected void registerEvents() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                if (AUtils.isNetworkConnected(context)){
                    txtNoDataFound.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    getRemarkNotificationServer();
                }else {
                    txtNoDataFound.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    protected void initData() {
        if (AUtils.isNetworkConnected(context)){
            txtNoDataFound.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            getRemarkNotificationServer();
        }else {
            txtNoDataFound.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        }
    }

    private void setAdapter(ArrayList<RemarkResponsePojo> remarkList) {
        refreshLayout.setRefreshing(false);
        txtNoDataFound.setVisibility(View.GONE);
        adapter = new FeedbackAdapter(context, remarkList, new FeedbackAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(RemarkResponsePojo pojo) {
                if (pojo != null){
                    callerNumber = pojo.getCaller();
                    truncateString = AUtils.truncateString(callerNumber,10);
                    Toast.makeText(context, truncateString, Toast.LENGTH_SHORT).show();
                }
                context.startActivity(new Intent(context, UpdateFeedActivity.class).putExtra("Caller_num",truncateString));
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getRemarkNotificationServer() {
        refreshLayout.setRefreshing(true);
        key = QuickUtils.prefs.getString(AUtils.KEY, "");
        userId = QuickUtils.prefs.getString(AUtils.AGENT_ID, "0");

        RemarkApi remarkApiWebService = RetrofitClient.createService(RemarkApi.class, AUtils.SERVER_URL);
        Call<ArrayList<RemarkResponsePojo>> getNotificationList = remarkApiWebService.remarkNotificationList(key,userId);

        getNotificationList.enqueue(new Callback<ArrayList<RemarkResponsePojo>>() {
            @Override
            public void onResponse(Call<ArrayList<RemarkResponsePojo>> call, Response<ArrayList<RemarkResponsePojo>> response) {
                if (response.code() == 200){
                    if (response.body() != null) {
                        refreshLayout.setRefreshing(false);
                        txtNoDataFound.setVisibility(View.GONE);

                         remarkList = response.body();
                        Log.e("TAG", "response: "+response.body());
                        if (remarkList.isEmpty()){
                            txtNoDataFound.setVisibility(View.VISIBLE);
                        }else {
                            txtNoDataFound.setVisibility(View.GONE);
                            setAdapter(remarkList);
                        }


                    }
                } else if (response.code() == 500){
                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.GONE);
                    txtNoDataFound.setVisibility(View.VISIBLE);
                    Toast.makeText(context,""+response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RemarkResponsePojo>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                txtNoDataFound.setVisibility(View.VISIBLE);
                Log.e("TAG", "onFailure: ", t);
                Toast.makeText(context,""+t, Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
    }
}