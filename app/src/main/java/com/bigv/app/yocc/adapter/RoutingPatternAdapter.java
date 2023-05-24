package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.bigv.app.yocc.pojo.RoutingPatternPojo;

/**
 * Created by MiTHUN on 27/11/17.
 */

public class RoutingPatternAdapter extends ArrayAdapter<RoutingPatternPojo> {

    private Context context;
    private List<RoutingPatternPojo> routingPatternPojoList;

    public RoutingPatternAdapter(Context context, int textViewResourceId,
                                 List<RoutingPatternPojo> routingPatternPojoList) {
        super(context, textViewResourceId, routingPatternPojoList);
        this.context = context;
        this.routingPatternPojoList = routingPatternPojoList;
    }

    @Override
    public int getCount() {
        return routingPatternPojoList.size();
    }

    @Override
    public RoutingPatternPojo getItem(int position) {
        return routingPatternPojoList.get(position);
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
        label.setText(routingPatternPojoList.get(position).getName());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(20);
        label.setPadding(15, 10, 0, 10);
        label.setText(routingPatternPojoList.get(position).getName());

        return label;
    }
}