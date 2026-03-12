package com.example.moofrosty.ui.enterstoreorders.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moofrosty.core.network.Constants;
import com.example.moofrosty.data.model.CategoryModel;
import com.example.moofrosty.data.model.SubCategoryModel;
import com.example.moofrosty.ui.filter.FilterSelectionListener;
import com.example.moofrosty.R;
import com.example.moofrosty.data.model.SubCategories;

import java.util.List;

public class SubCategoriesAdaptor extends RecyclerView.Adapter<SubCategoriesAdaptor.ViewHolder>  {

//    // Can hold either Categories or SubCategories
//    private List<CategoryModel> categoryList;
//    private List<SubCategoryModel> subCategoryList;
//    private boolean isCategory; // flag
//    private FilterSelectionListener listener;

    private List<SubCategoryModel> list;
    private FilterSelectionListener listener;

    // Correct Constructor
    public SubCategoriesAdaptor(List<SubCategoryModel> list, FilterSelectionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubCategoriesAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategoriesrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriesAdaptor.ViewHolder holder, int position) {
        SubCategoryModel model = list.get(position);
        holder.name.setText(model.subcategoryTitle);

//        String imageUrl = "https://moofrosty.ekatta.in/" + model.subcategoryImage;
        String imageUrl = Constants.BASE_URL + model.subcategoryImage;
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.icecategori)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // FIXED: Send "subcategory" so TakeOrderFragment updates the correct filter variable
                listener.onFilterSelected("subcategory", model.subcategoryTitle);
            }
        });

//        holder.itemView.setOnClickListener(v -> {
//            if (listener != null) {
//                // Pass "category" as type and the subcategory name as value
//                listener.onFilterSelected("category", model.subcategoryTitle);
//            }
//        });
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
            image = itemView.findViewById(R.id.itemImage);
            name = itemView.findViewById(R.id.itemName);
        }
    }



 //       1 st code below

//    private final List<SubCategories> list;
//   private FilterSelectionListener listener;
//
//    public SubCategoriesAdaptor(List<SubCategories> list) {
//        this.list = list;
//    }
//
//    public SubCategoriesAdaptor(List<SubCategories> list, FilterSelectionListener listener) {
//        this.list = list;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public SubCategoriesAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.subcategoriesrow, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SubCategoriesAdaptor.ViewHolder holder, int position) {
//        SubCategories model = list.get(position);
//        holder.image.setImageResource(model.getImage());
//        holder.name.setText(model.getName());
//
//        // --- ADD THIS CLICK LISTENER ---
//        holder.itemView.setOnClickListener(v -> {
//            if (listener != null) {
//                // Send the selected category name back
//                listener.onFilterSelected("category", model.getName());
//            }
//        });
//
//    }
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView image;
//        TextView name;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            image = itemView.findViewById(R.id.itemImage);
//            name = itemView.findViewById(R.id.itemName);
//        }
//    }
}
