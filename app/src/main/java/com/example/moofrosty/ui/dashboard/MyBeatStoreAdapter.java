    package com.example.moofrosty.ui.dashboard;

    import android.content.Context;
    import android.content.Intent;
    import android.net.Uri;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.moofrosty.R;
    import com.example.moofrosty.data.model.Store;
    import com.example.moofrosty.ui.store.StoreProfileActivity;

    import java.io.Serializable;
    import java.util.List;

    public class MyBeatStoreAdapter extends RecyclerView.Adapter<MyBeatStoreAdapter.ViewHolder>{

        private List<Store> stores;
        private Context context;

        public MyBeatStoreAdapter(Context context, List<Store> stores) {
            this.context = context;
            this.stores = stores;
        }

        public void updateList(List<Store> newStores) {
            this.stores = newStores;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Store store = stores.get(position);
            // 1. UPDATE: Use getStoreName() instead of getName()
            holder.tvName.setText(store.getStoreName());

            // 2. Status Icons
            if (store.isOrderTaken()) {
                holder.imgStatus.setImageResource(R.drawable.cartgreenicon);
                holder.imgStatus.setVisibility(View.VISIBLE);
            } else if (store.isVisited()) {
                holder.imgStatus.setImageResource(R.drawable.locationuser);
                holder.imgStatus.setVisibility(View.VISIBLE);
            } else {
                holder.imgStatus.setVisibility(View.INVISIBLE);
            }

            // 3. UPDATE: Use getMobileNumber()
            holder.btnCall.setOnClickListener(v -> {
                String mobile = store.getMobileNumber();
                if (mobile != null && !mobile.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobile));
                    context.startActivity(intent);
                }
            });

            // 4. UPDATE: Use getLat() / getLng() parsing logic
            holder.btnDirection.setOnClickListener(v -> {
                double lat = store.getLat();
                double lng = store.getLng();

                if (lat != 0.0 && lng != 0.0) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(mapIntent);
                    }
                }
            });

            holder.itemView.setOnClickListener(v -> {
                // Uncomment and use correct Activity when ready
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
            TextView tvName, tvRR, tvBeatCode;
            ImageView btnCall, btnDirection, imgStatus;

            public ViewHolder(View v) {
                super(v);
                tvName = v.findViewById(R.id.tv_store_name);
            //  tvRR = v.findViewById(R.id.tv_rr_value);
                btnCall = v.findViewById(R.id.btn_call);
                btnDirection = v.findViewById(R.id.btn_direction);
                imgStatus = v.findViewById(R.id.img_status);
            }
        }
    }
