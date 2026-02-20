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
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.UserDetailResponse;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

//Latest Updated Date :-28-01-2026
public class DetailsFragment extends Fragment {

    // Text fields
    private TextInputEditText etFirstName, etMiddleName, etLastName, etITeamsCode,
            etAddress, etEmail, etDob, etDateOfJoin, etMobile, etPastExperience;

    // Experience fields
    private MaterialAutoCompleteTextView spinnerGender, spinnerEducation,
            spinnerExperienceYear, spinnermonths;

    private SessionManager sessionManager;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

//        initViews(view);
//
//        // ✅ SessionManager unchanged
//        sessionManager = new SessionManager(requireContext());
//
//        // Load user data
//        loadUserFromSession();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        UserDetailViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(UserDetailViewModel.class);

        TextInputEditText etFirstName = view.findViewById(R.id.etFirstName);
        TextInputEditText etMiddleName = view.findViewById(R.id.etMiddleName);
        TextInputEditText etLastName = view.findViewById(R.id.etLastName);
        TextInputEditText etITeamsCode = view.findViewById(R.id.etITeamsCode);
        TextInputEditText etAddress = view.findViewById(R.id.IteamsCode);
        TextInputEditText etEmail = view.findViewById(R.id.etEmail);
        TextInputEditText etDob = view.findViewById(R.id.etDob);
        TextInputEditText etJoin = view.findViewById(R.id.dateofjoin);
        TextInputEditText etMobile = view.findViewById(R.id.etMobile);
        TextInputEditText etPastExp = view.findViewById(R.id.etPastExperience);
        MaterialAutoCompleteTextView gender = view.findViewById(R.id.spinnerGender);
        MaterialAutoCompleteTextView education = view.findViewById(R.id.SpinnerspEducation);
        MaterialAutoCompleteTextView year = view.findViewById(R.id.spinnerExperienceYear);
        MaterialAutoCompleteTextView month = view.findViewById(R.id.spinnermonths);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        viewModel.getUserDetail().observe(getViewLifecycleOwner(), resource -> {

            switch (resource.status) {

                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;

                case SUCCESS:
                    progressBar.setVisibility(View.GONE);

                    if (resource.data != null && resource.data.getData() != null) {
                        UserDetailResponse.Data d = resource.data.getData();

                        etFirstName.setText(d.getFirstName());
                        etMiddleName.setText(d.getMiddleName());
                        etLastName.setText(d.getLastName());
                        etITeamsCode.setText(d.getIteamsCode());
                        etAddress.setText(d.getAddress());
                        etEmail.setText(d.getEmail());
                        etDob.setText(d.getDateofBirth());
                        etJoin.setText(d.getDateofJoining());
                        etMobile.setText(d.getMobileNumber());
                        etPastExp.setText(d.getPastEmployer());

                        gender.setText(d.getGender(), false);
                        education.setText(d.getEducation(), false);
                        year.setText(d.getExperienceYears(), false);
                        month.setText(d.getExperienceMonths(), false);
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

        viewModel.loadUserDetail(sessionManager.getToken());
    }

}