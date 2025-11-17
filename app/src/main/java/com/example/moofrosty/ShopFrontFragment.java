package com.example.moofrosty;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class ShopFrontFragment extends Fragment {

    TextView viewMore;
    ImageView offerOpen,damageopen;
    private FilterViewModel filterViewModel;
    public ShopFrontFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_shop_front, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        viewMore = view.findViewById(R.id.btn_view_more);
        offerOpen = view.findViewById(R.id.offeropen);
        damageopen = view.findViewById(R.id.offeropen);

        viewMore.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CategoriesFragment())
                    .addToBackStack(null) // Allows user to press "back"
                    .commit();
        });

        // Handle button clicks (e.g., Offers)
        offerOpen.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), OffersActivity.class);
//            intent.putExtra("ACTIVITY_TITLE", "Offers");
//            startActivity(intent);
        });

        damageopen.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DamageExpiryActivity.class);
            intent.putExtra("ACTIVITY_TITLE", "Damage/Shortage/Expiry");
            startActivity(intent);
        });

        setupBrandLogos(view);
        setupTopCategories(view);

    }

    private void setupBrandLogos(View view) {
        RecyclerView recyclerBrands = view.findViewById(R.id.recycler_brands_logo);
        recyclerBrands.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<BrandModel> brandList = new ArrayList<>();
        // ... (your list of brands) ...
        brandList.add(new BrandModel(R.drawable.allbrands, "All Brands"));
        brandList.add(new BrandModel(R.drawable.magnumbrand, "magnum"));
        brandList.add(new BrandModel(R.drawable.kwalitywall, "kwalitywall"));
        brandList.add(new BrandModel(R.drawable.arunbrand, "arun"));
        brandList.add(new BrandModel(R.drawable.cornettobrand, "Cornetto"));
        brandList.add(new BrandModel(R.drawable.amulbrand, "Amul"));

        // --- UPDATE THIS ---
        // Pass a new listener
        BrandLogoAdapter adapter = new BrandLogoAdapter(brandList, brandName -> {
            // This is the click logic
            filterViewModel.setBrand(brandName.equals("All Brands") ? "All" : brandName);
            filterViewModel.setCategory("All"); // Reset category

            // Switch to the TakeOrder tab
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_take_order);
        });
        recyclerBrands.setAdapter(adapter);
    }

    private void setupTopCategories(View view) {
        RecyclerView recyclerCategories = view.findViewById(R.id.recycler_top_categories);
        recyclerCategories.setLayoutManager(new GridLayoutManager(getContext(), 3));

        ArrayList<SubCategories> categoryList = new ArrayList<>();
        // ... (your list of categories) ...
        categoryList.add(new SubCategories(R.drawable.conecategory, "Ice Cream"));
        categoryList.add(new SubCategories(R.drawable.kulficategory, "Kulfi"));
        categoryList.add(new SubCategories(R.drawable.cupcategory, "Cups"));
        categoryList.add(new SubCategories(R.drawable.magnunkulficategory, "Magnum kulfi"));
        categoryList.add(new SubCategories(R.drawable.familypackone, "Family Pack Small"));
        categoryList.add(new SubCategories(R.drawable.miniconecategory, "Mini Cone"));


        // --- UPDATE THIS ---
        // Pass a new listener
        CategoryGridAdapter adapter = new CategoryGridAdapter(categoryList, categoryName -> {
            // This is the click logic
            filterViewModel.setCategory(categoryName);
            filterViewModel.setBrand("All"); // Reset brand

            // Switch to the TakeOrder tab
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_take_order);
        });
        recyclerCategories.setAdapter(adapter);
    }
}