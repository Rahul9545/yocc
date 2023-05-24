package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.pojo.MenuMasterPojo;
import com.bigv.app.yocc.pojo.RoutingPatternPojo;
import com.bigv.app.yocc.pojo.RoutingTypePojo;
import com.bigv.app.yocc.utils.AUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import quickutils.core.QuickUtils;


/**
 * Created by mithun on 15/9/17.
 */

public class MenuListAdapter extends ArrayAdapter<MenuMasterPojo> {

    private List<MenuMasterPojo> menuMasterPojoList;
    private Context context;
    private View view;
    private ViewHolder holder;
    private List<RoutingPatternPojo> routingPatternPojoList;
    private List<RoutingTypePojo> routingTypePojoList;

    public MenuListAdapter(Context context, List<MenuMasterPojo> menuMasterPojoList) {
        super(context, android.R.layout.simple_list_item_1, menuMasterPojoList);
        this.context = context;
        this.menuMasterPojoList = menuMasterPojoList;

        initRoutingList();
    }

    private void initRoutingList() {

        Type type = new TypeToken<List<RoutingPatternPojo>>() {
        }.getType();

        routingPatternPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.ROUTING_PATTERN_LIST, null), type);

        Type type2 = new TypeToken<List<RoutingTypePojo>>() {
        }.getType();

        routingTypePojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.ROUTING_TYPE_LIST, null), type2);
    }


    class ViewHolder {
        private TextView menuNameTextView;
        private TextView menuDescriptionTextView;
        private TextView routingPatternTextView;
        private TextView routingTypeTextView;
        private TextView dialTimeOutTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.menu_list_view_adapter, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.menuNameTextView = view.findViewById(R.id.menu_name_tv);
            viewHolder.menuDescriptionTextView = view.findViewById(R.id.menu_desc_tv);
            viewHolder.routingPatternTextView = view.findViewById(R.id.menu_routing_pattern_tv);
            viewHolder.routingTypeTextView = view.findViewById(R.id.menu_routing_type_tv);
            viewHolder.dialTimeOutTextView = view.findViewById(R.id.menu_dial_timeout_tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(menuMasterPojoList) && !menuMasterPojoList.isEmpty()) {
            MenuMasterPojo menuMasterPojo = menuMasterPojoList.get(position);

            if (!AUtils.isNullString(menuMasterPojo.getMenuName())) {
                holder.menuNameTextView.setText(menuMasterPojo.getMenuName());
            } else {
                holder.menuNameTextView.setText("");
            }
            if (!AUtils.isNullString(menuMasterPojo.getMenuDescription())) {
                holder.menuDescriptionTextView.setText(menuMasterPojo.getMenuDescription());
            } else {
                holder.menuDescriptionTextView.setText("");
            }
            if (!AUtils.isNullString(menuMasterPojo.getPatId())) {

                for (RoutingPatternPojo routingPatternPojo : routingPatternPojoList) {
                    if (routingPatternPojo.getId().equals(menuMasterPojo.getPatId())) {

                        holder.routingPatternTextView.setText(routingPatternPojo.getName());
                        break;
                    }
                }
            }
            if (!AUtils.isNullString(menuMasterPojo.getTypeId())) {
                for (RoutingTypePojo routingTypePojo : routingTypePojoList) {
                    if (routingTypePojo.getId().equals(menuMasterPojo.getTypeId())) {

                        holder.routingTypeTextView.setText(routingTypePojo.getName());
                        break;
                    }
                }
            }
            if (!AUtils.isNullString(menuMasterPojo.getDialTimeout())) {
                holder.dialTimeOutTextView.setText(menuMasterPojo.getDialTimeout());
            } else {
                holder.dialTimeOutTextView.setText("");
            }
        }
        return view;
    }
}