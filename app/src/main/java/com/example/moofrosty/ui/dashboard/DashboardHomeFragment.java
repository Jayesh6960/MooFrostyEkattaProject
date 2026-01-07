package com.example.moofrosty.ui.dashboard;

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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.data.model.DashboardItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DashboardHomeFragment extends Fragment {

    private DashboardViewModel viewModel;
    private List<DashboardItem> currentListForDetail = new ArrayList<>();

    // UI Elements
    private TextView tvMocDropdown, tvTotalIncentives, tvViewMore;
    private RecyclerView recyclerView;


    public DashboardHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvMocDropdown = view.findViewById(R.id.tv_moc_dropdown);
        tvTotalIncentives = view.findViewById(R.id.tv_total_incentives);
        tvViewMore = view.findViewById(R.id.tv_view_more);
        recyclerView = view.findViewById(R.id.dashboard_recycler);

        // 2. Setup ViewModel
        // Using requireActivity() allows data to survive configuration changes
        viewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

//        // 3. Setup RecyclerView
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(gridLayoutManager);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        // 4. Observers
        viewModel.getDashboardItems().observe(getViewLifecycleOwner(), items -> {
            currentListForDetail = items;
            DashboardAdapter adapter = new DashboardAdapter(items);
            recyclerView.setAdapter(adapter);
        });

        viewModel.getTotalIncentives().observe(getViewLifecycleOwner(), value -> {
            if (tvTotalIncentives != null) tvTotalIncentives.setText(value);
        });

        // 5. Dropdown Logic
        tvMocDropdown.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), v);
            popup.getMenu().add("MOC 01 (01 Jan - 31 Jan)");
            popup.getMenu().add("MOC 12 (01 Dec - 31 Dec)");
            popup.getMenu().add("MOC 11 (01 Nov - 30 Nov)");

            popup.setOnMenuItemClickListener(menuItem -> {
                String fullTitle = menuItem.getTitle().toString();
                String shortTitle = fullTitle.split("\\(")[0].trim();
                tvMocDropdown.setText(shortTitle);
                viewModel.loadData(shortTitle);
                return true;
            });
            popup.show();
        });

        // 6. View More Logic
        tvViewMore.setOnClickListener(v -> {
            if (currentListForDetail == null || currentListForDetail.isEmpty()) {
                Toast.makeText(requireContext(), "No data to show", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(requireContext(), ViewMoreDetailsActivity.class);
            intent.putExtra("DATA_LIST", (Serializable) currentListForDetail);
            intent.putExtra("TITLE", tvMocDropdown.getText().toString());
            startActivity(intent);
        });
    }
}