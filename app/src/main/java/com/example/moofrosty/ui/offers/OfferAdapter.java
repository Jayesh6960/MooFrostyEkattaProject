package com.example.moofrosty.ui.offers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moofrosty.R;
import com.example.moofrosty.data.model.OfferModel;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

    private Context context;
    private List<OfferModel> offerList;

    public OfferAdapter(Context context, List<OfferModel> offerList) {
        this.context = context;
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OfferModel offer = offerList.get(position);
        holder.title.setText(offer.getTitle());
        holder.description.setText(offer.getDescription());
        // Safe Glide loading with error handling
        String imageUrl = offer.getImageUrl();

        if (imageUrl == null || imageUrl.isEmpty()) {
            holder.image.setImageResource(R.drawable.special_offer);
        } else {
            Glide.with(context.getApplicationContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.special_offer)   // while loading
                    .error(R.drawable.special_offer)         // if URL fails
                    .into(holder.image);
        }

    }
    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.offer_image);
            title = itemView.findViewById(R.id.offer_title);
            description = itemView.findViewById(R.id.offer_description);
        }
    }
}
