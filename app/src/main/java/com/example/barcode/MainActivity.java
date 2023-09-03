package com.example.barcode;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> scanBarcodeLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        ImageView imageView = findViewById(R.id.imageView);
        TextView textViewResult = findViewById(R.id.textViewResult);

        scanBarcodeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            Bitmap imageBitmap = ImageUtil.loadImageFromUri(imageUri, this);
                            if (imageBitmap != null) {
                                imageView.setImageBitmap(imageBitmap);
                                scanBarcodeFromBitmap(imageBitmap, textViewResult);
                            }
                        }
                    }
                });

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            scanBarcodeLauncher.launch(intent);
        });
    }

    private void scanBarcodeFromBitmap(Bitmap bitmap, TextView textViewResult) {
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_CODE_128,
                        Barcode.FORMAT_EAN_13,
                        Barcode.FORMAT_CODABAR,
                        Barcode.FORMAT_CODE_39,
                        Barcode.FORMAT_CODE_93,
                        Barcode.FORMAT_EAN_8,
                        Barcode.FORMAT_ITF,
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC,
                        Barcode.FORMAT_DATA_MATRIX,
                        Barcode.FORMAT_PDF417,
                        Barcode.FORMAT_UPC_A,
                        Barcode.FORMAT_UPC_E
                )
                .build();

        InputImage image = InputImage.fromBitmap(bitmap, 0);

        BarcodeScanner scanner = BarcodeScanning.getClient(options);


        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    StringBuilder resultText = new StringBuilder();
                    for (Barcode barcode : barcodes) {
                        String code = barcode.getDisplayValue();
                        resultText.append("Código detectado: ").append(code).append("\n");
                    }
                    textViewResult.setText(resultText.toString());
                })
                .addOnFailureListener(e -> {
                    textViewResult.setText("No se pudo detectar ningún código.");
                });
    }
}