package com.bigv.app.yocc.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.ReportCallDetailsPojo;
import com.bigv.app.yocc.pojo.ReportCallListAndSummeryPojo;
import com.bigv.app.yocc.pojo.WeekWiseCallPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.bigv.app.yocc.utils.MyBaseActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.componants.Toasty;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import quickutils.core.QuickUtils;

/**
 * Created by mithun on 28/10/17.
 */

public class WeeklyReportsActivity extends MyBaseActivity {

    private TextView displayDateTextView;
    private LinearLayout listLinearLayout;
    private EditText selecetedDateEditText;
    private List<ReportCallDetailsPojo> reportCallDetailsPojoList = null;
    private ReportCallListAndSummeryPojo reportCallListAndSummeryPojo = null;

    @Override
    protected void genrateId() {

        setContentView(R.layout.weekly_report_activty);
        //setToolbarTitle("WEEK WISE REPORT");
        setToolbarTitle(getResources().getString(R.string.str_title_week_wise_report));

        displayDateTextView = findViewById(R.id.display_date_tv);
        listLinearLayout = findViewById(R.id.list_layout_ll);
        selecetedDateEditText = findViewById(R.id.start_date_time_et);
    }

    @Override
    protected void registerEvents() {

    }

    private void getDataFromServer() {

        new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {
            public boolean result = false;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                result = syncServer.getReportWeeklyCallDetailsList();
                syncServer.getWeekWiseCall();
            }

            @Override
            public void onFinished() {

                if (result) {
                    setDataToList();
                    displayDateTextView.setText("Date = ");
                } else {
                    Toasty.error(WeeklyReportsActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

    @Override
    protected void initData() {

        getDataFromServer();
    }

    private void setDataToList() {

        listLinearLayout.removeAllViews();

        if (!AUtils.isNullString(QuickUtils.prefs.getString(AUtils.DAY_WISE_REPORT_CALL_DETAILS, null))) {

            Type type = new TypeToken<ReportCallListAndSummeryPojo>() {
            }.getType();

            listLinearLayout.addView(getTopView());
            listLinearLayout.addView(addPersentagePieGraph());
            listLinearLayout.addView(addBarChartGraph());

            reportCallListAndSummeryPojo = new Gson().fromJson(
                    QuickUtils.prefs.getString(AUtils.DAY_WISE_REPORT_CALL_DETAILS, null), type);

            reportCallDetailsPojoList = reportCallListAndSummeryPojo.getCallDetails();

            for (ReportCallDetailsPojo reportCallDetailsPojo : reportCallDetailsPojoList) {

                listLinearLayout.addView(getListViewItem(reportCallDetailsPojo));
            }
            Toasty.success(this, "" + getString(R.string.dataUpdated), Toast.LENGTH_SHORT).show();
        } else {
            Toasty.success(this, "" + getString(R.string.noData), Toast.LENGTH_SHORT).show();
        }
    }

    private View addBarChartGraph() {

        View view = getLayoutInflater().inflate(R.layout.bar_chart, null);

        Type type = new TypeToken<List<WeekWiseCallPojo>>() {
        }.getType();

        List<WeekWiseCallPojo> weekWiseCallPojoList = new Gson().fromJson(
                QuickUtils.prefs.getString(AUtils.WEEK_WISE_CALL_LIST, null), type);

        if (!AUtils.isNull(weekWiseCallPojoList) && !weekWiseCallPojoList.isEmpty()) {

            BarChart barChart = view.findViewById(R.id.bar_chart_graph);
            TextView title = view.findViewById(R.id.bar_chart_title);

            title.setText("Week Call Summary");

            for (WeekWiseCallPojo weekWiseCallPojo : weekWiseCallPojoList) {

                if (!AUtils.isNullString(weekWiseCallPojo.getTotalCall())) {

                    String duration = weekWiseCallPojo.getTotalCall().replace(":", ".");

                    barChart.addBar(new BarModel("" + weekWiseCallPojo.getDay(),
                            Float.parseFloat(duration), 0xFF4989c7));
                }
            }
            barChart.startAnimation();
        }
        return view;
    }

    private View addPersentagePieGraph() {

        View view = getLayoutInflater().inflate(R.layout.pie_chart, null);

        if (!AUtils.isNull(reportCallListAndSummeryPojo)) {

            PieChart pieChart = view.findViewById(R.id.piechart);
            pieChart.setUsePercentValues(true);

            // IMPORTANT: In a PieChart, no values (Entry) should have the same
            // xIndex (even if from different DataSets), since no values can be
            // drawn above each other.
            ArrayList<Entry> yvalues = new ArrayList<Entry>();

            if (!AUtils.isNullString(reportCallListAndSummeryPojo.getAnswered())) {

                yvalues.add(new Entry(Integer.parseInt(reportCallListAndSummeryPojo.getAnswered()), 0));
            }
            if (!AUtils.isNullString(reportCallListAndSummeryPojo.getUnanswered())) {

                yvalues.add(new Entry(Integer.parseInt(reportCallListAndSummeryPojo.getUnanswered()), 1));
            }
            if (!AUtils.isNullString(reportCallListAndSummeryPojo.getUnoptedCall())) {

                yvalues.add(new Entry(Integer.parseInt(reportCallListAndSummeryPojo.getUnoptedCall()), 2));
            }

            PieDataSet dataSet = new PieDataSet(yvalues, "");

            ArrayList<String> xVals = new ArrayList<String>();

            xVals.add("Answered");
            xVals.add("Unanswered");
            xVals.add("Unopted");
            xVals.add("Unopted");
            xVals.add("Unopted");
            xVals.add("Unopted");

            PieData data = new PieData(xVals, dataSet);
            // In Percentage term
            data.setValueFormatter(new PercentFormatter());
            // Default value
            // data.setValueFormatter(new DefaultValueFormatter(0));
            pieChart.setData(data);
            pieChart.setDescription("Call Percentage");
            pieChart.setDescriptionTextSize(15f);

            //Disable Hole in the Pie Chart
            pieChart.setDrawHoleEnabled(false);

            // Color
            int[] VORDIPLOM_COLORS = {
                    Color.rgb(126, 200, 132),
                    Color.rgb(220, 124, 122),
                    Color.rgb(237, 191, 122)
            };

            dataSet.setColors(VORDIPLOM_COLORS);
            data.setValueTextSize(13f);
            data.setValueTextColor(Color.DKGRAY);
            // pieChart.setOnChartValueSelectedListener(this);

            pieChart.setRotationEnabled(false);
            pieChart.animateXY(1400, 1400);
        }
        return view;
    }

    private View getTopView() {

        View view = getLayoutInflater().inflate(R.layout.call_details_top_view, null);

        if (!AUtils.isNull(reportCallListAndSummeryPojo)) {

            TextView totalCallsTextView = view.findViewById(R.id.count_total_call);
            TextView answeredCallsTextView = view.findViewById(R.id.count_answer_call);
            TextView unansweredCallsTextView = view.findViewById(R.id.count_unanswered_call);
            TextView unoptedCallsTextView = view.findViewById(R.id.count_unopted_call);

            totalCallsTextView.setText(reportCallListAndSummeryPojo.getTotalCall());
            answeredCallsTextView.setText(reportCallListAndSummeryPojo.getAnswered());
            unansweredCallsTextView.setText(reportCallListAndSummeryPojo.getUnanswered());
            unoptedCallsTextView.setText(reportCallListAndSummeryPojo.getUnoptedCall());
        } else {
            TextView totalCallsTextView = view.findViewById(R.id.count_total_call);
            TextView answeredCallsTextView = view.findViewById(R.id.count_answer_call);
            TextView unansweredCallsTextView = view.findViewById(R.id.count_unanswered_call);
            TextView unoptedCallsTextView = view.findViewById(R.id.count_unopted_call);

            totalCallsTextView.setText("0");
            answeredCallsTextView.setText("0");
            unansweredCallsTextView.setText("0");
            unoptedCallsTextView.setText("0");
        }
        return view;
    }


    private View getListViewItem(final ReportCallDetailsPojo callDetailsPojo) {

        final View view = getLayoutInflater().inflate(R.layout.report_call_detail_list, null);

        TextView nameTextView = view.findViewById(R.id.report_caller_name_number);
        TextView agentNameNumberTextView = view.findViewById(R.id.report_agent_name_number);
        TextView dateTimeTextView = view.findViewById(R.id.report_date);
        TextView callTimeTextView = view.findViewById(R.id.report_start_end_time);
        TextView menuNameNumberTextView = view.findViewById(R.id.report_menu_details);
        TextView callDurationTextView = view.findViewById(R.id.report_call_duration);
        ImageView statusImageView = view.findViewById(R.id.report_icon_call_status);

        if (!AUtils.isNullString(callDetailsPojo.getCallerDetails())) {
            nameTextView.setText(callDetailsPojo.getCallerDetails());
        }
        if (!AUtils.isNullString(callDetailsPojo.getOperatorDetails())) {
            agentNameNumberTextView.setText(callDetailsPojo.getOperatorDetails());
        }
        if (!AUtils.isNullString(callDetailsPojo.getDate())) {
            dateTimeTextView.setText(callDetailsPojo.getDate() + ", ");
        }
        if (!AUtils.isNullString(callDetailsPojo.getStartTime())) {
            callTimeTextView.setText(callDetailsPojo.getStartTime());
        }
        if (!AUtils.isNullString(callDetailsPojo.getEndTime())) {
            if (!AUtils.isNullString(callDetailsPojo.getStartTime())) {
                callTimeTextView.setText(callDetailsPojo.getStartTime() + " - " + callDetailsPojo.getEndTime());
            } else {
                callTimeTextView.setText(callDetailsPojo.getEndTime());
            }
        }
        if (!AUtils.isNullString(callDetailsPojo.getMenuDetails())) {
            menuNameNumberTextView.setText(callDetailsPojo.getMenuDetails());
        }
        if (!AUtils.isNullString(callDetailsPojo.getCallDuration())) {
            callDurationTextView.setText(callDetailsPojo.getCallDuration());
        }
        if (!AUtils.isNullString(callDetailsPojo.getStatus())) {
            switch (callDetailsPojo.getStatus()) {
                case "A":
                    statusImageView.setImageResource(R.drawable.ic_call_attended);
                    break;
                case "UA":
                    statusImageView.setImageResource(R.drawable.ic_missed_calls);
                    break;
                case "UO":
                    statusImageView.setImageResource(R.drawable.ic_unopted_icon);
                    break;
            }
        }
        return view;
    }
}
