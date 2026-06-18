package com.example.egatepass;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;

    TextView fullName,
            username,
            department,
            year,
            division,
            prn,
            mobile,
            parentMobile;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profileImage);

        fullName = findViewById(R.id.fullName);

        username = findViewById(R.id.username);

        department = findViewById(R.id.department);

        year = findViewById(R.id.year);

        division = findViewById(R.id.division);

        prn = findViewById(R.id.prn);

        mobile = findViewById(R.id.mobile);

        parentMobile = findViewById(R.id.parentMobile);

        sessionManager = new SessionManager(this);

        String currentUser =
                sessionManager.getUser();

        FirebaseHelper.usersRef
                .child(currentUser)

                .addListenerForSingleValueEvent(

                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                if(snapshot.exists()) {

                                    UserModel user =
                                            snapshot.getValue(
                                                    UserModel.class
                                            );

                                    if(user != null) {

                                        fullName.setText(
                                                user.fullName
                                        );

                                        username.setText(
                                                user.username
                                        );

                                        mobile.setText(
                                                user.mobile
                                        );

                                        parentMobile.setText(
                                                user.parentMobile
                                        );

                                        department.setText(
                                                user.department
                                        );

                                        year.setText(
                                                user.year
                                        );

                                        division.setText(
                                                user.division
                                        );

                                        prn.setText(
                                                user.prn
                                        );

                                        if(user.photo != null
                                                && !user.photo.isEmpty()) {

                                            try {

                                                profileImage.setImageURI(

                                                        Uri.parse(
                                                                user.photo
                                                        )

                                                );

                                            }

                                            catch (Exception e) {

                                                profileImage.setImageResource(

                                                        R.drawable.student_icon

                                                );

                                            }

                                        }

                                    }

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