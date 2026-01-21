package com.example.moofrosty.ui.enterstoreorders.categories;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof CategorySelectionListener) {
            listener = (CategorySelectionListener) getParentFragment();
        } else {
            throw new RuntimeException("Parent fragment must implement CategorySelectionListener");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init Views
        recyclerView = view.findViewById(R.id.recycler_categories);
        progressBar = view.findViewById(R.id.progressBarCategories);

        // Safety check (prevents NPE forever)
        if (recyclerView == null) {
            throw new RuntimeException("RecyclerView not found in fragment_category.xml");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        sessionManager = new SessionManager(requireContext());
        String token = sessionManager.getToken();

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Fetch categories
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            viewModel.fetchCategories(token);
        } else {
            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }

        // Observe result
        viewModel.getCategories().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;

                case SUCCESS:
                    progressBar.setVisibility(View.GONE);

                    if (resource.data != null &&
                            resource.data.data != null &&
                            !resource.data.data.isEmpty()) {

                        CategoryAdaptor adapter =
                                new CategoryAdaptor(resource.data.data, listener);
                        recyclerView.setAdapter(adapter);

                        // Default selection
                        if (listener != null) {
                            listener.onDefaultCategoryLoaded(resource.data.data.get(0));
                        }
                    }
                    break;

                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(),
                            resource.message != null ? resource.message : "Something went wrong",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
