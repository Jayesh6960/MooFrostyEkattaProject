package com.example.moofrosty.ui.ATMSummary;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private List<StoreModel> list;

    public StoreAdapter(List<StoreModel> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtMocSales, txtDaySales, txtinTime, txtoutTime,txtlines;
        ImageView dropdown;
        CardView cardView;
        LinearLayout daysales,intimeouttime;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtMocSales = itemView.findViewById(R.id.txtMocSales);
            txtDaySales = itemView.findViewById(R.id.txtDaySalesValue);
            txtinTime = itemView.findViewById(R.id.txtinTime);
            txtlines = itemView.findViewById(R.id.txtStoreLinesValue);
            cardView=itemView.findViewById(R.id.mocsales);
            daysales=itemView.findViewById(R.id.daysales);
            intimeouttime=itemView.findViewById(R.id.intimeouttime);


            txtoutTime = itemView.findViewById(R.id.txtoutTime);
            dropdown=itemView.findViewById(R.id.drowpdow);

            dropdown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cardView.getVisibility() == View.VISIBLE) {

                        cardView.setVisibility(View.GONE);
                        daysales.setVisibility(View.GONE);
                        intimeouttime.setVisibility(View.GONE);

                    } else {

                        cardView.setVisibility(View.VISIBLE);
                        daysales.setVisibility(View.VISIBLE);
                        intimeouttime.setVisibility(View.VISIBLE);

                    }

                }
            });


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
        holder.txtMocSales.setText( "₹"+String.valueOf(item.getMocSales()));

        holder.txtDaySales.setText(
                String.valueOf("₹"+item.getDaySales()));
        holder.txtlines.setText("5");

        holder.txtinTime.setText(item.getInTime());
        holder.txtoutTime.setText(item.getOutTime());
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