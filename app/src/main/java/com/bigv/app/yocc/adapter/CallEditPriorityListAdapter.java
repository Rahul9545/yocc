package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.bigv.app.yocc.pojo.CallPriorityPojo;

/**
 * Created by MiTHUN on 27/11/17.
 */

public class CallEditPriorityListAdapter extends ArrayAdapter<CallPriorityPojo> {

    private Context context;
    private List<CallPriorityPojo> callPriorityPojoList;

    public CallEditPriorityListAdapter(Context context, int textViewResourceId,
                                       List<CallPriorityPojo> callPriorityPojoList) {
        super(context, textViewResourceId, callPriorityPojoList);
        this.context = context;
        this.callPriorityPojoList = callPriorityPojoList;
    }

    @Override
    public int getCount() {
        return callPriorityPojoList.size();
    }

    @Override
    public CallPriorityPojo getItem(int position) {
        return callPriorityPojoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextSize(20);
        label.setTextColor(Color.BLACK);
        label.setText(callPriorityPojoList.get(position).getCallType());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(20);
        label.setPadding(15, 10, 0, 10);
        label.setText(callPriorityPojoList.get(position).getCallType());
        return label;
    }
}