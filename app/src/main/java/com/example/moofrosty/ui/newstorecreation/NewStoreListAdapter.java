package com.example.moofrosty.ui.newstorecreation;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.StoreCreationModel;
import com.example.moofrosty.data.model.StoreListResponse;

import java.util.List;

public class NewStoreListAdapter extends RecyclerView.Adapter<NewStoreListAdapter.ViewHolder>{

  //  private List<StoreCreationModel> list;
    private List<StoreListResponse.StoreModel> list;
    private OnItemClickListener listener;

//    public NewStoreListAdapter(List<StoreCreationModel> list) {
//        this.list = list;
//    }.

    public interface OnItemClickListener {
        void onItemClick(StoreListResponse.StoreModel item);
    }

    public NewStoreListAdapter(List<StoreListResponse.StoreModel> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public NewStoreListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_store_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewStoreListAdapter.ViewHolder holder, int position) {

        StoreListResponse.StoreModel item = list.get(position);

        holder.tvName.setText(item.getStoreName());
        holder.tvOwner.setText(item.getOwnerName());

        // Status Logic: 1 = Approved, 2 = Rejected, 0 = Pending (default)
        if (item.getStatus() == 1) {
            holder.tvStatus.setText("APPROVED");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
        } else if (item.getStatus() == 2) {
            holder.tvStatus.setText("REJECTED");
            holder.tvStatus.setTextColor(Color.RED);
        } else {
            holder.tvStatus.setText("PENDING");
            holder.tvStatus.setTextColor(Color.parseColor("#FFA000")); // Orange
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        Log.d("Storelist :", "Storelist ");

//        StoreCreationModel item = list.get(position);
//        holder.tvName.setText(item.getStoreName());
//        holder.tvOwner.setText(item.getOwnerName());
//        holder.tvStatus.setText(item.getStatus());
//
//        if ("Approved".equalsIgnoreCase(item.getStatus())) {
//            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
//        } else {
//            holder.tvStatus.setTextColor(Color.parseColor("#FFA000")); // Orange
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvOwner, tvStatus;
        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_store_name);
            tvOwner = v.findViewById(R.id.tv_owner_name);
            tvStatus = v.findViewById(R.id.tv_status);
        }
    }
}
