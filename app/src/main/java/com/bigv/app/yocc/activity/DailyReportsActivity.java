package com.bigv.app.yocc.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.ReportCallDetailsPojo;
import com.bigv.app.yocc.pojo.ReportCallListAndSummeryPojo;
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

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import quickutils.core.QuickUtils;

/**
 * Created by mithun on 28/10/17.
 */

public class DailyReportsActivity extends MyBaseActivity {

    private TextView displayDateTextView;
    private LinearLayout listLinearLayout;
    private Button filterButton;
    private Button searchButton;
    private SlidingDrawer slidingDrawer;
    private EditText selecetedDateEditText;
    private List<ReportCallDetailsPojo> reportCallDetailsPojoList = null;
    private ReportCallListAndSummeryPojo reportCallListAndSummeryPojo = null;
    private String selectedDate = null;
    private String displayDate = "";

    @Override
    protected void genrateId() {

        setContentView(R.layout.daily_report_activty);
        //setToolbarTitle("DAY WISE REPORT");
        setToolbarTitle(getResources().getString(R.string.str_title_day_wise_report));

        displayDateTextView = findViewById(R.id.display_date_tv);
        listLinearLayout = findViewById(R.id.list_layout_ll);
        filterButton = findViewById(R.id.report_filter_btn);
        searchButton = findViewById(R.id.report_search_btn);
        slidingDrawer = findViewById(R.id.report_sliding_drawer);
        selecetedDateEditText = findViewById(R.id.start_date_time_et);
    }

    @Override
    protected void registerEvents() {

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filterButton.getText().equals("Filter Calls")) {
                    slidingDrawer.open();
                } else if (filterButton.getText().equals("Close")) {
                    slidingDrawer.close();
                }
            }
        });
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {

                filterButton.setText("Close");
                selecetedDateEditText.setText("00/00/0000");
                selectedDate = null;
            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {

                filterButton.setText("Filter Calls");
            }
        });
        selecetedDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateTimeSelecterOnClick();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonOnClick();
            }
        });

    }

    private void searchButtonOnClick() {

        if (validateFilelds()) {
            if (slidingDrawer.isOpened()) {
                slidingDrawer.close();
            }

            new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {
                public boolean result = false;

                @Override
                public void doInBackgroundOpration(SyncServer syncServer) {

                    result = syncServer.getReportCallDetailsList(selectedDate);
                }

                @Override
                public void onFinished() {

                    if (result) {
                        setDataToList();
                        displayDateTextView.setText("Date = " + displayDate);
                    } else {
                        Toasty.error(DailyReportsActivity.this, "" + getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();
        }
    }

    private boolean validateFilelds() {

        if (AUtils.isNullString(selectedDate)) {

            Toasty.warning(this, "Please select date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void dateTimeSelecterOnClick() {

        Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat serverFormat = new SimpleDateFormat(AUtils.SERVER_DATE_FORMATE);
        final SimpleDateFormat mobileFormat = new SimpleDateFormat(AUtils.MOBILE_DATE_FORMATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                // date show on selected textview
                selecetedDateEditText.setText(mobileFormat.format(newDate.getTime()));
                // date to display ontop
                displayDate = mobileFormat.format(newDate.getTime());
                // date for server call
                selectedDate = serverFormat.format(newDate.getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void initData() {

        initDisplayDate();
        setDataToList();
        searchButtonOnClick();
    }

    private void initDisplayDate() {

        displayDateTextView.setText("Today  " + AUtils.getTodaysDate(AUtils.MOBILE_DATE_FORMATE));
        selectedDate = (AUtils.getTodaysDate(AUtils.SERVER_DATE_FORMATE));
    }

    private void setDataToList() {

        listLinearLayout.removeAllViews();

        listLinearLayout.addView(getTopView());
        listLinearLayout.addView(addPersentagePieGraph());

        if (!AUtils.isNullString(QuickUtils.prefs.getString(AUtils.DAY_WISE_REPORT_CALL_DETAILS, null))) {

            Type type = new TypeToken<ReportCallListAndSummeryPojo>() {
            }.getType();

            reportCallListAndSummeryPojo = new Gson().fromJson(
                    QuickUtils.prefs.getString(AUtils.DAY_WISE_REPORT_CALL_DETAILS, null), type);

            reportCallDetailsPojoList = reportCallListAndSummeryPojo.getCallDetails();

            for (ReportCallDetailsPojo reportCallDetailsPojo : reportCallDetailsPojoList) {

                listLinearLayout.addView(getListViewItem(reportCallDetailsPojo));
            }
            Toasty.success(this, "" + getString(R.string.dataUpdated), Toast.LENGTH_SHORT).show();
        }
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
