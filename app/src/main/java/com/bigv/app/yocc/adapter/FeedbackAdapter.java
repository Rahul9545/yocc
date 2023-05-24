package com.bigv.app.yocc.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.activity.UpdateFeedActivity;
import com.bigv.app.yocc.pojo.RemarkResponsePojo;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<RemarkResponsePojo> remarkList;
    private OnItemClickedListener listener;

    public FeedbackAdapter(Context context, ArrayList<RemarkResponsePojo> remarkList, OnItemClickedListener listener){
        this.context = context;
        this.remarkList = remarkList;
        this.listener = listener;
    }
    public interface OnItemClickedListener{
        void onItemClicked(RemarkResponsePojo pojo);
    }

    @NonNull
    @Override
    public FeedbackAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.iten_remark_notification,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.MyViewHolder holder, int position) {
        RemarkResponsePojo responsePojo = remarkList.get(position);
        Log.i("TAG", "adapter remark list: "+responsePojo);

        if (responsePojo != null){
            holder.txtCallerNumber.setText(responsePojo.getCaller());
            String num = holder.txtCallerNumber.getText().toString();
            Log.d("TAG", "onBindViewHolder: "+num);
            holder.txtCallerName.setText(responsePojo.getName());
            if (responsePojo.getRemark() != null){
                holder.txtRemarkInfo.setVisibility(View.VISIBLE);
                holder.txtRemarkInfo.setText(responsePojo.getRemark());
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(responsePojo);

            }
        });

    }

    @Override
    public int getItemCount() {
        return remarkList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCallerNumber;
        private TextView txtRemarkInfo;
        private TextView txtCallerName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCallerNumber = itemView.findViewById(R.id.txt_caller);
            txtRemarkInfo = itemView.findViewById(R.id.txt_caller_feed);
            txtCallerName = itemView.findViewById(R.id.txt_caller_name);
        }
    }
}
