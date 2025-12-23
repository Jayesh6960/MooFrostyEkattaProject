package com.example.moofrosty.ui.enterstoreorders.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.SubCategories;

import java.util.List;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.ViewHolder>{

    private final List<SubCategories> categoryList;

    // 1. Define the click listener interface
    public interface OnCategoryGridClickListener {
        void onCategoryGridClick(String categoryName);
    }

    private final OnCategoryGridClickListener listener;

    // 2. Update constructor
    public CategoryGridAdapter(List<SubCategories> categoryList, OnCategoryGridClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public CategoryGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_grid, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryGridAdapter.ViewHolder holder, int position) {
        SubCategories category = categoryList.get(position);
        holder.imgCategory.setImageResource(category.getImage());
        holder.tvCategoryName.setText(category.getName());

        // 3. Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryGridClick(category.getName());
            }
        });
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView tvCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.img_category);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
        }
    }
}
