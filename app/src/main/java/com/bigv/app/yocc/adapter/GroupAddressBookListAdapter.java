package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bigv.app.yocc.pojo.GroupAddressBookPojo;
import com.bigv.app.yocc.utils.AUtils;

import java.util.List;

/**
 * Created by MiTHUN on 27/11/17.
 */

public class GroupAddressBookListAdapter extends ArrayAdapter<GroupAddressBookPojo> {

    private Context context;
    private List<GroupAddressBookPojo> groupAddressBookPojoList;

    public GroupAddressBookListAdapter(Context context, int textViewResourceId,
                                       List<GroupAddressBookPojo> menuMasterPojoList) {
        super(context, textViewResourceId, menuMasterPojoList);
        this.context = context;
        this.groupAddressBookPojoList = menuMasterPojoList;
    }

    @Override
    public int getCount() {
        return groupAddressBookPojoList.size();
    }

    @Override
    public GroupAddressBookPojo getItem(int position) {
        return groupAddressBookPojoList.get(position);
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
        if (!AUtils.isNullString(groupAddressBookPojoList.get(position).getDepartmentName())) {
            label.setText(groupAddressBookPojoList.get(position).getDepartmentName());
        }

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(20);
        label.setPadding(15, 10, 0, 10);
        if (!AUtils.isNullString(groupAddressBookPojoList.get(position).getDepartmentName())) {
            label.setText(groupAddressBookPojoList.get(position).getDepartmentName());
        }

        return label;
    }
}