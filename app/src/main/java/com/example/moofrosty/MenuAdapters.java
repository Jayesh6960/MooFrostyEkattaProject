package com.example.moofrosty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapters extends RecyclerView.Adapter<MenuAdapters.MenuViewHolder> {

    private List<MenuOption> optionList;
  //  private AdapterView.OnItemClickListener mListener;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

//    public MenuAdapters(List<MenuOption> optionList) {
//        this.optionList = optionList;
//    }

    public MenuAdapters(List<MenuOption> optionList, OnItemClickListener listener) {
        this.optionList = optionList;
        this.mListener = listener;
    }


    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_option, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuOption option = optionList.get(position);
        holder.tvMenuTitle.setText(option.getTitle());
        holder.imgMenuIcon.setImageResource(option.getIconResId());
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

     class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMenuIcon;
        TextView tvMenuTitle;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenuIcon = itemView.findViewById(R.id.img_menu_icon);
            tvMenuTitle = itemView.findViewById(R.id.tv_menu_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
