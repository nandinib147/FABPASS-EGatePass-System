package com.example.egatepass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Spinner roleSpinner;

    Button loginBtn;

    EditText username,
            password;

    TextView registerText;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        roleSpinner = findViewById(R.id.roleSpinner);

        loginBtn = findViewById(R.id.loginBtn);

        username = findViewById(R.id.username);

        password = findViewById(R.id.password);

        registerText = findViewById(R.id.registerText);

        sessionManager = new SessionManager(this);

        // ROLE SPINNER

        String[] roles = {

                "Role",

                "Student",

                "Warden",

                "GFM",

                "HOD",

                "Security"

        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(

                        this,

                        R.layout.spinner_item,

                        roles

                );

        adapter.setDropDownViewResource(

                android.R.layout.simple_spinner_dropdown_item

        );

        roleSpinner.setAdapter(adapter);

        // REGISTER PAGE

        registerText.setOnClickListener(v -> {

            startActivity(

                    new Intent(

                            MainActivity.this,

                            RegisterActivity.class

                    )

            );

        });

        // LOGIN BUTTON

        loginBtn.setOnClickListener(v -> {

            String user =
                    username.getText().toString().trim();

            String pass =
                    password.getText().toString().trim();

            String role =
                    roleSpinner.getSelectedItem().toString();

            if(user.isEmpty()
                    || pass.isEmpty()
                    || role.equals("Role")) {

                Toast.makeText(

                        this,

                        "Fill all fields",

                        Toast.LENGTH_SHORT

                ).show();

                return;

            }

            FirebaseHelper.usersRef
                    .child(user)

                    .addListenerForSingleValueEvent(

                            new ValueEventListener() {

                                @Override
                                public void onDataChange(
                                        @NonNull DataSnapshot snapshot
                                ) {

                                    if(snapshot.exists()) {

                                        UserModel model =
                                                snapshot.getValue(
                                                        UserModel.class
                                                );

                                        if(model != null
                                                && model.password.equals(pass)
                                                && model.role.equals(role)) {

                                            sessionManager.saveUser(

                                                    user,

                                                    model.fullName,

                                                    model.department,

                                                    model.year,

                                                    model.division,

                                                    model.role,

                                                    model.photo

                                            );

                                            Toast.makeText(

                                                    MainActivity.this,

                                                    "Login Success",

                                                    Toast.LENGTH_SHORT

                                            ).show();

                                            // OPEN DASHBOARD

                                            if(role.equals("Student")) {

                                                startActivity(

                                                        new Intent(

                                                                MainActivity.this,

                                                                StudentDashboard.class

                                                        )

                                                );

                                            }

                                            else if(role.equals("Warden")) {

                                                startActivity(

                                                        new Intent(

                                                                MainActivity.this,

                                                                WardenDashboard.class

                                                        )

                                                );

                                            }

                                            else if(role.equals("GFM")) {

                                                startActivity(

                                                        new Intent(

                                                                MainActivity.this,

                                                                GFMDashboard.class

                                                        )

                                                );

                                            }

                                            else if(role.equals("HOD")) {

                                                startActivity(

                                                        new Intent(

                                                                MainActivity.this,

                                                                HODDashboard.class

                                                        )

                                                );

                                            }

                                            else if(role.equals("Security")) {

                                                startActivity(

                                                        new Intent(

                                                                MainActivity.this,

                                                                SecurityDashboard.class

                                                        )

                                                );

                                            }

                                        }

                                        else {

                                            Toast.makeText(

                                                    MainActivity.this,

                                                    "Invalid Credentials",

                                                    Toast.LENGTH_SHORT

                                            ).show();

                                        }

                                    }

                                    else {

                                        Toast.makeText(

                                                MainActivity.this,

                                                "User Not Found",

                                                Toast.LENGTH_SHORT

                                        ).show();

                                    }

                                }

                                @Override
                                public void onCancelled(
                                        @NonNull DatabaseError error
                                ) {

                                    Toast.makeText(

                                            MainActivity.this,

                                            "Firebase Error",

                                            Toast.LENGTH_SHORT

                                    ).show();

                                }

                            }

                    );

        });

    }

}