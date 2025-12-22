package com.example.moofrosty.ui.attendance.leave;

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
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.moofrosty.R;

import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.local.SessionManager;


public class LeaveHistoryFragment extends Fragment {

    private LeaveViewModel viewModel;
    private RecyclerView recyclerView;
    private LeaveHistoryAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SessionManager sessionManager;

    public LeaveHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leave_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());
        viewModel = new ViewModelProvider(this).get(LeaveViewModel.class);

        recyclerView = view.findViewById(R.id.recycler_history);
        progressBar = view.findViewById(R.id.progress_bar);
        tvEmpty = view.findViewById(R.id.tv_empty);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // GET TOKEN AND FETCH HISTORY
        String token = sessionManager.getToken();
        if (!token.isEmpty()) {
            viewModel.fetchHistory(token);
        } else {
            tvEmpty.setText("Please login to view history.");
            tvEmpty.setVisibility(View.VISIBLE);
        }

        viewModel.getHistoryResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                if (resource.status == Resource.Status.LOADING) {
                    progressBar.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                } else if (resource.status == Resource.Status.SUCCESS) {
                    progressBar.setVisibility(View.GONE);
                    if (resource.data != null && resource.data.getData() != null && !resource.data.getData().isEmpty()) {
                        adapter = new LeaveHistoryAdapter(resource.data.getData());
                        recyclerView.setAdapter(adapter);
                        tvEmpty.setVisibility(View.GONE);
                    } else {
                        tvEmpty.setText("No Leave History Found");
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    tvEmpty.setText(resource.message);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}