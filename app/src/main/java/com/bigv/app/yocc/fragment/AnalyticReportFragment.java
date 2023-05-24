package com.bigv.app.yocc.fragment;

/**
 * Created by MiTHUN on 12/9/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.activity.DailyReportsActivity;
import com.bigv.app.yocc.activity.MonthlyReportsActivity;
import com.bigv.app.yocc.activity.WeeklyReportsActivity;
import com.bigv.app.yocc.adapter.AnalyticReportAdapter;

import java.util.ArrayList;
import java.util.List;


public class AnalyticReportFragment extends Fragment {

    private static final String TAG = "AnalyticReportFragment";
    private View view;
    private Context context;
    private ListView analyticReportListView;

    public static Fragment newInstance() {
        AnalyticReportFragment fragment = new AnalyticReportFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.analytic_report_fragment, container, false);
        context = container.getContext();
        initComponants();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initComponants() {
        genrateId();
        registerEvents();
        initAdapter();
    }

    private void initAdapter() {

        List<String> analyticList = new ArrayList<>();
        analyticList.add("Daywise Reports");
        analyticList.add("Agent Reports");
        analyticList.add("Call Reports");

        AnalyticReportAdapter analyticReportAdapter = new AnalyticReportAdapter(context, analyticList);
        analyticReportListView.setAdapter(analyticReportAdapter);
    }

    private void genrateId() {

        analyticReportListView = view.findViewById(R.id.analytic_report_lv);
    }

    private void registerEvents() {

        analyticReportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                analyticReportListViewOnClick(position, view);
            }
        });
    }

    private void analyticReportListViewOnClick(int position, View view) {

        switch (position) {
            case 0:
                //https://stackoverflow.com/questions/15454995/popupmenu-with-icons
                showDaywiseReportsList(view);
                break;
            case 1:
                Toast.makeText(context, "Coming soon...", Toast.LENGTH_SHORT).show();
//                showAgentReportsList(view);
                break;
            case 2:
                Toast.makeText(context, "Coming soon...", Toast.LENGTH_SHORT).show();
//                showCallReportsList(view);
                break;
        }
    }

    private void showDaywiseReportsList(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.daywise_reports, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_daily:
                        startActivity(new Intent(context, DailyReportsActivity.class));
                        return true;
                    case R.id.menu_weekly:
                        startActivity(new Intent(context, WeeklyReportsActivity.class));
                        return true;
                    case R.id.menu_monthly:
                        startActivity(new Intent(context, MonthlyReportsActivity.class));
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void showAgentReportsList(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.agent_reports, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.agent_dashboard:
                        Toast.makeText(context, "Agent Dashboard", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.agent_call_details:
                        Toast.makeText(context, "Agent Call Details", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.agent_call_summery:
                        Toast.makeText(context, "Agent Call Summary", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void showCallReportsList(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.call_reports, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.call_transaction:
                        Toast.makeText(context, "Call Transaction", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.call_priority:
                        Toast.makeText(context, "Call Priority", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.call_details:
                        Toast.makeText(context, "Call Details", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.call_remark:
                        Toast.makeText(context, "Call Remark", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }
}