package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.pojo.RemarkPojo;
import com.bigv.app.yocc.utils.AUtils;

import java.util.List;


/**
 * Created by mithun on 15/9/17.
 */

public class CallRemarkListAdapter extends ArrayAdapter<RemarkPojo> {

    private List<RemarkPojo> remarkPojoList;
    private Context context;
    private View view;
    private ViewHolder holder;

    public CallRemarkListAdapter(Context context, List<RemarkPojo> remarkPojoList) {
        super(context, android.R.layout.simple_list_item_1, remarkPojoList);
        this.context = context;
        this.remarkPojoList = remarkPojoList;
    }


    class ViewHolder {
        private TextView dateTextView;
        private TextView followUpTextView;
        private TextView remarkTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.call_remark_adapter, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.dateTextView = view.findViewById(R.id.call_remark_date_tv);
            viewHolder.followUpTextView = view.findViewById(R.id.call_remark_follow_up_tv);
            viewHolder.remarkTextView = view.findViewById(R.id.call_remark_remark_tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(remarkPojoList) && !remarkPojoList.isEmpty()) {
            RemarkPojo callTranscriptionPojo = remarkPojoList.get(position);

            if (!AUtils.isNullString(callTranscriptionPojo.getDate())) {
                holder.dateTextView.setText(callTranscriptionPojo.getDate());
            } else {
                holder.dateTextView.setText("");
            }
            if (!AUtils.isNullString(callTranscriptionPojo.getFollowUp())) {
                holder.followUpTextView.setText(callTranscriptionPojo.getFollowUp());
            } else {
                holder.followUpTextView.setText("");
            }
            if (!AUtils.isNullString(callTranscriptionPojo.getRemark())) {
                holder.remarkTextView.setText(callTranscriptionPojo.getRemark());
            } else {
                holder.remarkTextView.setText("");
            }
        }
        return view;
    }
}