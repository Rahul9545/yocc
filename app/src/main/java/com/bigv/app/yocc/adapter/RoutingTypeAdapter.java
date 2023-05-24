package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.bigv.app.yocc.pojo.RoutingTypePojo;

/**
 * Created by MiTHUN on 27/11/17.
 */

public class RoutingTypeAdapter extends ArrayAdapter<RoutingTypePojo> {

    private Context context;
    private List<RoutingTypePojo> routingTypePojoList;

    public RoutingTypeAdapter(Context context, int textViewResourceId,
                              List<RoutingTypePojo> routingTypePojoList) {
        super(context, textViewResourceId, routingTypePojoList);
        this.context = context;
        this.routingTypePojoList = routingTypePojoList;
    }

    @Override
    public int getCount() {
        return routingTypePojoList.size();
    }

    @Override
    public RoutingTypePojo getItem(int position) {
        return routingTypePojoList.get(position);
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
        label.setText(routingTypePojoList.get(position).getName());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(20);
        label.setPadding(15, 10, 0, 10);
        label.setText(routingTypePojoList.get(position).getName());

        return label;
    }
}