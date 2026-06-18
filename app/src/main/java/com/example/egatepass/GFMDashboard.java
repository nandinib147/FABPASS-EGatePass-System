package com.example.egatepass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class GFMDashboard extends AppCompatActivity {

    TextView studentInfo;

    Button approveBtn,
            rejectBtn,
            logoutBtn;

    MaterialCardView requestCard;

    SessionManager sessionManager;

    String currentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gfmdashboard);

        studentInfo = findViewById(R.id.studentInfo);

        approveBtn = findViewById(R.id.approveBtn);

        rejectBtn = findViewById(R.id.rejectBtn);

        logoutBtn = findViewById(R.id.logoutBtn);

        requestCard = findViewById(R.id.requestCard);

        sessionManager = new SessionManager(this);

        loadRequest();

        // APPROVE

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
                    .setValue("GFM Approved");

            NotificationHelper helper =
                    new NotificationHelper(this);

            helper.showNotification(

                    "GFM Approved ✅",

                    "Request approved by GFM"

            );

            Toast.makeText(

                    this,

                    "Request Approved",

                    Toast.LENGTH_SHORT

            ).show();

            loadRequest();

        });

        // REJECT

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

                    "Request Rejected ❌",

                    "Request rejected by GFM"

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

                            GFMDashboard.this,

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
                                            && request.status != null
                                            && request.department != null
                                            && request.year != null
                                            && request.division != null

                                            && request.department.equals(
                                            sessionManager.getDepartment()
                                    )

                                            && request.year.equals(
                                            sessionManager.getYear()
                                    )

                                            && request.division.equals(
                                            sessionManager.getDivision()
                                    )

                                            && (

                                            request.status.equals(
                                                    "Pending for GFM"
                                            )

                                                    || request.status.equals(
                                                    "Warden Approved"
                                            )

                                    )

                                    ){

                                        found = true;

                                        currentId = request.id;

                                        String currentStatus;

                                        if(request.status.equals(
                                                "Pending for GFM"
                                        )) {

                                            currentStatus =
                                                    "Pending for GFM";

                                        }

                                        else if(request.status.equals(
                                                "Warden Approved"
                                        )) {

                                            currentStatus =
                                                    "Pending for GFM";

                                        }

                                        else {

                                            currentStatus =
                                                    request.status;

                                        }

                                        requestCard.setVisibility(
                                                View.VISIBLE
                                        );

                                        studentInfo.setText(

                                                "Request ID : " + request.id +

                                                        "\n\nStudent Name : " + request.studentName +

                                                        "\n\nReason : " + request.reason +

                                                        "\n\nDate : " + request.date +

                                                        "\n\nCurrent Status : " + currentStatus

                                        );

                                        break;

                                    }

                                }

                                if(!found) {

                                    currentId = "";

                                    requestCard.setVisibility(
                                            View.GONE
                                    );

                                }

                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                Toast.makeText(

                                        GFMDashboard.this,

                                        "Firebase Error",

                                        Toast.LENGTH_SHORT

                                ).show();

                            }

                        }

                );

    }

}