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
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.data.model.LeaveTypeResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ApplyLeaveFragment extends Fragment {

    private TextInputEditText etStartDate, etEndDate, etReason;
    private AutoCompleteTextView spinnerLeaveType;
    private MaterialButton btnApply;
    private LeaveViewModel viewModel;
    private SessionManager sessionManager;
    private final Calendar calendar = Calendar.getInstance();

    private final List<LeaveTypeResponse.LeaveType> leaveTypeList = new ArrayList<>();
    private final List<String> leaveTypeNames = new ArrayList<>();

    private int
            selectedLeaveTypeId = -1;

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
//        String[] types = {"Casual Leave", "Medical Leave", "Loss of Pay"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, types);
//        spinnerLeaveType.setAdapter(adapter);

        // Date Pickers
        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        // Submit Logic
        btnApply.setOnClickListener(v -> submitLeave());

        checkNetworkAndLoadLeaveTypes();
        observeLeaveTypes();
        observeApplyLeave();

//        // Observe Result
//        viewModel.getApplyLeaveResult().observe(getViewLifecycleOwner(), resource -> {
//            if (resource != null) {
//                if (resource.status == Resource.Status.LOADING) {
//                    btnApply.setEnabled(false);
//                    btnApply.setText("Applying...");
//                } else if (resource.status == Resource.Status.SUCCESS) {
//                    btnApply.setEnabled(true);
//                    btnApply.setText("Apply Leave");
//                    Toast.makeText(requireContext(), resource.data.getMessage(), Toast.LENGTH_LONG).show();
//                    // Clear inputs on success
//                    etReason.setText("");
//                    etStartDate.setText("");
//                    etEndDate.setText("");
//                } else if (resource.status == Resource.Status.ERROR) {
//                    btnApply.setEnabled(true);
//                    btnApply.setText("Apply Leave");
//                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    private void checkNetworkAndLoadLeaveTypes() {
        if (!NetworkUtil.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(),
                    "No Internet Connection",
                    Toast.LENGTH_LONG).show();
            btnApply.setEnabled(false);
            return;
        }
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Session Expired. Please Login Again.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.fetchLeaveTypes(token);
    }

    private void observeLeaveTypes() {
        viewModel.getLeaveTypesResult().observe(getViewLifecycleOwner(), resource -> {

            if (resource == null) return;

            switch (resource.status) {

                case LOADING:
                    btnApply.setEnabled(false);
                    btnApply.setText("Loading...");
                    break;

                case SUCCESS:
                    btnApply.setEnabled(true);
                    btnApply.setText("Apply Leave");

                    leaveTypeList.clear();
                    leaveTypeNames.clear();

                    leaveTypeList.addAll(resource.data.getData());
                    for (LeaveTypeResponse.LeaveType type : leaveTypeList) {
                        leaveTypeNames.add(type.getLeaveType());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            leaveTypeNames
                    );
                    spinnerLeaveType.setAdapter(adapter);

                    spinnerLeaveType.setOnItemClickListener(
                            (parent, view, position, id) ->
                                    selectedLeaveTypeId =
                                            leaveTypeList.get(position).getLeavesId()
                    );
                    break;

                case ERROR:
                    btnApply.setEnabled(false);
                    btnApply.setText("Apply Leave");
                    String errorMessage = resource.message != null ? resource.message : "Something Error";
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void observeApplyLeave() {
        viewModel.getApplyLeaveResult().observe(getViewLifecycleOwner(), resource -> {

            if (resource == null) return;

            switch (resource.status) {

                case LOADING:
                    btnApply.setEnabled(false);
                    btnApply.setText("Applying...");
                    break;

                case SUCCESS:
                    btnApply.setEnabled(true);
                    btnApply.setText("Apply Leave");

                    Toast.makeText(requireContext(), resource.data.getMessage(), Toast.LENGTH_LONG).show();

                    // Reset form
                    etStartDate.setText("");
                    etEndDate.setText("");
                    etReason.setText("");
                    spinnerLeaveType.setText("");
                    selectedLeaveTypeId = -1;
                    break;

                case ERROR:
                    btnApply.setEnabled(true);
                    btnApply.setText("Apply Leave");
                    String errorMessage = resource.message != null ? resource.message : "Something Error";
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }


//    private void showDatePicker(TextInputEditText editText) {
//        new DatePickerDialog(requireContext(),R.style.CustomDatePickerTheme, (view, year, month, dayOfMonth) -> {
//            calendar.set(year, month, dayOfMonth);
//            String format = "yyyy-MM-dd";
//            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
//            editText.setText(sdf.format(calendar.getTime()));
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//    }

//    private void showDatePicker(TextInputEditText editText) {
//
//        Calendar currentCal = Calendar.getInstance();
//
//        DatePickerDialog dialog = new DatePickerDialog(
//                requireContext(),
//                R.style.CustomDatePickerTheme,
//                (view, year, month, day) -> {
//                    calendar.set(year, month, day);
//                    SimpleDateFormat sdf =
//                            new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//                    editText.setText(sdf.format(calendar.getTime()));
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//        );
//        dialog.setOnShowListener(d -> {
//            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
//                    .setTextColor(getResources().getColor(R.color.Purple_Color));
//
//            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
//                    .setTextColor(getResources().getColor(R.color.Purple_Color));
//        });
//        dialog.show();
//    }

    private void showDatePicker(TextInputEditText editText) {


        if (editText.getId() == R.id.et_end_date) {
            String startDateStr = etStartDate.getText().toString().trim();

            // If Start Date is empty, stop and show Toast
            if (startDateStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please select Start Date first", Toast.LENGTH_SHORT).show();
                return; // EXIT FUNCTION HERE
            }
        }
        // Use current date as default for the picker view
        Calendar currentCal = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                R.style.CustomDatePickerTheme,
                (view, year, month, day) -> {
                    // Use a local calendar instance to avoid messing up global state
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, day);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    editText.setText(sdf.format(selectedDate.getTime()));
                },
                currentCal.get(Calendar.YEAR),
                currentCal.get(Calendar.MONTH),
                currentCal.get(Calendar.DAY_OF_MONTH)
        );

        // --- LOGIC START: Restrict End Date ---
        // If the clicked field is the END DATE, check if we have a Start Date
        if (editText.getId() == R.id.et_end_date) {
            String startDateStr = etStartDate.getText().toString(); // Get text from Start Date field

            if (!startDateStr.isEmpty()) {
                try {
                    // Parse the string "yyyy-MM-dd" back to a Date object
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    Date date = sdf.parse(startDateStr);

                    if (date != null) {
                        // Set the Minimum Date for the picker
                        // This disables all previous dates, allowing "Same Day" or "Future"
                        dialog.getDatePicker().setMinDate(date.getTime());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Optional: If Start Date is empty, maybe restrict to today?
                // dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }
        }
        // --- LOGIC END ---

        dialog.setOnShowListener(d -> {
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.Purple_Color));

            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.Purple_Color));
        });

        dialog.show();
    }

    private void submitLeave() {

        if (!NetworkUtil.isNetworkAvailable(requireContext())) {
            Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String start = etStartDate.getText().toString().trim();
        String end = etEndDate.getText().toString().trim();
        String reason = etReason.getText().toString().trim();

        if (selectedLeaveTypeId == -1 ||
                start.isEmpty() ||
                end.isEmpty() ||
                reason.isEmpty()) {

            Toast.makeText(requireContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return;
        }
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Session Expired. Please Login Again.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.applyLeave(token, String.valueOf(selectedLeaveTypeId), start, end, reason
        );
    }

//    private void submitLeave() {
//        String typeText = spinnerLeaveType.getText().toString();
//        String start = etStartDate.getText().toString();
//        String end = etEndDate.getText().toString();
//        String reason = etReason.getText().toString();
//
//        if (typeText.isEmpty() || start.isEmpty() || end.isEmpty() || reason.isEmpty()) {
//            Toast.makeText(requireContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        // Map text to ID (1, 2, 3)
//        String typeId = "1";
//        if (typeText.equals("Medical Leave")) typeId = "2";
//        else if (typeText.equals("Loss of Pay")) typeId = "3";
//        // GET TOKEN FROM SESSION
//        String token = sessionManager.getToken();
//        if (token.isEmpty()) {
//            Toast.makeText(requireContext(), "Session Expired. Please Login Again.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        viewModel.applyLeave(token, typeId, start, end, reason);
//    }
}