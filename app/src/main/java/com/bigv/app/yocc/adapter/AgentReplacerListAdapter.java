package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.pojo.AgentReplacerPojo;
import com.bigv.app.yocc.pojo.LanguagePojo;
import com.bigv.app.yocc.pojo.MenuMasterPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import quickutils.core.QuickUtils;


/**
 * Created by mithun on 15/9/17.
 */

public class AgentReplacerListAdapter extends ArrayAdapter<AgentReplacerPojo> {

    private List<AgentReplacerPojo> agentReplacerPojoList;
    private Context context;
    private View view;
    private ViewHolder holder;
    private List<MenuMasterPojo> menuMasterPojoList;
    private List<LanguagePojo> languagePojoList;

    public AgentReplacerListAdapter(Context context, List<AgentReplacerPojo> agentReplacerPojoList) {
        super(context, android.R.layout.simple_list_item_1, agentReplacerPojoList);
        this.context = context;
        this.agentReplacerPojoList = agentReplacerPojoList;

        initMenuList();
        initLanguageList();
    }

    private void initMenuList() {

        Type type = new TypeToken<List<MenuMasterPojo>>() {
        }.getType();

        menuMasterPojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.MENU_MASTER_LIST, null), type);
    }

    private void initLanguageList() {

        Type type = new TypeToken<List<LanguagePojo>>() {
        }.getType();

        languagePojoList = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.LANGUAGE_LIST, null), type);
    }


    class ViewHolder {
        private TextView seqNoTextView;
        private TextView agentNameTextView;
        private TextView agentNumberTextView;
        private TextView agentMenuTextView;
        private TextView agentLanguageTextView;
        private TextView agentStstusTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.agent_replacer_list_view_adapter, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.seqNoTextView = view.findViewById(R.id.agent_replacer_seq_no_tv);
            viewHolder.agentNameTextView = view.findViewById(R.id.agent_replacer_agent_name_tv);
            viewHolder.agentNumberTextView = view.findViewById(R.id.agent_replacer_number_tv);
            viewHolder.agentMenuTextView = view.findViewById(R.id.agent_replacer_menu_tv);
            viewHolder.agentLanguageTextView = view.findViewById(R.id.agent_replacer_language_tv);
            viewHolder.agentStstusTextView = view.findViewById(R.id.agent_replacer_status_tv);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(agentReplacerPojoList) && !agentReplacerPojoList.isEmpty()) {
            AgentReplacerPojo agentReplacerPojo = agentReplacerPojoList.get(position);

            if (!AUtils.isNullString(agentReplacerPojo.getSequence())) {
                holder.seqNoTextView.setText(agentReplacerPojo.getSequence());
            } else {
                holder.seqNoTextView.setText("");
            }
            if (!AUtils.isNullString(agentReplacerPojo.getOperatorName())) {
                holder.agentNameTextView.setText(agentReplacerPojo.getOperatorName());
            } else {
                holder.agentNameTextView.setText("");
            }
            if (!AUtils.isNullString(agentReplacerPojo.getOperatorNo())) {
                holder.agentNumberTextView.setText(agentReplacerPojo.getOperatorNo());
            } else {
                holder.agentNumberTextView.setText("");
            }
            if (!AUtils.isNullString(agentReplacerPojo.getMenuName())) {

                if (!AUtils.isNull(menuMasterPojoList) && !menuMasterPojoList.isEmpty()) {

                    for (MenuMasterPojo menuMasterPojo : menuMasterPojoList) {

                        if (menuMasterPojo.getMenuName().equals(agentReplacerPojo.getMenuName())) {

                            holder.agentMenuTextView.setText(menuMasterPojo.getMenuDescription() + " (" + menuMasterPojo.getMenuName() + ")");
                            break;
                        }
                    }
                } else {
                    holder.agentMenuTextView.setText("");
                }
            } else {
                holder.agentMenuTextView.setText("");
            }
            if (!AUtils.isNullString(agentReplacerPojo.getLanguageKey())) {

                if (agentReplacerPojo.getLanguageKey().equals("0")) {
                    holder.agentLanguageTextView.setText("");
                } else {
                    if (!AUtils.isNull(languagePojoList) && !languagePojoList.isEmpty()) {
                        for (LanguagePojo languagePojo : languagePojoList) {

                            if (languagePojo.getId().equals(agentReplacerPojo.getLanguageKey())) {

                                holder.agentLanguageTextView.setText(languagePojo.getLanguage());
                                break;
                            }
                        }
                    } else {
                        holder.agentLanguageTextView.setText("");
                    }
                }
            } else {
                holder.agentLanguageTextView.setText("");
            }
            if (!AUtils.isNull(agentReplacerPojo.isActive())) {
                if (agentReplacerPojo.isActive()) {
                    holder.agentStstusTextView.setText("Active");
                } else {
                    holder.agentStstusTextView.setText("Inactive");
                }
            } else {
                holder.agentStstusTextView.setText("");
            }
        }
        return view;
    }
}