package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.utils.AUtils;


/**
 * Created by mithun on 15/9/17.
 */

public class SettingsListAdapter extends ArrayAdapter<String> {

    private List<String> mainMenuPojoList;
    private Context context;
    private View view;
    private ViewHolder holder;
    Integer icons[] = {
            R.drawable.ic_menu_master,
            R.drawable.ic_agent_master,
            R.drawable.ic_agent_replacer,
            R.drawable.ic_change_password,
    };

    public SettingsListAdapter(Context context, List<String> mainMenuPojoList) {
        super(context, android.R.layout.simple_list_item_1, mainMenuPojoList);
        this.context = context;
        this.mainMenuPojoList = mainMenuPojoList;
    }


    class ViewHolder {
        private ImageView iconImageView;
        private TextView titleTextView;
        private LinearLayout outerLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.setting_list_view_adapter, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.iconImageView = view.findViewById(R.id.setting_adp_iv);
            viewHolder.titleTextView = view.findViewById(R.id.setting_adp_tv);
//            viewHolder.outerLayout = view.findViewById(R.id.call_details_adapterCV);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(mainMenuPojoList) && !mainMenuPojoList.isEmpty()) {
            String title = mainMenuPojoList.get(position);

            if (!AUtils.isNullString(title)) {
                holder.titleTextView.setText(title);
                holder.iconImageView.setImageResource(icons[position]);
            }
        }
        return view;
    }
}