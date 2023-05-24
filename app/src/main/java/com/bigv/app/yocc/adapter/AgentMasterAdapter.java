package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.pojo.AgentMasterPojo;
import com.bigv.app.yocc.utils.AUtils;


/**
 * Created by mithun on 15/9/17.
 */

public class AgentMasterAdapter extends ArrayAdapter<AgentMasterPojo> {

    private static final String TAG = "AgentMasterAdapter";
    private List<AgentMasterPojo> agentMasterPojoList;
    private Context context;
    private View view;
    private ViewHolder holder;

    public AgentMasterAdapter(Context context, List<AgentMasterPojo> agentMasterPojoList) {
        super(context, android.R.layout.simple_list_item_1, agentMasterPojoList);
        this.context = context;
        this.agentMasterPojoList = agentMasterPojoList;
    }


    class ViewHolder {
        private TextView agentNameTextView;
        private TextView agentNumberTextView;
        private TextView agentExtensionTextView;
        private TextView agentStatusTextView;
        private ImageView agentStstusImageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.agent_list_view_adapter, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.agentNameTextView = view.findViewById(R.id.agent_name_tv);
            viewHolder.agentNumberTextView = view.findViewById(R.id.agent_number_tv);
            viewHolder.agentExtensionTextView = view.findViewById(R.id.agent_extension);
            viewHolder.agentStatusTextView = view.findViewById(R.id.agent_status);
            viewHolder.agentStstusImageView = view.findViewById(R.id.agent_ststus_iv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(agentMasterPojoList) && !agentMasterPojoList.isEmpty()) {
            AgentMasterPojo agentMasterPojo = agentMasterPojoList.get(position);

            if (!AUtils.isNullString(agentMasterPojo.getOperatorName())) {
                holder.agentNameTextView.setText(agentMasterPojo.getOperatorName());
            } else {
                holder.agentNameTextView.setText("");
            }
            if (!AUtils.isNullString(agentMasterPojo.getOperatorNo())) {
                holder.agentNumberTextView.setText(agentMasterPojo.getOperatorNo());
            } else {
                holder.agentNumberTextView.setText("");
            }
            if (!AUtils.isNullString(agentMasterPojo.getExtension())) {
                holder.agentExtensionTextView.setText(agentMasterPojo.getExtension());
            } else {
                holder.agentExtensionTextView.setText("");
            }
            if (!AUtils.isNull(agentMasterPojo.isActive())) {

                if (agentMasterPojo.isActive()) {
                    holder.agentStatusTextView.setText("Active");
                    holder.agentStstusImageView.setImageResource(R.drawable.ic_active);
                } else {
                    holder.agentStatusTextView.setText("Inactive");
                    holder.agentStstusImageView.setImageResource(R.drawable.ic_inactive);
                }
            } else {
                holder.agentStatusTextView.setText("");
            }
        }
        return view;
    }
}