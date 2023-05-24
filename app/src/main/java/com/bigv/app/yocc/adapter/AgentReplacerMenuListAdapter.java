package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.bigv.app.yocc.pojo.MenuMasterPojo;
import com.bigv.app.yocc.utils.AUtils;

/**
 * Created by MiTHUN on 27/11/17.
 */

public class AgentReplacerMenuListAdapter extends ArrayAdapter<MenuMasterPojo> {

    private Context context;
    private List<MenuMasterPojo> menuMasterPojoList;

    public AgentReplacerMenuListAdapter(Context context, int textViewResourceId,
                                        List<MenuMasterPojo> menuMasterPojoList) {
        super(context, textViewResourceId, menuMasterPojoList);
        this.context = context;
        this.menuMasterPojoList = menuMasterPojoList;
    }

    @Override
    public int getCount() {
        return menuMasterPojoList.size();
    }

    @Override
    public MenuMasterPojo getItem(int position) {
        return menuMasterPojoList.get(position);
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
        if (!AUtils.isNullString(menuMasterPojoList.get(position).getMenuDescription())) {
            label.setText(menuMasterPojoList.get(position).getMenuDescription());
        } else {
            label.setText("(No Name)");
        }

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(20);
        label.setPadding(15, 10, 0, 10);
        if (!AUtils.isNullString(menuMasterPojoList.get(position).getMenuDescription())) {
            label.setText(menuMasterPojoList.get(position).getMenuDescription());
        } else {
            label.setText("(No Name)");
        }

        return label;
    }
}