package com.example.moofrosty.ui.enterstoreorders.categories;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moofrosty.data.model.CategoryModel;
import com.example.moofrosty.ui.filter.CategorySelectionListener;
import com.example.moofrosty.ui.filter.FilterSelectionListener;
import com.example.moofrosty.R;
import com.example.moofrosty.ui.brands.BrandsFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

public class BottomSheetSubCategeries extends BottomSheetDialogFragment implements CategorySelectionListener, FilterSelectionListener {
    private FilterSelectionListener parentFilterListener; // The Parent Fragment (TakeOrderFragment)
    private TabLayout tabLayout;
    private int selectedCategoryId = 0;

    public BottomSheetSubCategeries() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Find parent fragment to communicate back
        Fragment parent = getParentFragment();
        if (parent instanceof FilterSelectionListener) {
            parentFilterListener = (FilterSelectionListener) parent;
        } else {
            // Log warning or throw exception if strict
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_sub_categeries, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Categories"));
//        tabLayout.addTab(tabLayout.newTab().setText("Sub-Categories"));

        // Load Tab 1 (Categories) by default
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Categories"));
//        tabLayout.addTab(tabLayout.newTab().setText("Sub-Categories"));

        // Load Tab 1: Categories (pass 'this' as listener)
        replaceFragment(CategoryFragment.newInstance());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(CategoryFragment.newInstance());
                } else {
                    // Pass 'this' as listener so SubCategoriesFragment calls onFilterSelected() here
                    replaceFragment(SubCategoriesFragment.newInstance(selectedCategoryId));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
        // 1. Handle Click from CategoriesFragment (Tab 1)
        @Override
        public void onCategorySelected(CategoryModel category) {
            this.selectedCategoryId = category.categoryId;

            if (parentFilterListener != null) {
                parentFilterListener.onFilterSelected("category", category.categoryTitle);
            }
///     Stop switch just dismiss
//            TabLayout.Tab tab = tabLayout.getTabAt(1);
//            if (tab != null) tab.select();

            dismiss();
        }
        // --- Scenario 2: API Loads (Index 0 Default) ---
        @Override
        public void onDefaultCategoryLoaded(CategoryModel category) {
            // Just set the ID. Do NOT switch tabs.
            this.selectedCategoryId = category.categoryId;
        }
        // 2. Handle Click from SubCategoriesFragment (Tab 2)
        @Override
        public void onFilterSelected(String filterType, String value) {
            // Pass it up to TakeOrderFragment
            if (parentFilterListener != null) {
                parentFilterListener.onFilterSelected(filterType, value);
            }
            dismiss(); // Close sheet
        }

//       // replaceFragment(new SubCategoriesFragment());
//        // Load initial fragment
//        replaceFragment(SubCategoriesFragment.newInstance(filterListener));
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
////                if (tab.getPosition() == 0) {
////                    replaceFragment(new SubCategoriesFragment());
////                } else if (tab.getPosition() == 1) {
////                    replaceFragment(new BrandsFragment());
////                }
//                if (tab.getPosition() == 0) {
//                    replaceFragment(SubCategoriesFragment.newInstance(filterListener));
//                } else if (tab.getPosition() == 1) {
//                    replaceFragment(BrandsFragment.newInstance(filterListener));
//                }
//            }
//
//            @Override public void onTabUnselected(TabLayout.Tab tab) {}
//            @Override public void onTabReselected(TabLayout.Tab tab) {}
//        });

//    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragmentContainer, fragment);
//        transaction.commit();
//    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}