package com.example.moofrosty.ui.newstorecreation;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moofrosty.R;
import com.example.moofrosty.core.network.Resource;
import com.example.moofrosty.core.utils.NetworkUtil;
import com.example.moofrosty.data.local.SessionManager;
import com.example.moofrosty.databinding.DialogStoreDetailsBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Latest Changes Date 24_02_2026
// GST  nonMandatory
// gst change add
public class Step4DocUploadFragment extends Fragment {

    private CreateStoreViewModel viewModel;

    private ImageView imgDoc, imgBoard, imgInside;
    private MaterialButton btnSubmit;
    private ProgressBar progressBar;

    private TextInputEditText etDocNum, etGstnNumber;
    private TextInputLayout tilDoc, gstnNumber;

    private int currentImageRequest = 0;

    private File currentPhotoFile;

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
        imgDoc.setOnClickListener(v -> {
            if (viewModel.docImage != null) {
                showImagePreviewDialog(viewModel.docImage, 1);
            } else {
                pickImage(1);
            }
        });

        imgBoard.setOnClickListener(v -> {
            if (viewModel.boardImage != null) {
                showImagePreviewDialog(viewModel.boardImage, 2);
            } else {
                pickImage(2);
            }
        });

        imgInside.setOnClickListener(v -> {
            if (viewModel.insideImage != null) {
                showImagePreviewDialog(viewModel.insideImage, 3);
            } else {
                pickImage(3);
            }
        });

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



//        etDocNum.addTextChangedListener(new TextWatcher() {
//            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                viewModel.docNumber = s.toString();
//                tilDoc.setError(null); // clear error while typing
//            }
//        });
        etDocNum.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.docNumber = s.toString();

                if (!s.toString().trim().isEmpty()) {
                    tilDoc.setError(null);
                }
            }
        });

        Log.d("viewModel.gstnnumber", "onViewCreated: "+viewModel.gstnnumber);

        // ---------- SUBMIT ----------
//        btnSubmit.setOnClickListener(v -> {
//
//            // Clear previous error
//            tilDoc.setError(null);
//            gstnNumber.setError(null);
//
//            // 1️⃣ Doc number validation
//            if (etGstnNumber.getText() == null || etGstnNumber.getText().toString().trim().isEmpty()) {
//                gstnNumber.setError("GSTN Number is  Required");
//                return;
//            }
//
//            if (etDocNum.getText() == null || etDocNum.getText().toString().trim().isEmpty()) {
//                tilDoc.setError("Doc Type  Number is Required");
//                return;
//            }
//
//            // Save (backend unchanged)
//            viewModel.gstnnumber = etGstnNumber.getText().toString().trim();
//            viewModel.docNumber = etDocNum.getText().toString().trim();
//
//            // 2️⃣ Image validation
//            if (viewModel.docImage == null ||
//                    viewModel.boardImage == null ||
//                    viewModel.insideImage == null) {
//
//                Toast.makeText(requireContext(),
//                        "Please upload all images",
//                        Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // 3️⃣ Location validation
//            if (viewModel.latLong == null || viewModel.latLong.isEmpty()) {
//                Toast.makeText(requireContext(),
//                        "Fetching location, please wait...",
//                        Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // 4️⃣ Network check
//            if (NetworkUtil.isNetworkAvailable(requireContext())) {
//                SessionManager session = new SessionManager(requireContext());
//                viewModel.submitStore(session.getToken());
//            } else {
//                Toast.makeText(requireContext(),
//                        "No Internet Connection",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
        btnSubmit.setOnClickListener(v -> {

            // Clear previous errors
            tilDoc.setError(null);


            // 2️⃣ Document number validation
//            if (etDocNum.getText() == null ||
//                    etDocNum.getText().toString().trim().isEmpty()) {
//                tilDoc.setError("Document number is required");
//                return;
//            }
            if (etDocNum.getText() == null ||
                    etDocNum.getText().toString().trim().isEmpty()) {
                tilDoc.setError("Document number is required");
                etDocNum.requestFocus();
                return;
            }

//

            // Save values (backend unchanged)
            viewModel.gstnnumber = etGstnNumber.getText().toString().trim();
            Log.d("Viewmodle.gst", "Viewmodle.gst"+viewModel.gstnnumber);
            viewModel.docNumber = etDocNum.getText().toString().trim();

            // 3️⃣ Image validation
            if (viewModel.docImage == null ||
                    viewModel.boardImage == null ||
                    viewModel.insideImage == null) {

                Toast.makeText(requireContext(),
                        "Please upload all images",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 4️⃣ Location validation
            if (viewModel.latLong == null || viewModel.latLong.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Fetching location, please wait",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 5️⃣ Network check
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                SessionManager session = new SessionManager(requireContext());
                viewModel.submitStore(session.getToken());
            } else {
                Toast.makeText(requireContext(),
                        "No internet connection",
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

//    private void showImagePreviewDialog(File imageFile, int reqCode) {
//        // Create standard dialog (Not Fullscreen)
//        Dialog dialog = new Dialog(requireContext());
//        dialog.setContentView(R.layout.storecreatedialog_image_preview);
//
//        // Make the dialog window background transparent so the rounded corners of our CardView are visible
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
//        }
//
//        ImageView imgPreview = dialog.findViewById(R.id.img_preview_fullscreen);
//        Button btnClose = dialog.findViewById(R.id.btn_close_preview);
//        Button btnRetake = dialog.findViewById(R.id.btn_retake_photo);
//
//        // Load the image into the view
//        imgPreview.setImageURI(Uri.fromFile(imageFile));
//
//        btnClose.setOnClickListener(view -> dialog.dismiss());
//
//        btnRetake.setOnClickListener(view -> {
//            dialog.dismiss();
//            pickImage(reqCode); // Open camera again for this specific image slot
//        });
//
//        dialog.show();
//    }

    private void showImagePreviewDialog(File imageFile, int reqCode) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.storecreatedialog_image_preview);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        ImageView imgPreview = dialog.findViewById(R.id.img_preview_fullscreen);
        Button btnClose = dialog.findViewById(R.id.btn_close_preview);
        Button btnRetake = dialog.findViewById(R.id.btn_retake_photo);
        TextView tvLocation = dialog.findViewById(R.id.tv_preview_location); // [HIGHLIGHT] Find the new TextView

        // Load the image into the view
        imgPreview.setImageURI(Uri.fromFile(imageFile));

        // [HIGHLIGHT] Setup Location Text Below Buttons
        if (viewModel.latLong != null && !viewModel.latLong.isEmpty()) {
            try {
                String[] parts = viewModel.latLong.split(",");
                double lat = Double.parseDouble(parts[0]);
                double lng = Double.parseDouble(parts[1]);
                String addressText = getAddressFromLatLong(lat, lng);
                String dateTime = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date());

                String locationString = addressText + "\n"
                        + "Lat: " + String.format(Locale.US, "%.6f", lat) + "  Long: " + String.format(Locale.US, "%.6f", lng) + "\n"
                        + dateTime;

                tvLocation.setText(locationString);
                tvLocation.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                tvLocation.setVisibility(View.GONE);
            }
        } else {
            tvLocation.setVisibility(View.GONE);
        }

        btnClose.setOnClickListener(view -> dialog.dismiss());

        btnRetake.setOnClickListener(view -> {
            dialog.dismiss();
            pickImage(reqCode); // Open camera again for this specific image slot
        });

        dialog.show();
    }

    private void pickImage(int reqCode) {
        currentImageRequest = reqCode;
        ImagePicker.with(this)
                .cameraOnly() // Forces default camera, ignores gallery
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

                // [HIGHLIGHT] Stamp GPS Location on the Image Before saving!
                file = addLocationWatermark(file);

                if (currentImageRequest == 1) {
                    viewModel.docImage = file;
                    imgDoc.setImageURI(Uri.fromFile(file));
                } else if (currentImageRequest == 2) {
                    viewModel.boardImage = file;
                    imgBoard.setImageURI(Uri.fromFile(file));
                } else if (currentImageRequest == 3) {
                    viewModel.insideImage = file;
                    imgInside.setImageURI(Uri.fromFile(file));
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    }

    // =========================================================================
    // [HIGHLIGHT] DRAW GPS WATERMARK LOGIC (LIKE GPS MAP CAMERA)
    // =========================================================================

    private File addLocationWatermark(File imageFile) {
        // If we don't have a location, just return the original image
        if (viewModel.latLong == null || viewModel.latLong.isEmpty()) {
            return imageFile;
        }

        try {
            // 1. Get Lat/Long and Address
            String[] parts = viewModel.latLong.split(",");
            double lat = Double.parseDouble(parts[0]);
            double lng = Double.parseDouble(parts[1]);
            String addressText = getAddressFromLatLong(lat, lng);

            // 2. Format the text to print
            String dateTime = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date());

            String locationText = addressText + "\n"
                    + "Lat " + String.format(Locale.US, "%.6f", lat) + "° Long " + String.format(Locale.US, "%.6f", lng) + "°\n"
                    + dateTime;

            // 3. Load Bitmap and make it Mutable (so we can draw on it)
            Bitmap originalBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            Bitmap mutableBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(mutableBitmap);

            // 4. Setup Paint for Text
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(mutableBitmap.getWidth() / 35f); // Scale text based on image size
            textPaint.setAntiAlias(true);
            textPaint.setShadowLayer(3f, 1f, 1f, Color.BLACK);

            // 5. Setup Paint for Semi-Transparent Background
            Paint bgPaint = new Paint();
            bgPaint.setColor(Color.parseColor("#99000000")); // Dark grey/black transparent background
            bgPaint.setStyle(Paint.Style.FILL);

            // 6. Measure Text block size
            String[] lines = locationText.split("\n");
            float textHeight = textPaint.descent() - textPaint.ascent();
            float maxTextWidth = 0;
            for (String line : lines) {
                float width = textPaint.measureText(line);
                if (width > maxTextWidth) maxTextWidth = width;
            }

            // 7. Calculate position (Bottom Left corner)
            float padding = 20f;
            float startX = 30f;
            float startY = mutableBitmap.getHeight() - (lines.length * textHeight) - padding * 3;

            // 8. Draw Background Rectangle
            canvas.drawRect(
                    startX - padding,
                    startY - textHeight,
                    startX + maxTextWidth + padding,
                    startY + (lines.length * textHeight) + padding,
                    bgPaint
            );

            // 9. Draw Text Lines
            float y = startY;
            for (String line : lines) {
                canvas.drawText(line, startX, y, textPaint);
                y += textHeight + 5f; // Small line spacing
            }

            // 10. Save the stamped image back to the file
            FileOutputStream out = new FileOutputStream(imageFile);
            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            return imageFile; // Return the freshly stamped file

        } catch (Exception e) {
            e.printStackTrace();
            return imageFile; // If anything fails, return the normal photo safely
        }
    }

    // Helper to get Address String
    private String getAddressFromLatLong(double lat, double lng) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0); // Returns full address (City, State, etc)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown Location";
    }

//    // ---------- IMAGE PICKER ----------
//    private void pickImage(int reqCode) {
//        currentImageRequest = reqCode;
//        ImagePicker.with(this)
//                .cameraOnly()
//                .compress(1024)
//                .maxResultSize(1080, 1080)
//                .start();
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK && data != null) {
//            Uri uri = data.getData();
//            if (uri != null) {
//                File file = new File(uri.getPath());
//
//                if (currentImageRequest == 1) {
//                    viewModel.docImage = file;
//                    imgDoc.setImageURI(uri);
//                } else if (currentImageRequest == 2) {
//                    viewModel.boardImage = file;
//                    imgBoard.setImageURI(uri);
//                } else if (currentImageRequest == 3) {
//                    viewModel.insideImage = file;
//                    imgInside.setImageURI(uri);
//                }
//            }
//        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(requireContext(),
//                    ImagePicker.getError(data),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
}