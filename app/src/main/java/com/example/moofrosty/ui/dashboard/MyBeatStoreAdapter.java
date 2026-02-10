package com.example.moofrosty.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.Store;
import com.example.moofrosty.ui.store.StoreProfileActivity;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.io.Serializable;
import java.util.List;

public class MyBeatStoreAdapter
        extends RecyclerView.Adapter<MyBeatStoreAdapter.ViewHolder> {

    private Context context;
    private List<Store> stores;

    private ToolTipsManager toolTipsManager;
    private int currentTooltipPosition = RecyclerView.NO_POSITION;

    public MyBeatStoreAdapter(Context context, List<Store> stores) {
        this.context = context;
        this.stores = stores;

        toolTipsManager = new ToolTipsManager((view, anchorViewId, byUser) -> {
            currentTooltipPosition = RecyclerView.NO_POSITION;
        });
    }

    public void updateList(List<Store> newStores) {
        dismissTooltip();
        this.stores = newStores;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_store_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {

        Store store = stores.get(position);
        holder.tvName.setText(store.getStoreName());

        String tooltipText = "";

        if (store.isOrderTaken()) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.drawable.cartgreenicon);
            tooltipText = "Order Taken";
        } else if (store.isVisited()) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.drawable.locationuser);
            tooltipText = "Visited";
        } else {
            holder.imgStatus.setVisibility(View.INVISIBLE);
        }

        String finalTooltipText = tooltipText;

        holder.imgStatus.setOnClickListener(v -> {
            if (finalTooltipText.isEmpty()) return;

            if (currentTooltipPosition == position) {
                dismissTooltip();
            } else {
                showTooltip(holder.imgStatus, holder.itemView,
                        finalTooltipText, position);
            }
        });

        holder.btnCall.setOnClickListener(v -> {
            String mobile = store.getMobileNumber();
            if (mobile != null && !mobile.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobile));
                context.startActivity(intent);
            }
        });

        holder.btnDirection.setOnClickListener(v -> {
            double lat = store.getLat();
            double lng = store.getLng();

            if (lat != 0 && lng != 0) {
                Uri uri = Uri.parse("google.navigation:q=" + lat + "," + lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            dismissTooltip();
            Intent intent = new Intent(context, StoreProfileActivity.class);
            intent.putExtra("STORE_DATA", (Serializable) store);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stores != null ? stores.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView btnCall, btnDirection, imgStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_store_name);
            btnCall = itemView.findViewById(R.id.btn_call);
            btnDirection = itemView.findViewById(R.id.btn_direction);
            imgStatus = itemView.findViewById(R.id.img_status);
        }
    }

    // ---------------- TOOLTIP ----------------
    //Above Used to mak
    //Updated Code Date 10-02-2026
    private PopupWindow tooltipWindow;
    private void showTooltip(View anchorView, View parentView, String text, int position) {
        dismissTooltip(); // dismiss previous

        LayoutInflater inflater = LayoutInflater.from(anchorView.getContext());
        View tooltipView = inflater.inflate(R.layout.layout_tooltip, null);

        TextView tvTooltip = tooltipView.findViewById(R.id.tv_tooltip_text);
        tvTooltip.setText(text);
//Tootip Window Concepts Used to Adjust the size and the Shape of the
        tooltipView.measure(
                View.MeasureSpec.AT_MOST,
                View.MeasureSpec.AT_MOST
        );

        int tooltipHeight = tooltipView.getMeasuredHeight();

// Show above the icon

        tooltipWindow = new PopupWindow(
                tooltipView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true // focusable
        );


        tooltipWindow.setElevation(8f);
        tooltipWindow.setOutsideTouchable(true);
        tooltipWindow.setTouchable(true);

        // Show above the icon
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        tooltipWindow.showAtLocation(
                parentView,
                Gravity.NO_GRAVITY,
                location[0],
                location[1] - tooltipView.getMeasuredHeight() - 10
        );
        currentTooltipPosition = position;
    }

//Old Code
//    private void showTooltip(View anchor, View itemView,
//                             String text, int position) {
//
//        dismissTooltip();
//
//        ViewGroup rootView =
//                (ViewGroup) itemView.getRootView();
//
//        ToolTip toolTip = new ToolTip.Builder(
//                context,
//                anchor,
//                rootView,
//                text,
//                ToolTip.POSITION_RIGHT_TO
//        )
//                .setAlign(ToolTip.ALIGN_RIGHT)
//                .setBackgroundColor(
//                        context.getResources().getColor(R.color.textGreen)
//                )
//                .build();
//
//        toolTipsManager.show(toolTip);
//        currentTooltipPosition = position;
//    }

    private void dismissTooltip() {
        toolTipsManager.dismissAll();
        currentTooltipPosition = RecyclerView.FOCUS_RIGHT;
    }
}
