package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.pojo.GroupAddressBookPojo;
import com.bigv.app.yocc.utils.AUtils;

import java.util.List;


/**
 * Created by mithun on 15/9/17.
 */

public class GroupAddressBookAdapter extends ArrayAdapter<GroupAddressBookPojo> {

    private static final String TAG = "GroupAddressBookAdapter";
    private List<GroupAddressBookPojo> groupAddressBookPojoList;
    private Context context;
    private View view;
    private ViewHolder holder;

    public GroupAddressBookAdapter(Context context, List<GroupAddressBookPojo> groupAddressBookPojoList) {
        super(context, android.R.layout.simple_list_item_1, groupAddressBookPojoList);
        this.context = context;
        this.groupAddressBookPojoList = groupAddressBookPojoList;

    }

    class ViewHolder {
        private TextView groupNameTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_address_book_adapter, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.groupNameTextView = view.findViewById(R.id.group_name_tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(groupAddressBookPojoList) && !groupAddressBookPojoList.isEmpty()) {
            GroupAddressBookPojo addressBookPojo = groupAddressBookPojoList.get(position);

            if (!AUtils.isNullString(addressBookPojo.getDepartmentName())) {
                holder.groupNameTextView.setText(addressBookPojo.getDepartmentName());
            } else {
                holder.groupNameTextView.setText("");
            }
        }
        return view;
    }
}