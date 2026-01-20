package com.example.moofrosty.ui.enterstoreorders.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moofrosty.R;
import com.example.moofrosty.data.model.CategoryModel;
import com.example.moofrosty.ui.filter.CategorySelectionListener;

import java.util.List;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.ViewHolder>{
    private List<CategoryModel> list;
    private CategorySelectionListener listener;

    public CategoryAdaptor(List<CategoryModel> list, CategorySelectionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel model = list.get(position);
        holder.name.setText(model.categoryTitle);

        String imageUrl = "https://moofrosty.ekatta.in/" + model.categoryImage;
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.icecategori)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCategorySelected(model);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_category);
            name = itemView.findViewById(R.id.tv_category_name);
        }
    }
}
