package com.example.moofrosty.ui.newstorecreation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

public class Step4DocUploadFragment extends Fragment {

    private CreateStoreViewModel viewModel;
    private ImageView imgDoc, imgBoard, imgInside;
    private int currentImageRequest = 0;
    private MaterialButton btnSubmit;

    public Step4DocUploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step4_doc_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CreateStoreViewModel.class);

        TextView tvDocTitle = view.findViewById(R.id.tv_doc_title);
        // Safety check for null
        if (viewModel.selectedDocType != null && !viewModel.selectedDocType.isEmpty()) {
            tvDocTitle.setText(viewModel.selectedDocType + " Upload");
        } else {
            tvDocTitle.setText("Document Upload");
        }

        TextInputEditText etDocNum = view.findViewById(R.id.et_doc_number);
        imgDoc = view.findViewById(R.id.img_doc);
        imgBoard = view.findViewById(R.id.img_board);
        imgInside = view.findViewById(R.id.img_inside);
        btnSubmit = view.findViewById(R.id.btn_submit);

        imgDoc.setOnClickListener(v -> pickImage(1));
        imgBoard.setOnClickListener(v -> pickImage(2));
        imgInside.setOnClickListener(v -> pickImage(3));

        btnSubmit.setOnClickListener(v -> {
            if (etDocNum.getText() != null) {
                viewModel.docNumber = etDocNum.getText().toString();
            }

            // Mock Lat/Lng for now (Use FusedLocationProvider for real gps)
           // viewModel.latLong = "19.876,75.343";

            if(viewModel.docImage == null || viewModel.boardImage == null || viewModel.insideImage == null) {
                Toast.makeText(requireContext(), "Please upload all images", Toast.LENGTH_SHORT).show();
                return;
            }

            if (viewModel.latLong == null || viewModel.latLong.isEmpty()) {
                Toast.makeText(requireContext(), "Fetching location, please wait...", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Check Network
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                SessionManager session = new SessionManager(requireContext());
                viewModel.submitStore(session.getToken());
            } else {
                Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

        viewModel.submitResult.observe(getViewLifecycleOwner(), res -> {
            if(res != null) {
                // Use Resource.Status if Status is inner enum, or Status if standalone
                if(res.status == Resource.Status.LOADING) {
                    btnSubmit.setText("Submitting...");
                    btnSubmit.setEnabled(false);
                } else if(res.status == Resource.Status.SUCCESS) {
                    Toast.makeText(requireContext(), "Store Added Successfully!", Toast.LENGTH_LONG).show();
                    requireActivity().finish();
                } else if(res.status == Resource.Status.ERROR) {
                    btnSubmit.setText("Submit");
                    btnSubmit.setEnabled(true);
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void pickImage(int reqCode) {
        currentImageRequest = reqCode;
        ImagePicker.with(this)
                .crop()
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
                // This library returns a Uri that points to a file, so getPath() is safe here
                File file = new File(uri.getPath());

                if(currentImageRequest == 1) {
                    viewModel.docImage = file;
                    imgDoc.setImageURI(uri);
                } else if(currentImageRequest == 2) {
                    viewModel.boardImage = file;
                    imgBoard.setImageURI(uri);
                } else if(currentImageRequest == 3) {
                    viewModel.insideImage = file;
                    imgInside.setImageURI(uri);
                }
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    }
}