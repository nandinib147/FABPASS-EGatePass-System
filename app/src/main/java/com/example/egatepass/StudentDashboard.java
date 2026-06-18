package com.example.egatepass;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class StudentDashboard extends AppCompatActivity {

    EditText studentName,
            reason;

    Spinner typeSpinner;

    Button requestBtn,
            logoutBtn;

    ImageView qrImage,
            profilePic;

    TextView requestDetails,
            welcomeName,
            deptText;

    RecyclerView historyRecycler;

    ArrayList<HistoryModel> list;

    HistoryAdapter adapter;

    SessionManager sessionManager;

    String currentUser;

    Button profileBtn;

    DatabaseReference requestRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_dashboard);

        studentName = findViewById(R.id.studentName);

        reason = findViewById(R.id.reason);

        typeSpinner = findViewById(R.id.typeSpinner);

        requestBtn = findViewById(R.id.requestBtn);

        logoutBtn = findViewById(R.id.logoutBtn);

        qrImage = findViewById(R.id.qrImage);

        requestDetails = findViewById(R.id.requestDetails);

        profilePic = findViewById(R.id.profilePic);

        welcomeName = findViewById(R.id.welcomeName);

        deptText = findViewById(R.id.deptText);

        profileBtn = findViewById(R.id.profileBtn);

        historyRecycler = findViewById(R.id.historyRecycler);

        historyRecycler.setLayoutManager(
                new LinearLayoutManager(this)
        );

        requestRef =
                FirebaseDatabase
                        .getInstance()
                        .getReference("Requests");

        sessionManager = new SessionManager(this);

        currentUser = sessionManager.getUser();

        // SPINNER TYPES

        String[] types = {

                "Hostel GatePass",

                "College GatePass"

        };

        ArrayAdapter<String> typeAdapter =
                new ArrayAdapter<>(

                        this,

                        R.layout.spinner_item,

                        types

                );

        typeAdapter.setDropDownViewResource(

                android.R.layout.simple_spinner_dropdown_item

        );

        typeSpinner.setAdapter(typeAdapter);

        // PROFILE BUTTON

        profileBtn.setOnClickListener(v -> {

            startActivity(

                    new Intent(

                            StudentDashboard.this,

                            ProfileActivity.class

                    )

            );

        });

        // PROFILE DATA

        String fullName =
                sessionManager.getFullName();

        String department =
                sessionManager.getDepartment();

        String photo =
                sessionManager.getPhoto();

        welcomeName.setText(
                "Welcome, " + fullName
        );

        deptText.setText(
                "Department : " + department
        );

        if(photo != null && !photo.isEmpty()) {

            try {

                profilePic.setImageURI(
                        Uri.parse(photo)
                );

            }

            catch (Exception e) {

                profilePic.setImageResource(
                        R.drawable.student_icon
                );

            }

        }

        // HISTORY

        list = new ArrayList<>();

        adapter = new HistoryAdapter(list);

        historyRecycler.setAdapter(adapter);

        loadData();

        // LOGOUT

        logoutBtn.setOnClickListener(v -> {

            sessionManager.logout();

            startActivity(

                    new Intent(

                            StudentDashboard.this,

                            MainActivity.class

                    )

            );

            finish();

        });

        // CREATE REQUEST

        requestBtn.setOnClickListener(v -> {

            String name =
                    sessionManager.getFullName();

            String issue =
                    reason.getText().toString().trim();

            String selectedType =
                    typeSpinner.getSelectedItem().toString();

            if(issue.isEmpty()) {

                Toast.makeText(

                        this,

                        "Fill all fields",

                        Toast.LENGTH_SHORT

                ).show();

                return;

            }

            Random random =
                    new Random();

            String requestId =

                    "R" +

                            (100000 + random.nextInt(900000));

            String dateTime =

                    new SimpleDateFormat(

                            "dd/MM/yyyy HH:mm:ss",

                            Locale.getDefault()

                    ).format(new Date());

            // FINAL STATUS

            final String status;

            if(selectedType.equals("Hostel GatePass")) {

                status = "Pending for Warden";

            }

            else {

                status = "Pending for GFM";

            }

            // QR DATA

            String qrData =

                    "ID: " + requestId +

                            "\nName: " + name +

                            "\nReason: " + issue +

                            "\nType: " + selectedType +

                            "\nDateTime: " + dateTime;

            // FIREBASE REQUEST

            RequestModel request =

                    new RequestModel(

                            requestId,

                            name,

                            sessionManager.getUser(),

                            issue,

                            dateTime,

                            status,

                            sessionManager.getDepartment(),

                            sessionManager.getYear(),

                            sessionManager.getDivision(),

                            selectedType

                    );

            requestRef
                    .child(requestId)
                    .setValue(request)

                    .addOnSuccessListener(unused -> {

                        try {

                            BarcodeEncoder barcodeEncoder =
                                    new BarcodeEncoder();

                            BitMatrix bitMatrix =
                                    barcodeEncoder.encode(

                                            qrData,

                                            BarcodeFormat.QR_CODE,

                                            400,

                                            400

                                    );

                            Bitmap bitmap =
                                    barcodeEncoder.createBitmap(bitMatrix);

                            qrImage.setImageBitmap(bitmap);

                            requestDetails.setText(

                                    "ID: " + requestId +

                                            "\nName: " + name +

                                            "\nReason: " + issue +

                                            "\nType: " + selectedType +

                                            "\nDate & Time: " + dateTime +

                                            "\nStatus: " + status

                            );

                            loadData();

                            Toast.makeText(

                                    StudentDashboard.this,

                                    "GatePass Generated",

                                    Toast.LENGTH_SHORT

                            ).show();

                        }

                        catch (WriterException e) {

                            e.printStackTrace();

                        }

                    })

                    .addOnFailureListener(e -> {

                        Toast.makeText(

                                StudentDashboard.this,

                                "Firebase Error",

                                Toast.LENGTH_SHORT

                        ).show();

                    });

        });

    }

    // LOAD HISTORY

    private void loadData() {

        FirebaseHelper.requestsRef

                .addListenerForSingleValueEvent(

                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                list.clear();

                                for(DataSnapshot data :
                                        snapshot.getChildren()) {

                                    RequestModel request =
                                            data.getValue(
                                                    RequestModel.class
                                            );

                                    if(request != null
                                            && request.studentName != null
                                            && request.studentName.equals(
                                            sessionManager.getFullName()
                                    )) {

                                        list.add(

                                                new HistoryModel(

                                                        request.reason,

                                                        request.date,

                                                        request.status

                                                )

                                        );

                                    }

                                }

                                adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                            }

                        }

                );

    }

}