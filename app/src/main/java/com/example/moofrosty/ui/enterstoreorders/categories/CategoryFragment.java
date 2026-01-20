package com.example.moofrosty.ui.enterstoreorders.categories;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.ui.enterstoreorders.takeorder.CategoryViewModel;
import com.example.moofrosty.ui.filter.CategorySelectionListener;


public class CategoryFragment extends Fragment {

    private CategorySelectionListener listener;
    private CategoryViewModel viewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    public CategoryFragment() {
        // Required empty public constructor
    }

    // Standard static factory method
    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Connect listener to Parent Fragment (BottomSheet)
        if (getParentFragment() instanceof CategorySelectionListener) {
            listener = (CategorySelectionListener) getParentFragment();
        } else {
            throw new RuntimeException("Parent fragment must implement CategorySelectionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        String token = sessionManager.getToken();

        recyclerView = view.findViewById(R.id.recycler_categories);
        progressBar = view.findViewById(R.id.progressBarCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Fetch Data
        if (NetworkUtil.isNetworkAvailable(getContext())) {
            viewModel.fetchCategories(token);
        } else {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }

        // Observe Data
        viewModel.getCategories().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    if (resource.data != null && resource.data.data != null) {
                        CategoryAdaptor adapter = new CategoryAdaptor(resource.data.data, listener);
                        recyclerView.setAdapter(adapter);
                    }

                    if (listener != null) {
                        listener.onDefaultCategoryLoaded(resource.data.data.get(0));
                    }
                    break;
                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}