package com.example.moofrosty;

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

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

public class BottomSheetSubCategeries extends BottomSheetDialogFragment {
    private FilterSelectionListener filterListener;

    public BottomSheetSubCategeries() {
        // Required empty public constructor
    }

    TabLayout tabLayout;
    ViewPager2 viewPager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Get the parent fragment (TakeOrderFragment) and cast it as the listener
        Fragment parent = getParentFragment();
        if (parent instanceof FilterSelectionListener) {
            filterListener = (FilterSelectionListener) parent;
        } else {
            throw new RuntimeException("Parent fragment must implement FilterSelectionListener");
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

       // replaceFragment(new SubCategoriesFragment());
        // Load initial fragment
        replaceFragment(SubCategoriesFragment.newInstance(filterListener));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 0) {
//                    replaceFragment(new SubCategoriesFragment());
//                } else if (tab.getPosition() == 1) {
//                    replaceFragment(new BrandsFragment());
//                }
                if (tab.getPosition() == 0) {
                    replaceFragment(SubCategoriesFragment.newInstance(filterListener));
                } else if (tab.getPosition() == 1) {
                    replaceFragment(BrandsFragment.newInstance(filterListener));
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });


    }

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