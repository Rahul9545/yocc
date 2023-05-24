package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.pojo.CallTranscriptionPojo;
import com.bigv.app.yocc.utils.AUtils;


/**
 * Created by mithun on 15/9/17.
 */

public class CallTranscriptionAdapter extends ArrayAdapter<CallTranscriptionPojo> {

    private List<CallTranscriptionPojo> callDetailsPojoList;
    private Context context;
    private View view;
    private ViewHolder holder;

    public CallTranscriptionAdapter(Context context, List<CallTranscriptionPojo> callDetailsPojoList) {
        super(context, android.R.layout.simple_list_item_1, callDetailsPojoList);
        this.context = context;
        this.callDetailsPojoList = callDetailsPojoList;
    }


    class ViewHolder {
        private TextView callNameNumberTextView;
        private TextView agentNameNumberTextView;
        private TextView menuTextView;
        private TextView startTimeTextView;
        private TextView endTimeTextView;
        private TextView durationTextView;
        private TextView statusTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.call_transcription_adapter, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.callNameNumberTextView = view.findViewById(R.id.call_transcription_adp_caller_name_number_tv);
            viewHolder.agentNameNumberTextView = view.findViewById(R.id.call_transcription_adp_agent_name_number_tv);
            viewHolder.menuTextView = view.findViewById(R.id.call_transcription_adp_menu_name_tv);
            viewHolder.startTimeTextView = view.findViewById(R.id.call_transcription_adp_start_time_tv);
            viewHolder.endTimeTextView = view.findViewById(R.id.call_transcription_adp_end_time_tv);
            viewHolder.durationTextView = view.findViewById(R.id.call_transcription_adp_duration_tv);
            viewHolder.statusTextView = view.findViewById(R.id.call_transcription_adp_status_tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(callDetailsPojoList) && !callDetailsPojoList.isEmpty()) {
            CallTranscriptionPojo callTranscriptionPojo = callDetailsPojoList.get(position);

            if (!AUtils.isNullString(callTranscriptionPojo.getCallerName())) {
                holder.callNameNumberTextView.setText(callTranscriptionPojo.getCallerName() + "\n");
            }
            if (!AUtils.isNullString(callTranscriptionPojo.getCallerNumber())) {
                if (!AUtils.isNullString(callTranscriptionPojo.getCallerName())) {
                    holder.callNameNumberTextView.setText(holder.callNameNumberTextView.getText() + callTranscriptionPojo.getCallerNumber());
                } else {
                    holder.callNameNumberTextView.setText(callTranscriptionPojo.getCallerNumber());
                }
            }
            if (!AUtils.isNullString(callTranscriptionPojo.getAgentName())) {
                holder.agentNameNumberTextView.setText(callTranscriptionPojo.getAgentName() + "\n");
            }
            if (!AUtils.isNullString(callTranscriptionPojo.getAgentNo())) {
                if (!AUtils.isNullString(callTranscriptionPojo.getAgentName())) {
                    holder.agentNameNumberTextView.setText(holder.agentNameNumberTextView.getText() + callTranscriptionPojo.getAgentNo());
                } else {
                    holder.agentNameNumberTextView.setText(callTranscriptionPojo.getAgentNo());
                }
            }
            if (!AUtils.isNullString(callTranscriptionPojo.getMenuName())) {
                holder.menuTextView.setText(callTranscriptionPojo.getMenuName());
            }
            if (!AUtils.isNullString(callTranscriptionPojo.getMenuDescription())) {
                if (!AUtils.isNullString(callTranscriptionPojo.getMenuName())) {
                    holder.menuTextView.setText(holder.menuTextView.getText() + " - " + callTranscriptionPojo.getMenuDescription());
                } else {
                    holder.menuTextView.setText(callTranscriptionPojo.getMenuDescription());
                }
            }
            if (!AUtils.isNullString(callTranscriptionPojo.getStartTime())) {
                holder.startTimeTextView.setText(callTranscriptionPojo.getStartTime());
            }
            if (!AUtils.isNullString(callTranscriptionPojo.getEndTime())) {
                holder.endTimeTextView.setText(callTranscriptionPojo.getEndTime());
            }
            if (!AUtils.isNullString(callTranscriptionPojo.getDuration())) {
                holder.durationTextView.setText(callTranscriptionPojo.getDuration());
            }
            if (!AUtils.isNull(callTranscriptionPojo.getStatus())) {
                holder.statusTextView.setText(callTranscriptionPojo.getStatus());
            }
        }
        return view;
    }
}