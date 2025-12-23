package com.example.moofrosty.ui.dashboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.DashboardItem;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder>{

    private List<DashboardItem> items;

    public DetailAdapter(List<DashboardItem> items) { this.items = items; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardItem item = items.get(position);

        // Bind Data
        holder.tvName.setText(item.getTitle());
        holder.tvTarget.setText(String.valueOf(item.getTarget()));
        holder.tvAch.setText(String.valueOf(item.getAchieved()));
        holder.tvPerc.setText(item.getProgress() + "%");

        // Color Logic for Percentage
        if(item.getProgress() < 35) {
            holder.tvPerc.setTextColor(Color.parseColor("#E53935")); // Red
        } else if(item.getProgress() < 80) {
            holder.tvPerc.setTextColor(Color.parseColor("#FB8C00")); // Orange
        } else {
            holder.tvPerc.setTextColor(Color.parseColor("#43A047")); // Green
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTarget, tvAch, tvPerc;
        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_col_param);
            tvTarget = v.findViewById(R.id.tv_col_tgt);
            tvAch = v.findViewById(R.id.tv_col_ach);
            tvPerc = v.findViewById(R.id.tv_col_perc);
        }
    }

}
