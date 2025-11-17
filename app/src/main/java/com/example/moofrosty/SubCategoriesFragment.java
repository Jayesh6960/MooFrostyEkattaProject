package com.example.moofrosty;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SubCategoriesFragment extends Fragment {

    public SubCategoriesFragment() {
        // Required empty public constructor
    }

    public static SubCategoriesFragment newInstance(FilterSelectionListener listener) {
        SubCategoriesFragment fragment = new SubCategoriesFragment();
        fragment.listener = listener;
        return fragment;
    }

    private FilterSelectionListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sub_categories, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSub);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<SubCategories> list = new ArrayList<>();
        list.add(new SubCategories(R.drawable.conecategory, "Big Cones"));
        list.add(new SubCategories(R.drawable.kulficategory, "Kulfi"));
        list.add(new SubCategories(R.drawable.cupcategory, "Cups"));
        list.add(new SubCategories(R.drawable.familypacktwo, "Family Pack"));
        list.add(new SubCategories(R.drawable.magnunkulficategory, "Magnum kulfi"));
        list.add(new SubCategories(R.drawable.familypackcategory, "Family Pack Big"));
        list.add(new SubCategories(R.drawable.familypackone, "Family Pack Small"));
        list.add(new SubCategories(R.drawable.miniconecategory, "Mini Cone"));
        list.add(new SubCategories(R.drawable.familypacktwo, "Party Pack"));

        recyclerView.setAdapter(new SubCategoriesAdaptor(list,listener));
    }
}