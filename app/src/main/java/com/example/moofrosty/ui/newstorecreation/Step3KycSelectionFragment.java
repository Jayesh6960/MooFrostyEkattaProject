package com.example.moofrosty.ui.newstorecreation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.google.android.material.button.MaterialButton;

public class Step3KycSelectionFragment extends Fragment {

    private CreateStoreViewModel viewModel;
    private RadioGroup radioGroup;


    public Step3KycSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step3_kyc_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CreateStoreViewModel.class);
        radioGroup = view.findViewById(R.id.radio_group_kyc);
        MaterialButton btnNext = view.findViewById(R.id.btn_next);

        btnNext.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if(selectedId == -1) {
                Toast.makeText(requireContext(), "Select a Document Type", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton rb = view.findViewById(selectedId);
            viewModel.selectedDocType = rb.getText().toString();
            ((CreateStoreWizardActivity) requireActivity()).nextStep();
        });
    }
}