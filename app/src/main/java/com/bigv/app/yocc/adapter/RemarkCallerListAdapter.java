package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.pojo.RemarkResponsePojo;
import com.bigv.app.yocc.utils.AUtils;

import java.util.ArrayList;

public class RemarkCallerListAdapter extends RecyclerView.Adapter<RemarkCallerListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<RemarkResponsePojo> remarkCallerList;

    public RemarkCallerListAdapter(Context context, ArrayList<RemarkResponsePojo> remarkCallerList) {
        this.context = context;
        this.remarkCallerList = remarkCallerList;
    }

    @NonNull
    @Override
    public RemarkCallerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_remark_caller,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RemarkCallerListAdapter.MyViewHolder holder, final int position) {

       final RemarkResponsePojo responsePojo = remarkCallerList.get(holder.getAdapterPosition());

        if (responsePojo != null){
            Log.i("TAG", "adapter remark list: "+responsePojo);
            holder.txtDate.setText(AUtils.getApiSideDateLocal(responsePojo.getCurrent_Date()));
            holder.txtFollowUpDate.setText(AUtils.getApiSideFollowUpDateLocal(responsePojo.getFollowDate()));
            String normalString = responsePojo.getRemark();
            String emogiAddedString = AUtils.stripNonValidXMLCharacters(normalString);
            holder.txtCallerRemark.setText(emogiAddedString);
        }

    }

    @Override
    public int getItemCount() {
        return remarkCallerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDate, txtFollowUpDate, txtCallerRemark;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtFollowUpDate = itemView.findViewById(R.id.txt_follow_up_date);
            txtCallerRemark = itemView.findViewById(R.id.txt_updated_remark);
        }
    }
}
