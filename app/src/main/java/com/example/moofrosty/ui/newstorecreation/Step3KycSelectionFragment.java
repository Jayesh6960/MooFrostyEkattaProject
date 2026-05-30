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
import android.util.Log;
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
    private boolean isReturningFromSettings = false;


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
        // --- 🔥 FIX: RESTORE SELECTION ---
        if (viewModel.selectedDocType != null) {
            String type = viewModel.selectedDocType;
            Log.d("Selectedtypes", "selctedtypes"+type);
            if (type.equals("Udyam Aadhaar")) radioGroup.check(R.id.rb_udyam);
            else if (type.equals("Aadhaar Card")) radioGroup.check(R.id.rb_aadhar);
//            else if (type.equals("GST  ")) radioGroup.check(R.id.rb_gst);
            else if (type.equals("FSSAI License")) radioGroup.check(R.id.rb_fssai);
            else if (type.equals("PAN Card")) radioGroup.check(R.id.rb_pan);
            else if (type.equals("Drug License")) radioGroup.check(R.id.rb_druglicense);
        }

        btnNext.setOnClickListener(v -> {

            int selectedId = radioGroup.getCheckedRadioButtonId();
            if(selectedId == -1) {
                Toast.makeText(requireContext(), "Select a Document Type", Toast.LENGTH_SHORT).show();
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
            RadioButton rb = view.findViewById(selectedId);
            viewModel.selectedDocType = rb.getText().toString();
            Log.d("selectedDocType", "onViewCreated: "+viewModel.selectedDocType);
            ((CreateStoreWizardActivity) requireActivity()).nextStep();
        });


    }

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