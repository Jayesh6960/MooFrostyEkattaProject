package com.example.moofrosty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moofrosty.OfferAdapter;
import com.example.moofrosty.OfferModel;
import com.example.moofrosty.R;
import com.example.moofrosty.OfferAdapter;
import com.example.moofrosty.OfferModel;

import java.util.ArrayList;

public class TopOffersFragment extends Fragment {

    RecyclerView recyclerView;
    OfferAdapter adapter;
    ArrayList<OfferModel> list;

    public TopOffersFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_top_offers, container, false);

        recyclerView = view.findViewById(R.id.offer_recycler_top);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTopOffers();

        adapter = new OfferAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadTopOffers() {
        list = new ArrayList<>();
        list.add(new OfferModel(
                "🔥 Hot Deal: 50% OFF",
                "Limited-time mega offer!",
                "https://via.placeholder.com/150"
        ));

        list.add(new OfferModel(
                "Exclusive Membership Discount",
                "Save more with premium deals.",
                "https://via.placeholder.com/150"
        ));
        list.add(new OfferModel(
                "🔥 Hot Deal: 50% OFF",
                "Limited-time mega offer!",
                "https://via.placeholder.com/150"
        ));

        list.add(new OfferModel(
                "Exclusive Membership Discount",
                "Save more with premium deals.",
                "https://via.placeholder.com/150"
        ));
        list.add(new OfferModel(
                "🔥 Hot Deal: 50% OFF",
                "Limited-time mega offer!",
                "https://via.placeholder.com/150"
        ));

        list.add(new OfferModel(
                "Exclusive Membership Discount",
                "Save more with premium deals.",
                "https://via.placeholder.com/150"
        ));
        list.add(new OfferModel(
                "🔥 Hot Deal: 50% OFF",
                "Limited-time mega offer!",
                "https://via.placeholder.com/150"
        ));

        list.add(new OfferModel(
                "Exclusive Membership Discount",
                "Save more with premium deals.",
                "https://via.placeholder.com/150"
        ));
    }
}
