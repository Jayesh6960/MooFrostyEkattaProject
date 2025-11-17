package com.example.moofrosty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BrandLogoAdapter extends RecyclerView.Adapter<BrandLogoAdapter.ViewHolder>{

    private final List<BrandModel> brandList;

    // 1. Define the click listener interface
    public interface OnBrandLogoClickListener {
        void onBrandLogoClick(String brandName);
    }

    private final OnBrandLogoClickListener listener;
    // 2. Update constructor
    public BrandLogoAdapter(List<BrandModel> brandList, OnBrandLogoClickListener listener) {
        this.brandList = brandList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BrandLogoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_brand_logo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandLogoAdapter.ViewHolder holder, int position) {
        BrandModel brand = brandList.get(position);
        holder.imgBrand.setImageResource(brand.getImageResId());
        holder.txtBrand.setText(brand.getName());

        // 3. Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBrandLogoClick(brand.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBrand;
        TextView txtBrand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBrand = itemView.findViewById(R.id.img_brand_logo);
            txtBrand = itemView.findViewById(R.id.tv_brand_name);
        }
    }

}
