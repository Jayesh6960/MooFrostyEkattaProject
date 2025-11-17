package com.example.moofrosty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubCategoriesAdaptor extends RecyclerView.Adapter<SubCategoriesAdaptor.ViewHolder>  {

    private final List<SubCategories> list;
   private FilterSelectionListener listener;

    public SubCategoriesAdaptor(List<SubCategories> list) {
        this.list = list;
    }

    public SubCategoriesAdaptor(List<SubCategories> list, FilterSelectionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubCategoriesAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcategoriesrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriesAdaptor.ViewHolder holder, int position) {
        SubCategories model = list.get(position);
        holder.image.setImageResource(model.getImage());
        holder.name.setText(model.getName());

        // --- ADD THIS CLICK LISTENER ---
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // Send the selected category name back
                listener.onFilterSelected("category", model.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
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
}
