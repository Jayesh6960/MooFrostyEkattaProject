package com.example.moofrosty.ui.ATMSummary;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private List<StoreModel> list;

    public StoreAdapter(List<StoreModel> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtMocSales, txtDaySales, txtinTime, txtoutTime;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtMocSales = itemView.findViewById(R.id.txtMocSales);
            txtDaySales = itemView.findViewById(R.id.txtDaySales);
            txtinTime = itemView.findViewById(R.id.txtinTime);
            txtoutTime = itemView.findViewById(R.id.txtoutTime);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_store, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        StoreModel item = list.get(position);

        holder.txtName.setText(item.getStoreName());
        holder.txtMocSales.setText("MOC Sales: ₹" + item.getMocSales());

        holder.txtDaySales.setText(
                "Day Sales: ₹" + item.getDaySales() +
                        " No. of lines: " + item.getNumberOfLines()
        );

        holder.txtinTime.setText("In Time: " + item.getInTime());
        holder.txtoutTime.setText("Out Time: " + item.getOutTime());
    }

    // ✅ FIXED METHOD
    public void updateList(List<StoreModel> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}