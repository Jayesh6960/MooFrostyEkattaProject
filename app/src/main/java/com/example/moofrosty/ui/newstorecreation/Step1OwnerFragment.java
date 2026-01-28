package com.example.moofrosty.ui.newstorecreation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moofrosty.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class Step1OwnerFragment extends Fragment {

    private CreateStoreViewModel viewModel;
    private TextInputEditText etOwner, etEmail, etMobile;

    public Step1OwnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step1_owner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CreateStoreViewModel.class);

        etOwner = view.findViewById(R.id.et_owner_name);
        etEmail = view.findViewById(R.id.et_email);
        etMobile = view.findViewById(R.id.et_mobile);
        MaterialButton btnNext = view.findViewById(R.id.btn_next);

        // Pre-fill mobile number if passed from previous activity

        // When you come back, the ViewModel still holds the text. Put it back in the box.
        if (viewModel.ownerName != null) {
            etOwner.setText(viewModel.ownerName);
        }
        if (viewModel.email != null) {
            etEmail.setText(viewModel.email);
        }
        if (viewModel.mobileNumber != null) {
            etMobile.setText(viewModel.mobileNumber);
            etMobile.setEnabled(false);
        }

        btnNext.setOnClickListener(v -> {
            String name = etOwner.getText().toString();
            String email = etEmail.getText().toString();
            etOwner.setError(null);
            if(name.isEmpty() ) {

                etOwner.setError("OwnerFull Name Required");
                return;
            }
//            if(email.isEmpty() ) {
//                etOwner.setError("OwnerFull Name Required");
//                return;
//            }
            viewModel.ownerName = name;
            viewModel.email = email;
            ((CreateStoreWizardActivity) requireActivity()).nextStep();
        });
    }
}