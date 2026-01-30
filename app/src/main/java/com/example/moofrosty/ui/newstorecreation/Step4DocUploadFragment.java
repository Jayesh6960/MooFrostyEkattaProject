package com.example.moofrosty.ui.newstorecreation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

public class Step4DocUploadFragment extends Fragment {

    private CreateStoreViewModel viewModel;

    private ImageView imgDoc, imgBoard, imgInside;
    private MaterialButton btnSubmit;
    private ProgressBar progressBar;

    private TextInputEditText etDocNum, etGstnNumber;
    private TextInputLayout tilDoc, gstnNumber;

    private int currentImageRequest = 0;

    public Step4DocUploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step4_doc_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CreateStoreViewModel.class);

        // ---------- UI BINDING ----------
        TextView tvDocTitle = view.findViewById(R.id.tv_doc_title);

        if (viewModel.selectedDocType != null && !viewModel.selectedDocType.isEmpty()) {
            tvDocTitle.setText(viewModel.selectedDocType + " Upload");
        } else {
            tvDocTitle.setText("Document Upload");
        }

        etDocNum = view.findViewById(R.id.et_doc_number);
        tilDoc = view.findViewById(R.id.till_doc);

        etGstnNumber = view.findViewById(R.id.et_gstn_number);
        gstnNumber = view.findViewById(R.id.gstn_number);

        imgDoc = view.findViewById(R.id.img_doc);
        imgBoard = view.findViewById(R.id.img_board);
        imgInside = view.findViewById(R.id.img_inside);

        btnSubmit = view.findViewById(R.id.btn_submit);
        progressBar = view.findViewById(R.id.progress_bar);

        // ---------- IMAGE PICKERS ----------
        imgDoc.setOnClickListener(v -> pickImage(1));
        imgBoard.setOnClickListener(v -> pickImage(2));
        imgInside.setOnClickListener(v -> pickImage(3));

        // ---------- RESTORE DATA ----------
        if (viewModel.gstnnumber != null){
            etGstnNumber.setText(viewModel.gstnnumber);
        }

        if (viewModel.docNumber != null) {
            etDocNum.setText(viewModel.docNumber);
        }

        if (viewModel.docImage != null) {
            imgDoc.setImageURI(Uri.fromFile(viewModel.docImage));
        }
        if (viewModel.boardImage != null) {
            imgBoard.setImageURI(Uri.fromFile(viewModel.boardImage));
        }
        if (viewModel.insideImage != null) {
            imgInside.setImageURI(Uri.fromFile(viewModel.insideImage));
        }

        etGstnNumber.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.gstnnumber = s.toString();
                gstnNumber.setError(null); // clear error while typing
            }
        });

        etDocNum.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.docNumber = s.toString();
                tilDoc.setError(null); // clear error while typing
            }
        });


        // ---------- SUBMIT ----------
        btnSubmit.setOnClickListener(v -> {

            // Clear previous error
            tilDoc.setError(null);
            gstnNumber.setError(null);

            // 1️⃣ Doc number validation
            if (etGstnNumber.getText() == null || etGstnNumber.getText().toString().trim().isEmpty()) {
                gstnNumber.setError("GSTN Number Required");
                return;
            }

            if (etDocNum.getText() == null || etDocNum.getText().toString().trim().isEmpty()) {
                tilDoc.setError("Doc Type Required");
                return;
            }

            // Save (backend unchanged)
            viewModel.gstnnumber = etGstnNumber.getText().toString().trim();
            viewModel.docNumber = etDocNum.getText().toString().trim();

            // 2️⃣ Image validation
            if (viewModel.docImage == null ||
                    viewModel.boardImage == null ||
                    viewModel.insideImage == null) {

                Toast.makeText(requireContext(),
                        "Please upload all images",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 3️⃣ Location validation
            if (viewModel.latLong == null || viewModel.latLong.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Fetching location, please wait...",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 4️⃣ Network check
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                SessionManager session = new SessionManager(requireContext());
                viewModel.submitStore(session.getToken());
            } else {
                Toast.makeText(requireContext(),
                        "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        });

        // ---------- OBSERVER ----------
        viewModel.submitResult.observe(getViewLifecycleOwner(), res -> {
            if (res == null) return;

            if (res.status == Resource.Status.LOADING) {
                btnSubmit.setEnabled(false);
                btnSubmit.setText("Submitting...");
                progressBar.setVisibility(View.VISIBLE);

            } else if (res.status == Resource.Status.SUCCESS) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(),
                        "Store Added Successfully!",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(requireActivity(), NewStoreActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();

            } else if (res.status == Resource.Status.ERROR) {
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                btnSubmit.setText("Submit");
                Toast.makeText(requireContext(),
                        res.message,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // ---------- IMAGE PICKER ----------
    private void pickImage(int reqCode) {
        currentImageRequest = reqCode;
        ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                File file = new File(uri.getPath());

                if (currentImageRequest == 1) {
                    viewModel.docImage = file;
                    imgDoc.setImageURI(uri);
                } else if (currentImageRequest == 2) {
                    viewModel.boardImage = file;
                    imgBoard.setImageURI(uri);
                } else if (currentImageRequest == 3) {
                    viewModel.insideImage = file;
                    imgInside.setImageURI(uri);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(),
                    ImagePicker.getError(data),
                    Toast.LENGTH_SHORT).show();
        }
    }
}