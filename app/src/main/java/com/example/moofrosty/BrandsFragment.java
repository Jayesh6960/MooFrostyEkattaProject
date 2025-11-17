package com.example.moofrosty;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class BrandsFragment extends Fragment {
    private RecyclerView recyclerView;
    private BrandsAdapter adapter;
    private List<BrandModel> brandList;
    private FilterSelectionListener listener;
    public BrandsFragment() {

    }
    public static BrandsFragment newInstance(FilterSelectionListener listener) {
        BrandsFragment fragment = new BrandsFragment();
        fragment.listener = listener;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_brands, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewBrands);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        // Initialize data
        brandList = new ArrayList<>();
        brandList.add(new BrandModel(R.drawable.amulbrand, "Amul"));
        brandList.add(new BrandModel(R.drawable.cornettobrand, "Cornetto"));
        brandList.add(new BrandModel(R.drawable.magnumbrand, "magnum"));
        brandList.add(new BrandModel(R.drawable.arunbrand, "arun"));
        brandList.add(new BrandModel(R.drawable.kwalitywall, "kwalitywall"));
        brandList.add(new BrandModel(R.drawable.motherdairybrand, "motherdairy"));
        brandList.add(new BrandModel(R.drawable.havmorebrand, "havemore"));
        brandList.add(new BrandModel(R.drawable.vadinalbrand, "Vadinal"));
        adapter = new BrandsAdapter(brandList,listener);
        recyclerView.setAdapter(adapter);
    }
}