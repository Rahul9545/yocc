package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.bigv.app.yocc.pojo.AgentMasterPojo;
import com.bigv.app.yocc.utils.AUtils;

/**
 * Created by MiTHUN on 27/11/17.
 */

public class AgentReplacerAgentListAdapter extends ArrayAdapter<AgentMasterPojo> {

    private Context context;
    private List<AgentMasterPojo> agentMasterPojoList;

    public AgentReplacerAgentListAdapter(Context context, int textViewResourceId,
                                         List<AgentMasterPojo> agentMasterPojoList) {
        super(context, textViewResourceId, agentMasterPojoList);
        this.context = context;
        this.agentMasterPojoList = agentMasterPojoList;
    }

    @Override
    public int getCount() {
        return agentMasterPojoList.size();
    }

    @Override
    public AgentMasterPojo getItem(int position) {
        return agentMasterPojoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AgentMasterPojo agentMasterPojo = agentMasterPojoList.get(position);
        TextView label = new TextView(context);
        label.setTextSize(20);
        label.setTextColor(Color.BLACK);
        if (!AUtils.isNullString(agentMasterPojo.getOperatorNo())) {

            label.setText(agentMasterPojo.getOperatorName() + " (" + agentMasterPojo.getOperatorNo() + ")");
        } else {
            label.setText(agentMasterPojo.getOperatorName());
        }
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        AgentMasterPojo agentMasterPojo = agentMasterPojoList.get(position);

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(20);
        label.setPadding(15, 10, 0, 10);
        if (!AUtils.isNullString(agentMasterPojo.getOperatorNo())) {

            label.setText(agentMasterPojo.getOperatorName() + " (" + agentMasterPojo.getOperatorNo() + ")");
        } else {
            label.setText(agentMasterPojo.getOperatorName());
        }

        return label;
    }
}