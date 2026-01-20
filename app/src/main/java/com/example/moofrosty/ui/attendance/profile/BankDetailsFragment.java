package com.example.moofrosty.ui.attendance.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.model.UserDetailResponse;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;


public class BankDetailsFragment extends Fragment {
    public BankDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bank_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserDetailViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(UserDetailViewModel.class);

        TextInputEditText bank = view.findViewById(R.id.etBankName);
        TextInputEditText branch = view.findViewById(R.id.etBranchName);
        TextInputEditText acc = view.findViewById(R.id.etAccountNumber);
        TextInputEditText confirm = view.findViewById(R.id.etConfirmAccountNumber);
        TextInputEditText ifsc = view.findViewById(R.id.etIFSCCode);
        MaterialAutoCompleteTextView type = view.findViewById(R.id.spinnerAccountType);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        viewModel.getUserDetail().observe(getViewLifecycleOwner(), resource -> {

            switch (resource.status) {

                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;

                case SUCCESS:
                    progressBar.setVisibility(View.GONE);

                    if (resource.data != null && resource.data.getData() != null &&
                            resource.data.getData().getBankDetail() != null) {

                        UserDetailResponse.Data.BankDetail b = resource.data.getData().getBankDetail();
                        bank.setText(b.getBankname());
                        branch.setText(b.getBranchName());
                        acc.setText(b.getAccountNumber());
                        confirm.setText(b.getConfirmAccountNumber());
                        ifsc.setText(b.getIFSCCode());
                        type.setText(b.getAccountType(), false);
                    }
                    break;

                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        });
    }
}