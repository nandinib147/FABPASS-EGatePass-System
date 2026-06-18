package com.example.egatepass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class WardenDashboard extends AppCompatActivity {

    TextView studentInfo;

    Button approveBtn,
            rejectBtn,
            logoutBtn;

    String currentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_warden_dashboard);

        studentInfo = findViewById(R.id.studentInfo);

        approveBtn = findViewById(R.id.approveBtn);

        rejectBtn = findViewById(R.id.rejectBtn);

        logoutBtn = findViewById(R.id.logoutBtn);

        loadRequest();

        // APPROVE BUTTON

        approveBtn.setOnClickListener(v -> {

            if(currentId.isEmpty()) {

                Toast.makeText(

                        this,

                        "No Request",

                        Toast.LENGTH_SHORT

                ).show();

                return;

            }

            FirebaseHelper.requestsRef
                    .child(currentId)
                    .child("status")
                    .setValue("Warden Approved");

            NotificationHelper helper =
                    new NotificationHelper(this);

            helper.showNotification(

                    "GatePass Approved ✅",

                    "Student request approved by Warden"

            );

            Toast.makeText(

                    this,

                    "Request Approved",

                    Toast.LENGTH_SHORT

            ).show();

            loadRequest();

        });

        // REJECT BUTTON

        rejectBtn.setOnClickListener(v -> {

            if(currentId.isEmpty()) {

                Toast.makeText(

                        this,

                        "No Request",

                        Toast.LENGTH_SHORT

                ).show();

                return;

            }

            FirebaseHelper.requestsRef
                    .child(currentId)
                    .child("status")
                    .setValue("Rejected");

            NotificationHelper helper =
                    new NotificationHelper(this);

            helper.showNotification(

                    "GatePass Rejected ❌",

                    "Student request rejected by Warden"

            );

            Toast.makeText(

                    this,

                    "Request Rejected",

                    Toast.LENGTH_SHORT

            ).show();

            loadRequest();

        });

        // LOGOUT

        logoutBtn.setOnClickListener(v -> {

            startActivity(

                    new Intent(

                            WardenDashboard.this,

                            MainActivity.class

                    )

            );

            finish();

        });

    }

    // LOAD REQUEST

    private void loadRequest() {

        FirebaseHelper.requestsRef

                .addListenerForSingleValueEvent(

                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                boolean found = false;

                                for(DataSnapshot data :
                                        snapshot.getChildren()) {

                                    RequestModel request =
                                            data.getValue(
                                                    RequestModel.class
                                            );

                                    if(request != null
                                            && request.type != null
                                            && request.status != null
                                            && request.type.equals(
                                            "Hostel GatePass"
                                    )
                                            && request.status.equals(
                                            "Pending for Warden"
                                    )) {

                                        found = true;

                                        currentId = request.id;

                                        studentInfo.setText(

                                                "ID : " + request.id +

                                                        "\n\nName : " + request.studentName +

                                                        "\n\nReason : " + request.reason +

                                                        "\n\nDate : " + request.date +

                                                        "\n\nStatus : " + request.status

                                        );

                                        break;

                                    }

                                }

                                if(!found) {

                                    currentId = "";

                                    studentInfo.setText(

                                            "No Pending Requests"

                                    );

                                }

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