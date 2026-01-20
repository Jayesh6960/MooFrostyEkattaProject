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

import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.ui.enterstoreorders.takeorder.CategoryViewModel;
import com.example.moofrosty.ui.filter.FilterSelectionListener;
import com.example.moofrosty.R;
import com.example.moofrosty.data.model.SubCategories;

import java.util.ArrayList;

public class SubCategoriesFragment extends Fragment {

    private static final String ARG_CAT_ID = "cat_id";
    private int selectedCategoryId;
    private FilterSelectionListener listener;
    private CategoryViewModel viewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    public SubCategoriesFragment() {
        // Required empty public constructor
    }

    // Pass ID via Bundle (Safe way)
    public static SubCategoriesFragment newInstance(int categoryId) {
        SubCategoriesFragment fragment = new SubCategoriesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CAT_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedCategoryId = getArguments().getInt(ARG_CAT_ID, 0);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Connect listener to Parent (BottomSheet) which gets it from Activity
        if (getParentFragment() instanceof FilterSelectionListener) {
            listener = (FilterSelectionListener) getParentFragment();
        } else {
            // Optional: You can check context (Activity) if not in parent fragment
            throw new RuntimeException("Parent must implement FilterSelectionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        String token = sessionManager.getToken();

        recyclerView = view.findViewById(R.id.recyclerViewSub);
        progressBar = view.findViewById(R.id.progressBarSub);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        if (selectedCategoryId == 0) {
            Toast.makeText(getContext(), "Please select a Category first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (NetworkUtil.isNetworkAvailable(getContext())) {
            viewModel.fetchSubCategories(token, selectedCategoryId);
        } else {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }

        viewModel.getSubCategories().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    if (resource.data != null && resource.data.subcategoryData != null) {
                        SubCategoriesAdaptor adapter = new SubCategoriesAdaptor(resource.data.subcategoryData, listener);
                        recyclerView.setAdapter(adapter);
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

    //  static code


//    public SubCategoriesFragment() {
//        // Required empty public constructor
//    }
//    public static SubCategoriesFragment newInstance(FilterSelectionListener listener) {
//        SubCategoriesFragment fragment = new SubCategoriesFragment();
//        fragment.listener = listener;
//        return fragment;
//    }
//    private FilterSelectionListener listener;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) { //fixed the class name as the camalcase and do the development
//        View view = inflater.inflate(R.layout.fragment_sub_categories, container, false);
//        return view;
//    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSub);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        ArrayList<SubCategories> list = new ArrayList<>();//Subcategories  name modle created
//        list.add(new SubCategories(R.drawable.amulfruitandnut, "Ice Cream"));
//        list.add(new SubCategories(R.drawable.conecategory, "Big Cones"));
//        list.add(new SubCategories(R.drawable.kulficategory, "Kulfi"));
//        list.add(new SubCategories(R.drawable.cupcategory, "Cups"));
//        list.add(new SubCategories(R.drawable.familypacktwo, "Family Pack"));
//        list.add(new SubCategories(R.drawable.magnunkulficategory, "Magnum kulfi"));
//        list.add(new SubCategories(R.drawable.familypackcategory, "Family Pack Big"));
//        list.add(new SubCategories(R.drawable.familypackone, "Family Pack Small"));
//        list.add(new SubCategories(R.drawable.miniconecategory, "Mini Cone"));
//        list.add(new SubCategories(R.drawable.familypacktwo, "Party Pack"));
//        recyclerView.setAdapter(new SubCategoriesAdaptor(list,listener));
//    }
