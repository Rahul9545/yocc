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
import com.bigv.app.yocc.utils.AUtils;


/**
 * Created by mithun on 15/9/17.
 */

public class AnalyticReportAdapter extends ArrayAdapter<String> {

    private List<String> lstString;
    private Context context;
    private View view;
    private ViewHolder holder;
    public Integer[] mThumbIds = {
            R.drawable.ic_daily_wise_reports,
            R.drawable.ic_agent_reports,
            R.drawable.ic_call_reports,
    };


    public AnalyticReportAdapter(Context context, List<String> lstString) {
        super(context, android.R.layout.simple_list_item_1, lstString);
        this.context = context;
        this.lstString = lstString;
    }


    class ViewHolder {
        private ImageView iconImageView;
        private TextView titleTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.analytic_report_adapter, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.iconImageView = (ImageView) view.findViewById(R.id.icon_analytic_report_adapterIV);
            viewHolder.titleTextView = (TextView) view.findViewById(R.id.title_analytic_report_adapterTV);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(lstString) && !lstString.isEmpty()) {
            String title = lstString.get(position);

            if (!AUtils.isNullString(title)) {
                holder.titleTextView.setText(title);
                holder.iconImageView.setImageResource(mThumbIds[position]);
            }
        }
        return view;
    }

//    private void removeAccountOnClick(View v, int position) {
//        lstOprMstPojo.remove(position);
//        Type typeLstOprMst = new TypeToken<List<OprMstPojo>>() {
//        }.getType();
//        Gson gson = new Gson();
//        String jsonLstOprMst = gson.toJson(lstOprMstPojo, typeLstOprMst);
//        SharedPreferences.Editor editor;
//        SharedPreferences preferences = context.getSharedPreferences(null, 0);
//        editor = preferences.edit();
//        editor.putString(CrUtil.OPR_LST, jsonLstOprMst);
//        editor.commit();
//        notifyDataSetChanged();
//    }
}