package com.example.moofrosty.ui.brands;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moofrosty.ui.filter.FilterSelectionListener;
import com.example.moofrosty.ui.filter.FilterViewModel;
import com.example.moofrosty.R;
import com.example.moofrosty.data.model.BrandModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BrandsListFragment extends Fragment {

    private FilterViewModel filterViewModel;

    public BrandsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brands_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the Activity-scoped ViewModel
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        // Your code uses 'view' as the RecyclerView, so let's cast it
        RecyclerView recyclerView = view.findViewById(R.id.recycler_brands_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        ArrayList<BrandModel> brandList = new ArrayList<>();
        // ... (your list of brands) ...
        brandList.add(new BrandModel(R.drawable.amulbrand, "Amul"));
        brandList.add(new BrandModel(R.drawable.cornettobrand, "Cornetto"));
        brandList.add(new BrandModel(R.drawable.magnumbrand, "magnum"));
        brandList.add(new BrandModel(R.drawable.arunbrand, "arun"));
        brandList.add(new BrandModel(R.drawable.kwalitywall, "kwalitywall"));
        brandList.add(new BrandModel(R.drawable.motherdairybrand, "motherdairy"));
        brandList.add(new BrandModel(R.drawable.havmorebrand, "havemore"));
        brandList.add(new BrandModel(R.drawable.vadinalbrand, "Vadinal"));

        // --- THIS IS THE FIX ---
        // Create a new FilterSelectionListener that does what your lambda did
        FilterSelectionListener brandClickListener = (filterType, brandName) -> {
            // 1. Set the brand in the ViewModel
            filterViewModel.setBrand(brandName);
            filterViewModel.setCategory("All"); // Reset category

            // 2. Go back to the ShopFrontFragment
            getParentFragmentManager().popBackStack();

            // 3. Switch to the TakeOrder tab
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_take_order);
        };

        // Pass the listener to the adapter
        BrandsAdapter adapter = new BrandsAdapter(brandList, brandClickListener);
        recyclerView.setAdapter(adapter);
    }
    }