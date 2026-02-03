package com.example.moofrosty.ui.attendance.leave;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.LeaveHistoryResponse;

import java.util.List;

public class LeaveHistoryAdapter  extends RecyclerView.Adapter<LeaveHistoryAdapter.ViewHolder>{

    private List<LeaveHistoryResponse.LeaveItem> list;

    public LeaveHistoryAdapter(List<LeaveHistoryResponse.LeaveItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leave_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaveHistoryResponse.LeaveItem item = list.get(position);

        Context context = holder.itemView.getContext();

        // FROM & TO DATE
        holder.tvFromDate.setText(item.getStartDate());
        holder.tvToDate.setText(item.getEndDate());


        // LEAVE TYPE
        String typeName = "Casual Leave";
        if ("2".equals(item.getLeaveType())) {
            typeName = "Medical Leave";
        } else if ("3".equals(item.getLeaveType())) {
            typeName = "Leave Without Pay";
        }
        holder.tvLeaveType.setText(typeName);
        holder.tvReason.setText(item.getReason());

        // DAYS
      //  holder.tvDays.setText(String.valueOf(item.getTotalDays()));

        // STATUS
        if (item.getLeaveStatus() == 1) {
            holder.tvStatus.setText("Pending");
            holder.tvStatus.setTextColor(Color.parseColor("#F57C00"));
            holder.dayBadge.setBackgroundResource(R.drawable.bg_leave_pending);

        } else if (item.getLeaveStatus() == 2) {
            holder.tvStatus.setText("Approved");
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
            holder.dayBadge.setBackgroundResource(R.drawable.bg_leavedays_circle);

        } else {
            holder.tvStatus.setText("Rejected");
            holder.tvStatus.setTextColor(Color.parseColor("#C62828"));
            holder.dayBadge.setBackgroundResource(R.drawable.bg_leave_rejected);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView tvType, tvDateRange, tvReason, tvStatus;
//        CardView statusCard;
        TextView tvFromDate, tvToDate, tvLeaveType, tvStatus, tvDays, tvReason;
        View dayBadge;

        public ViewHolder(View v) {
            super(v);
//            tvType = v.findViewById(R.id.tv_leave_type);
//            tvDateRange = v.findViewById(R.id.tv_date_range);
//            tvReason = v.findViewById(R.id.tv_reason);
//            tvStatus = v.findViewById(R.id.tv_status);
//            statusCard = v.findViewById(R.id.card_status);

            tvFromDate   = itemView.findViewById(R.id.tvFromDate);
            tvToDate     = itemView.findViewById(R.id.tvToDate);
            tvLeaveType  = itemView.findViewById(R.id.tvLeaveType);
            tvStatus     = itemView.findViewById(R.id.tvStatus);
            tvDays       = itemView.findViewById(R.id.tvDays);
            tvReason       = itemView.findViewById(R.id.tvReasonType);
            dayBadge     = itemView.findViewById(R.id.dayBadge);
        }
    }
}
