package com.example.moofrosty.ui.newstorecreation;

import static androidx.core.location.LocationManagerCompat.isLocationEnabled;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.ui.splash.BaseActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class Step1OwnerFragment extends Fragment {

    private CreateStoreViewModel viewModel;
    private TextInputEditText etOwner, etEmail, etMobile;
    private TextInputLayout tillowner;
    private SessionManager sessionManager;
    private boolean isReturningFromSettings = false;
    TextView tvLocation;




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

        sessionManager = new SessionManager(requireContext());

        etOwner = view.findViewById(R.id.et_owner_name);
        etEmail = view.findViewById(R.id.et_email);
        etMobile = view.findViewById(R.id.et_mobile);
        MaterialButton btnNext = view.findViewById(R.id.btn_next);
        tillowner=view.findViewById(R.id.tillowner);
//        tvLocation =view.findViewById(R.id.tv_preview_location);


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

            if(name.isEmpty() ) {

                tillowner.setError("Owner name is required");
                return;
            }
            if (!isLocationEnabled(requireContext())) {
                Toast.makeText(requireContext(),
                        "Please enable location",
                        Toast.LENGTH_LONG).show();

                isReturningFromSettings = true; // ✅ mark

                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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
//        tvLocation.setVisibility(View.VISIBLE);
//        tvLocation.setOnClickListener(v -> {
//            openMapWithLocation();
//        });
    }



    //instead of teh fecthong the new location  we have to extedn the beloewb metods in teh code /
    private boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        try {
            locationMode = Settings.Secure.getInt(
                    context.getContentResolver(),
                    Settings.Secure.LOCATION_MODE
            );
        } catch (Settings.SettingNotFoundException e) {
            throw new RuntimeException(e);
        }

        //    private void redirectToBaseActivity() {
//        Intent intent = new Intent(getContext(), BaseActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }
//private void redirectToBaseActivity() {
//    Intent intent = new Intent(requireActivity(), BaseActivity.class);
//    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//    startActivity(intent);
//    requireActivity().finish(); // optional but recommended
//}
        if (locationMode == Settings.Secure.LOCATION_MODE_OFF) {
            return false;
        }
        return true;
    }
}