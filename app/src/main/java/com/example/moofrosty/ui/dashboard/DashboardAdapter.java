package com.example.moofrosty.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.DashboardItem;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder>{

    private List<DashboardItem> items;

    public DashboardAdapter(List<DashboardItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dashboard_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvAchValue.setText("Ach: " + item.getAchievementText()); // Added prefix
        holder.progressIndicator.setProgress(item.getProgress());
        holder.tvPercent.setText(item.getProgress() + "%");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPercent, tvAchValue;
        CircularProgressIndicator progressIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPercent = itemView.findViewById(R.id.tv_percent);
            tvAchValue = itemView.findViewById(R.id.tv_ach_value); // New ID
            progressIndicator = itemView.findViewById(R.id.progress_indicator);
        }
    }
}
