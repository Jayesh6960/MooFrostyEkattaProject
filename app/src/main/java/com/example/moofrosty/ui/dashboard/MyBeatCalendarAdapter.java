package com.example.moofrosty.ui.dashboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.CalendarDateModel;

import java.util.List;

public class MyBeatCalendarAdapter extends RecyclerView.Adapter<MyBeatCalendarAdapter.ViewHolder>{

    private List<CalendarDateModel> dates;

    public MyBeatCalendarAdapter(List<CalendarDateModel> dates) {
        this.dates = dates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_date, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarDateModel model = dates.get(position);
        holder.tvDay.setText(model.getDay());
        holder.tvDate.setText(model.getDate());

        if (model.isSelected()) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.Purple_Color));
            holder.tvDay.setTextColor(Color.WHITE);
            holder.tvDate.setTextColor(Color.WHITE);
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
            holder.tvDay.setTextColor(Color.GRAY);
            holder.tvDate.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvDate;
        CardView cardView;
        public ViewHolder(View v) {
            super(v);
            tvDay = v.findViewById(R.id.tv_day);
            tvDate = v.findViewById(R.id.tv_date);
            cardView = (CardView) v;
        }
    }
}
