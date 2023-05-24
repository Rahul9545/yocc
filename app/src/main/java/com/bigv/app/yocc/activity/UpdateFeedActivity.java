package com.bigv.app.yocc.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.adapter.RemarkCallerListAdapter;
import com.bigv.app.yocc.pojo.RemarkResponsePojo;
import com.bigv.app.yocc.pojo.RemarkUpdatePojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.RetrofitClient;
import com.bigv.app.yocc.webservices.RemarkApi;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.componants.MyDateTimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import quickutils.core.QuickUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFeedActivity extends BaseActivity {
    private Context context;
    private Toolbar toolbar;
    private TextView txtNoCallerRemarkFound;
    private TextView txtBtnFollowUp;
    private EditText edtFollowupDate;
    private Button btnUpdate;
    private AutoCompleteTextView autoRemark;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ArrayList<RemarkResponsePojo> remarkCallerList;
    private RemarkUpdatePojo remarkUpdatePojo;
    private LinearLayoutManager layoutManager;
    private RemarkCallerListAdapter adapter;
    private String key, userId, callerNum;
    private String followUpDateTime;
    private SimpleDateFormat formatMobile;
    private SimpleDateFormat formatServer;
    private String remarkStatus;

    @Override
    protected void genrateId() {
        setContentView(R.layout.activity_update_feed);
        context = this;
        remarkCallerList = new ArrayList<>();
        remarkUpdatePojo = new RemarkUpdatePojo();
        refreshLayout = findViewById(R.id.swipe_caller);
        recyclerView = findViewById(R.id.recycler_caller);
        txtBtnFollowUp = findViewById(R.id.txt_btn_followup);
        edtFollowupDate = findViewById(R.id.edt_date_follow_up);
        autoRemark = findViewById(R.id.txt_auto_edit_remark);
        btnUpdate = findViewById(R.id.btn_update);
        layoutManager = new LinearLayoutManager(context);
        txtNoCallerRemarkFound = findViewById(R.id.txt_no_remark_caller);

        formatServer = new SimpleDateFormat(AUtils.SERVER_DATE_time);
        formatMobile = new SimpleDateFormat(AUtils.MOBILE_DATE_time);

        callerNum = getIntent().getStringExtra("Caller_num");
        Log.i("TAG", "get update caller num: "+callerNum);

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

        toolbar.setTitle(getResources().getString(R.string.str_title_remark_update));
    }

    @Override
    protected void registerEvents() {
        txtBtnFollowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followupDateOnClick();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()){
                    postUpdatedRemark();
                }
            }
        });


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                if (AUtils.isNetworkConnected(context)){
                    txtNoCallerRemarkFound.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    getRemarkCallerList();
                }else {
                    txtNoCallerRemarkFound.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    protected void initData() {
        if (AUtils.isNetworkConnected(context)){
            txtNoCallerRemarkFound.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            getRemarkCallerList();
        }else {
            txtNoCallerRemarkFound.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        }
    }

    private void setAdapter(ArrayList<RemarkResponsePojo> remarkCallerList) {
        refreshLayout.setRefreshing(false);
        txtNoCallerRemarkFound.setVisibility(View.GONE);
        adapter = new RemarkCallerListAdapter(context, remarkCallerList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void getRemarkCallerList() {
        refreshLayout.setRefreshing(true);
        key = QuickUtils.prefs.getString(AUtils.KEY, "");
        userId = QuickUtils.prefs.getString(AUtils.AGENT_ID, "0");
        callerNum = getIntent().getStringExtra("Caller_num");

        RemarkApi remarkApiWebService = RetrofitClient.createService(RemarkApi.class, AUtils.SERVER_URL);
        Call<ArrayList<RemarkResponsePojo>> getCallerRemark = remarkApiWebService.remarkCallerList(key,userId,callerNum);

        getCallerRemark.enqueue(new Callback<ArrayList<RemarkResponsePojo>>() {
            @Override
            public void onResponse(Call<ArrayList<RemarkResponsePojo>> call, Response<ArrayList<RemarkResponsePojo>> response) {
                if (response.code() == 200){
                    txtNoCallerRemarkFound.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                    if (response.body() != null) {
                        remarkCallerList = response.body();
                        Log.e("TAG", "response: "+remarkCallerList);
                        if (remarkCallerList.isEmpty()){
                            txtNoCallerRemarkFound.setVisibility(View.VISIBLE);
                        }
                        setAdapter(remarkCallerList);

                    }
                } else if (response.code() == 500){
                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.GONE);
                    txtNoCallerRemarkFound.setVisibility(View.VISIBLE);
                    Toast.makeText(context,""+response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RemarkResponsePojo>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                txtNoCallerRemarkFound.setVisibility(View.VISIBLE);
                Log.e("TAG", "onFailure: ", t);
                Toast.makeText(context,""+t, Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void followupDateOnClick() {

        MyDateTimePicker dateAndTimePicker = new MyDateTimePicker(this,
                new MyDateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog,
                                      Calendar calendarSelected,
                                      Date dateSelected, int year,
                                      String monthFullName,
                                      String monthShortName, int monthNumber,
                                      int date, String weekDayFullName,
                                      String weekDayShortName, int hour24,
                                      int hour12, int min, int sec, String AM_PM) {

                        setDateSelected(dateSelected);
                    }

                    @Override
                    public void onCancel() {
                    }
                });

        dateAndTimePicker.set24HourFormat(false);
        dateAndTimePicker.showDialog();
    }

    private void setDateSelected(Date dateSelected) {

        followUpDateTime = formatServer.format(dateSelected);
        Log.i("TAG", "setDateSelected: "+followUpDateTime);
        edtFollowupDate.setVisibility(View.VISIBLE);
        edtFollowupDate.setText(formatMobile.format(dateSelected));
    }

    private void postUpdatedRemark(){
        key = QuickUtils.prefs.getString(AUtils.KEY, "");
        userId = QuickUtils.prefs.getString(AUtils.AGENT_ID, "0");
        callerNum = getIntent().getStringExtra("Caller_num");
        remarkStatus = autoRemark.getText().toString();
        String good = AUtils.stripNonValidXMLCharacters(remarkStatus);

        RemarkApi remarkApiWebService = RetrofitClient.createService(RemarkApi.class, AUtils.SERVER_URL);
        Call<RemarkUpdatePojo> postRemarkUpdate = remarkApiWebService.callerRemarkInsert(key,userId,callerNum, good,followUpDateTime);

        postRemarkUpdate.enqueue(new Callback<RemarkUpdatePojo>() {
            @Override
            public void onResponse(Call<RemarkUpdatePojo> call, Response<RemarkUpdatePojo> response) {
                if (response.code() == 200){
                    if (response.body() != null) {
                        remarkUpdatePojo = response.body();
                        if (remarkUpdatePojo.getStatus().equals("success")){
                            Toast.makeText(context, ""+remarkUpdatePojo.getMessage(), Toast.LENGTH_SHORT).show();
                            btnUpdate.setVisibility(View.GONE);
                            autoRemark.setText("");
                            edtFollowupDate.setText("");
                            edtFollowupDate.setVisibility(View.GONE);

                        }else {
                            Toast.makeText(context,"Technical Problem", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else if (response.code() == 500){
                    Toast.makeText(context,"Technical Problem", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context,""+response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RemarkUpdatePojo> call, Throwable t) {
                Log.e("TAG", "onFailure: ", t);
                Toast.makeText(context,""+t, Toast.LENGTH_SHORT).show();
                Toast.makeText(context,"Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isValid(){
        if (edtFollowupDate.getText().toString().isEmpty()){
            Toast.makeText(context, "PLease select first follow up button", Toast.LENGTH_SHORT).show();
            return false;
        } else if (autoRemark.getText().toString().isEmpty()){
            Toast.makeText(context, "Please enter your feedback", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}