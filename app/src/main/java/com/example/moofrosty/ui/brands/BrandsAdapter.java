package com.example.moofrosty.ui.brands;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.ui.filter.FilterSelectionListener;
import com.example.moofrosty.R;
import com.example.moofrosty.data.model.BrandModel;

import java.util.List;

public class BrandsAdapter  extends RecyclerView.Adapter<BrandsAdapter.ViewHolder>{

    private List<BrandModel> brandList;
    private FilterSelectionListener listener;

    public BrandsAdapter(List<BrandModel> brandList) {
        this.brandList = brandList;
    }

    public BrandsAdapter(List<BrandModel> list, FilterSelectionListener listener) {
        this.brandList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BrandsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brandrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandsAdapter.ViewHolder holder, int position) {
        BrandModel brand = brandList.get(position);
        holder.imgBrand.setImageResource(brand.getImageResId());
        holder.txtBrand.setText(brand.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // Send the selected brand name back
                listener.onFilterSelected("brand", brand.getName());
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
            imgBrand = itemView.findViewById(R.id.imgBrand);
            txtBrand = itemView.findViewById(R.id.txtBrand);
        }
    }
}
