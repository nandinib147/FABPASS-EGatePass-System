package com.example.egatepass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class SecurityDashboard extends AppCompatActivity {

    Button scanBtn,
            uploadBtn,
            checkBtn,
            logoutBtn;

    TextView resultText;

    EditText manualCode;

    ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_security_dashboard
        );

        scanBtn =
                findViewById(R.id.scanBtn);

        uploadBtn =
                findViewById(R.id.uploadBtn);

        checkBtn =
                findViewById(R.id.checkBtn);

        logoutBtn =
                findViewById(R.id.logoutBtn);

        resultText =
                findViewById(R.id.resultText);

        manualCode =
                findViewById(R.id.manualCode);

        // QR SCAN

        scanBtn.setOnClickListener(v -> {

            IntentIntegrator integrator =
                    new IntentIntegrator(
                            SecurityDashboard.this
                    );

            integrator.setPrompt(
                    "Scan Student QR"
            );

            integrator.setOrientationLocked(false);

            integrator.initiateScan();

        });

        // IMAGE PICKER

        imagePickerLauncher =
                registerForActivityResult(

                        new ActivityResultContracts.GetContent(),

                        uri -> {

                            if(uri != null) {

                                resultText.setText(

                                        "✅ QR Image Uploaded Successfully"

                                );

                            }

                        }

                );

        uploadBtn.setOnClickListener(v -> {

            imagePickerLauncher.launch(
                    "image/*"
            );

        });

        // MANUAL CHECK

        checkBtn.setOnClickListener(v -> {

            String id =
                    manualCode.getText()
                            .toString()
                            .trim();

            if(id.isEmpty()) {

                resultText.setText(
                        "Enter Request ID"
                );

                return;

            }

            checkGatePass(id);

        });

        // LOGOUT

        logoutBtn.setOnClickListener(v -> {

            startActivity(

                    new Intent(

                            SecurityDashboard.this,

                            MainActivity.class

                    )

            );

            finish();

        });

    }

    // QR RESULT

    @Override
    protected void onActivityResult(

            int requestCode,
            int resultCode,
            Intent data

    ) {

        IntentResult result =
                IntentIntegrator.parseActivityResult(

                        requestCode,
                        resultCode,
                        data

                );

        if(result != null) {

            if(result.getContents() != null) {

                String qrData =
                        result.getContents();

                String[] lines =
                        qrData.split("\n");

                String requestId =
                        lines[0]
                                .replace("ID: ", "")
                                .trim();

                checkGatePass(requestId);

            }

        }

        else {

            super.onActivityResult(

                    requestCode,
                    resultCode,
                    data

            );

        }

    }

    // CHECK GATEPASS

    private void checkGatePass(String requestId) {

        FirebaseHelper.requestsRef
                .child(requestId)

                .addListenerForSingleValueEvent(

                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                if(snapshot.exists()) {

                                    RequestModel request =
                                            snapshot.getValue(
                                                    RequestModel.class
                                            );

                                    if(request != null
                                            && request.status != null) {

                                        if(request.status.equals(
                                                "Final Approved"
                                        )) {

                                            resultText.setText(

                                                    "✅ ACCESS GRANTED\n\n"

                                                            + "Request ID : "

                                                            + requestId +

                                                            "\n\nStudent : "

                                                            + request.studentName +

                                                            "\n\nStatus : "

                                                            + request.status

                                            );

                                        }

                                        else {

                                            resultText.setText(

                                                    "❌ ACCESS DENIED\n\n"

                                                            + "Request ID : "

                                                            + requestId +

                                                            "\n\nStatus : "

                                                            + request.status

                                            );

                                        }

                                    }

                                }

                                else {

                                    resultText.setText(

                                            "❌ INVALID REQUEST ID"

                                    );

                                }

                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                resultText.setText(

                                        "Firebase Error"

                                );

                            }

                        }

                );

    }

}