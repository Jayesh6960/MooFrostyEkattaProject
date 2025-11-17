package com.example.moofrosty;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class CategoriesListFragment extends Fragment {
    private FilterViewModel filterViewModel;

    public CategoriesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_categories_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<SubCategories> list = new ArrayList<>();
        // ... (your list of categories) ...
        list.add(new SubCategories(R.drawable.conecategory, "Big Cones"));
        list.add(new SubCategories(R.drawable.kulficategory, "Kulfi"));
        list.add(new SubCategories(R.drawable.cupcategory, "Cups"));
        list.add(new SubCategories(R.drawable.familypacktwo, "Family Pack"));
        list.add(new SubCategories(R.drawable.magnunkulficategory, "Magnum kulfi"));
        list.add(new SubCategories(R.drawable.familypackcategory, "Family Pack Big"));
        list.add(new SubCategories(R.drawable.familypackone, "Family Pack Small"));
        list.add(new SubCategories(R.drawable.miniconecategory, "Mini Cone"));
        list.add(new SubCategories(R.drawable.familypacktwo, "Party Pack"));

        // --- THIS IS THE FIX ---
        // Create a new FilterSelectionListener that does what your lambda did
        FilterSelectionListener categoryClickListener = (filterType, categoryName) -> {
            // 1. Set the category in the ViewModel
            filterViewModel.setCategory(categoryName);
            filterViewModel.setBrand("All"); // Reset brand

            // 2. Go back to the ShopFrontFragment
            getParentFragmentManager().popBackStack();

            // 3. Switch to the TakeOrder tab
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_take_order);
        };

        // Pass the listener to the adapter
        SubCategoriesAdaptor adapter = new SubCategoriesAdaptor(list, categoryClickListener);
        recyclerView.setAdapter(adapter);
    }
}
