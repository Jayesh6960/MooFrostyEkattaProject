package com.example.moofrosty.ui.store;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moofrosty.R;

public class StoreProfileFragment extends Fragment {

    private StoreProfileViewModel viewModel;

    // UI Elements
    private TextView tvOwnerName, tvHulCode, tvPartyCode, tvPhone, tvAddress;
    private ImageView btnCall, btnDirection, btnCart;
    private LinearLayout layoutClassHeader, layoutClassContent;
    private LinearLayout layoutBusinessHeader, layoutBusinessContent;
    private ImageView imgClassToggle, imgBusinessToggle;

    public StoreProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Init Views
        tvOwnerName = view.findViewById(R.id.tv_owner_name);
        tvHulCode = view.findViewById(R.id.tv_hul_code);
        tvPartyCode = view.findViewById(R.id.tv_party_code);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvAddress = view.findViewById(R.id.tv_address);


        layoutClassHeader = view.findViewById(R.id.layout_class_header);
        layoutClassContent = view.findViewById(R.id.layout_class_content);
        imgClassToggle = view.findViewById(R.id.img_class_toggle);

        layoutBusinessHeader = view.findViewById(R.id.layout_business_header);
        layoutBusinessContent = view.findViewById(R.id.layout_business_content);
        imgBusinessToggle = view.findViewById(R.id.img_business_toggle);

        // 2. Setup ViewModel (Scoped to Activity to share data)
        viewModel = new ViewModelProvider(requireActivity()).get(StoreProfileViewModel.class);

        // 3. Observe Data
        viewModel.getStore().observe(getViewLifecycleOwner(), store -> {
            if (store != null) {
                tvOwnerName.setText("Owner Name  : " + store.getOwnerName());
            //    tvHulCode.setText("HUL Code: " + store.getHulCode());
                tvHulCode.setText("Moo Frosty Code : Empty" );
                tvPartyCode.setText("Outlet Code  : " + store.getShopId());    //.replaceAll("[^0-9]", ""));
                tvPhone.setText(store.getMobileNumber());
                tvAddress.setText(store.getAddress());

                // Click Listeners for Actions
//                btnCall.setOnClickListener(v -> {
//                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse("tel:" + store.getPhoneNumber()));
//                    startActivity(intent);
//                });

//                btnDirection.setOnClickListener(v -> {
//                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + store.getLat() + "," + store.getLng());
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//                    startActivity(mapIntent);
//                });
            }
        });

        // 4. Expandable Logic Observation
        viewModel.getIsClassificationExpanded().observe(getViewLifecycleOwner(), isExpanded -> {
            layoutClassContent.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            imgClassToggle.setRotation(isExpanded ? 180 : 0); // Rotate icon
        });

        viewModel.getIsBusinessDetailsExpanded().observe(getViewLifecycleOwner(), isExpanded -> {
            layoutBusinessContent.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            imgBusinessToggle.setRotation(isExpanded ? 180 : 0);
        });

        // 5. Toggle Click Listeners
        layoutClassHeader.setOnClickListener(v -> viewModel.toggleClassification());
        layoutBusinessHeader.setOnClickListener(v -> viewModel.toggleBusinessDetails());

     //   btnCart.setOnClickListener(v -> Toast.makeText(requireContext(), "Opening Order Screen...", Toast.LENGTH_SHORT).show());
    }
    }
