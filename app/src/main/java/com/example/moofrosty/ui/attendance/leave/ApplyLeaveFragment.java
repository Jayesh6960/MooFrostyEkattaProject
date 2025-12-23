package com.example.moofrosty.ui.attendance.leave;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource.Status;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.data.local.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ApplyLeaveFragment extends Fragment {

    private TextInputEditText etStartDate, etEndDate, etReason;
    private AutoCompleteTextView spinnerLeaveType;
    private MaterialButton btnApply;
    private LeaveViewModel viewModel;
    private SessionManager sessionManager;
    private final Calendar calendar = Calendar.getInstance();

    public ApplyLeaveFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_leave, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize Session Manager
        sessionManager = new SessionManager(requireContext());
        viewModel = new ViewModelProvider(this).get(LeaveViewModel.class);

        etStartDate = view.findViewById(R.id.et_start_date);
        etEndDate = view.findViewById(R.id.et_end_date);
        etReason = view.findViewById(R.id.et_reason);
        spinnerLeaveType = view.findViewById(R.id.spinner_leave_type);
        btnApply = view.findViewById(R.id.btn_apply);

        // Setup Spinner
        String[] types = {"Casual Leave", "Medical Leave", "Loss of Pay"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, types);
        spinnerLeaveType.setAdapter(adapter);

        // Date Pickers
        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        // Submit Logic
        btnApply.setOnClickListener(v -> submitLeave());

        // Observe Result
        viewModel.getApplyLeaveResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                if (resource.status == Resource.Status.LOADING) {
                    btnApply.setEnabled(false);
                    btnApply.setText("Applying...");
                } else if (resource.status == Resource.Status.SUCCESS) {
                    btnApply.setEnabled(true);
                    btnApply.setText("Apply Leave");
                    Toast.makeText(requireContext(), resource.data.getMessage(), Toast.LENGTH_LONG).show();
                    // Clear inputs on success
                    etReason.setText("");
                    etStartDate.setText("");
                    etEndDate.setText("");
                } else if (resource.status == Resource.Status.ERROR) {
                    btnApply.setEnabled(true);
                    btnApply.setText("Apply Leave");
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showDatePicker(TextInputEditText editText) {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            String format = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            editText.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void submitLeave() {
        String typeText = spinnerLeaveType.getText().toString();
        String start = etStartDate.getText().toString();
        String end = etEndDate.getText().toString();
        String reason = etReason.getText().toString();

        if (typeText.isEmpty() || start.isEmpty() || end.isEmpty() || reason.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        // Map text to ID (1, 2, 3)
        String typeId = "1";
        if (typeText.equals("Medical Leave")) typeId = "2";
        else if (typeText.equals("Loss of Pay")) typeId = "3";

        // GET TOKEN FROM SESSION
        String token = sessionManager.getToken();

        if (token.isEmpty()) {
            Toast.makeText(requireContext(), "Session Expired. Please Login Again.", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.applyLeave(token, typeId, start, end, reason);
    }
}