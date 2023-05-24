package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.pojo.LiveCallPojo;
import com.bigv.app.yocc.utils.AUtils;

import java.util.List;


/**
 * Created by mithun on 15/9/17.
 */

public class LiveCallAdapter extends ArrayAdapter<LiveCallPojo> {

    private List<LiveCallPojo> liveCallPojoList;
    private Context context;
    private View view;
    private ViewHolder holder;

    public LiveCallAdapter(Context context, List<LiveCallPojo> liveCallPojoList) {
        super(context, android.R.layout.simple_list_item_1, liveCallPojoList);
        this.context = context;
        this.liveCallPojoList = liveCallPojoList;
    }


    class ViewHolder {
        private TextView callerNameNumberTextView;
        private TextView agentNameNumberTextView;
        private TextView menuTextView;
        private TextView startTimeTextView;
        private TextView durationTextView;
        private TextView callStstusTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.live_call_adapter, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.callerNameNumberTextView = view.findViewById(R.id.live_calls_caller_details_tv);
            viewHolder.agentNameNumberTextView = view.findViewById(R.id.live_calls_agent_detail_tv);
            viewHolder.menuTextView = view.findViewById(R.id.live_calls_menu_tv);
            viewHolder.startTimeTextView = view.findViewById(R.id.live_calls_start_time_tv);
            viewHolder.durationTextView = view.findViewById(R.id.live_calls_duration_tv);
            viewHolder.callStstusTextView = view.findViewById(R.id.live_calls_call_status_tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(liveCallPojoList) && !liveCallPojoList.isEmpty()) {
            LiveCallPojo liveCallPojo = liveCallPojoList.get(position);

            if (!AUtils.isNullString(liveCallPojo.getCallerName())) {
                holder.callerNameNumberTextView.setText(liveCallPojo.getCallerName() + " - ");
            }
            if (!AUtils.isNullString(liveCallPojo.getCallerNumber())) {
                if (!AUtils.isNullString(liveCallPojo.getCallerName())) {

                    holder.callerNameNumberTextView.setText(holder.callerNameNumberTextView.getText() + liveCallPojo.getCallerNumber());
                } else {

                    holder.callerNameNumberTextView.setText(liveCallPojo.getCallerNumber());
                }
            }
            if (!AUtils.isNullString(liveCallPojo.getOperatorNumber())) {
                holder.agentNameNumberTextView.setText(liveCallPojo.getOperatorNumber());
            }
            if (!AUtils.isNullString(liveCallPojo.getOperatorName())) {
                if (!AUtils.isNullString(liveCallPojo.getOperatorNumber())) {

                    holder.agentNameNumberTextView.setText(holder.agentNameNumberTextView.getText() + " (" + liveCallPojo.getOperatorName() + ")");
                } else {

                    holder.agentNameNumberTextView.setText(" (" + liveCallPojo.getOperatorName() + ")");
                }
            }
            if (!AUtils.isNullString(liveCallPojo.getMenuName())) {
                holder.menuTextView.setText(liveCallPojo.getMenuName());
            }
            if (!AUtils.isNullString(liveCallPojo.getMenuNumber())) {
                if (!AUtils.isNullString(liveCallPojo.getMenuName())) {

                    holder.menuTextView.setText(holder.menuTextView.getText() + " (" + liveCallPojo.getMenuNumber() + ")");
                } else {

                    holder.menuTextView.setText("(" + liveCallPojo.getMenuNumber() + ")");
                }
            }
            if (!AUtils.isNullString(liveCallPojo.getStartTime())) {
                holder.startTimeTextView.setText(liveCallPojo.getStartTime());
            }
            if (!AUtils.isNullString(liveCallPojo.getCallDuration())) {
                holder.durationTextView.setText(liveCallPojo.getCallDuration());
            }
            if (!AUtils.isNullString(liveCallPojo.getStatus())) {
                holder.callStstusTextView.setText(liveCallPojo.getStatus());
            }
        }
        return view;
    }
}