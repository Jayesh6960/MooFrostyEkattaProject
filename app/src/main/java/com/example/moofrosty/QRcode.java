package com.example.moofrosty;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRcode extends Fragment {
    //QR Code Updated

    TextView scanResult;
    // QR scanner launcher
    private final ActivityResultLauncher<ScanOptions> barLauncher =
            registerForActivityResult(new ScanContract(), result -> {

                if (result.getContents() != null) {

                    String formattedResult = result.getContents()
                            .replace(".", ".\n")
                            .replace(";", ";\n")
                            .replace(",", ",\n")
                            .replace(":", " : ");

                    scanResult.setText("Scan Result:\n" + formattedResult);

                } else {

                    scanResult.setText("No QR code detected");
                }
            });

    public QRcode() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_q_rcode, container, false);
        // assign view
        scanResult = view.findViewById(R.id.scan_result);
        // automatically start QR scanner
        scanCode();
        return view;
    }
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume Up = Flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);
    }
}
