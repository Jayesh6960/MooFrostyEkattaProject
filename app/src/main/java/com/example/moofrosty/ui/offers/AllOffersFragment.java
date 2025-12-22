package com.example.moofrosty.ui.offers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.OfferModel;

import java.util.ArrayList;

public class AllOffersFragment extends Fragment {

    RecyclerView recyclerView;
    OfferAdapter adapter;
    ArrayList<OfferModel> list;

    public AllOffersFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_offers, container, false);

        recyclerView = view.findViewById(R.id.offer_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadDummyOffers();
        adapter = new OfferAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadDummyOffers() {

        list = new ArrayList<>();

        list.add(new OfferModel(
                "Buy 1 Get 1 Free",
                "Special offer valid for a limited time.",
                "https://via.placeholder.com/150"
        ));

        list.add(new OfferModel(
                "30% OFF on Dairy",
                "Use coupon DAIRY30 to avail discount.",
                "https://via.placeholder.com/150"
        ));

        list.add(new OfferModel(
                "Mega Combo Offer",
                "Combo pack discounts available.",
                "https://via.placeholder.com/150"
        ));
        list.add(new OfferModel(
                "Buy 1 Get 1 Free",
                "Special offer valid for a limited time.",
                "https://via.placeholder.com/150"
        ));

        list.add(new OfferModel(
                "30% OFF on Dairy",
                "Use coupon DAIRY30 to avail discount.",
                "https://via.placeholder.com/150"
        ));

        list.add(new OfferModel(
                "Mega Combo Offer",
                "Combo pack discounts available.",
                "https://via.placeholder.com/150"
        ));
    }
}
