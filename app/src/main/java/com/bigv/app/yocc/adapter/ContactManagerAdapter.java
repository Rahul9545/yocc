package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.pojo.AddressBookPojo;
import com.bigv.app.yocc.pojo.GroupAddressBookPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import quickutils.core.QuickUtils;


/**
 * Created by mithun on 15/9/17.
 */

public class ContactManagerAdapter extends ArrayAdapter<AddressBookPojo> {

    private static final String TAG = "ContactManagerAdapter";
    private List<AddressBookPojo> addressBookList;
    private Context context;
    private View view;
    private ViewHolder holder;
    private List<GroupAddressBookPojo> groupAddressBookPojoList;

    public ContactManagerAdapter(Context context, List<AddressBookPojo> addressBookList) {
        super(context, android.R.layout.simple_list_item_1, addressBookList);
        this.context = context;
        this.addressBookList = addressBookList;

        initGroupList();
    }

    private void initGroupList() {

        Type type = new TypeToken<List<GroupAddressBookPojo>>() {
        }.getType();

        groupAddressBookPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.GROUP_ADDRESS_BOOK_LIST, null), type);
    }

    class ViewHolder {
        private TextView callerNameTextView;
        private TextView callerNumberTextView;
        private TextView emailTextView;
        private TextView addressTextView;
        private TextView groupNameTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.address_book_adapter, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.callerNameTextView = view.findViewById(R.id.address_book_caller_name_tv);
            viewHolder.callerNumberTextView = view.findViewById(R.id.address_book_caller_number_tv);
            viewHolder.emailTextView = view.findViewById(R.id.address_book_caller_email_lv);
            viewHolder.addressTextView = view.findViewById(R.id.address_book_caller_address_lv);
            viewHolder.groupNameTextView = view.findViewById(R.id.address_book_group_name_tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(addressBookList) && !addressBookList.isEmpty()) {
            AddressBookPojo addressBookPojo = addressBookList.get(position);

            if (!AUtils.isNullString(addressBookPojo.getLocalName())) {
                holder.callerNameTextView.setText(addressBookPojo.getLocalName());
            } else {
                holder.callerNameTextView.setText("");
            }
            if (!AUtils.isNullString(addressBookPojo.getDepartmentId())) {

                if (addressBookPojo.getDepartmentId().equals("0")) {


                    holder.groupNameTextView.setText("");
                } else {
                    for (GroupAddressBookPojo groupAddressBookPojo : groupAddressBookPojoList) {

                        if (addressBookPojo.getDepartmentId().equals("" + groupAddressBookPojo.getDepartmentId())) {
                            holder.groupNameTextView.setText(groupAddressBookPojo.getDepartmentName());
                            break;
                        } else {
                            holder.groupNameTextView.setText("");
                        }
                    }
                }
            } else {
                holder.groupNameTextView.setText("");
            }
            if (!AUtils.isNullString(addressBookPojo.getCallerId())) {
                holder.callerNumberTextView.setText(addressBookPojo.getCallerId());
            } else {
                holder.callerNumberTextView.setText("");
            }
            if (!AUtils.isNullString(addressBookPojo.getEmail())) {
                holder.emailTextView.setText(addressBookPojo.getEmail());
            } else {
                holder.emailTextView.setText("");
            }
            if (!AUtils.isNullString(addressBookPojo.getAddress())) {
                holder.addressTextView.setText(addressBookPojo.getAddress());
            } else {
                holder.addressTextView.setText("");
            }
        }
        return view;
    }
}