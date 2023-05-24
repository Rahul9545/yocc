package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.bigv.app.yocc.pojo.LanguagePojo;

/**
 * Created by MiTHUN on 27/11/17.
 */

public class AgentReplacerLanguageListAdapter extends ArrayAdapter<LanguagePojo> {

    private Context context;
    private List<LanguagePojo> languagePojoList;

    public AgentReplacerLanguageListAdapter(Context context, int textViewResourceId,
                                            List<LanguagePojo> languagePojoList) {
        super(context, textViewResourceId, languagePojoList);
        this.context = context;
        this.languagePojoList = languagePojoList;
    }

    @Override
    public int getCount() {
        return languagePojoList.size();
    }

    @Override
    public LanguagePojo getItem(int position) {
        return languagePojoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LanguagePojo languagePojo = languagePojoList.get(position);
        TextView label = new TextView(context);
        label.setTextSize(20);
        label.setTextColor(Color.BLACK);
        label.setText(languagePojoList.get(position).getLanguage());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(20);
        label.setPadding(15, 10, 0, 10);
        label.setText(languagePojoList.get(position).getLanguage());
        return label;
    }
}