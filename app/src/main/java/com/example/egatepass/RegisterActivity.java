 package com.example.egatepass;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText fullName,
            mobileNo,
            address,
            parentMobile,
            prnNo,
            regUsername,
            regPassword;

    Spinner deptSpinner,
            yearSpinner,
            divSpinner,
            regRole;

    Button registerBtn,
            uploadBtn;

    ImageView profileImage;

    Uri imageUri;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_activity);

        // EDITTEXTS

        fullName = findViewById(R.id.fullName);

        mobileNo = findViewById(R.id.mobileNo);

        address = findViewById(R.id.address);

        parentMobile = findViewById(R.id.parentMobile);

        prnNo = findViewById(R.id.prnNo);

        regUsername = findViewById(R.id.regUsername);

        regPassword = findViewById(R.id.regPassword);

        // SPINNERS

        deptSpinner = findViewById(R.id.deptSpinner);

        yearSpinner = findViewById(R.id.yearSpinner);

        divSpinner = findViewById(R.id.divSpinner);

        regRole = findViewById(R.id.regRole);

        // BUTTONS

        registerBtn = findViewById(R.id.registerBtn);

        uploadBtn = findViewById(R.id.uploadBtn);

        // IMAGE

        profileImage = findViewById(R.id.profileImage);

        // DATABASE

        dbHelper = new DBHelper(this);

        // DEPARTMENT SPINNER

        String[] departments = {

                "Department",

                "CSE",

                "ENTC",

                "MECH",

                "CIVIL",

                "AIML"

        };

        ArrayAdapter<String> deptAdapter =
                new ArrayAdapter<>(

                        this,

                        R.layout.spinner_item,

                        departments

                );

        deptAdapter.setDropDownViewResource(

                android.R.layout.simple_spinner_dropdown_item

        );

        deptSpinner.setAdapter(deptAdapter);

        // YEAR SPINNER

        String[] years = {

                "Year",

                "FE",

                "SE",

                "TE",

                "BE"

        };

        ArrayAdapter<String> yearAdapter =
                new ArrayAdapter<>(

                        this,

                        R.layout.spinner_item,

                        years

                );

        yearAdapter.setDropDownViewResource(

                android.R.layout.simple_spinner_dropdown_item

        );

        yearSpinner.setAdapter(yearAdapter);

        // DIVISION SPINNER

        String[] divisions = {

                "Division",

                "A",

                "B",

                "C"

        };

        ArrayAdapter<String> divAdapter =
                new ArrayAdapter<>(

                        this,

                        R.layout.spinner_item,

                        divisions

                );

        divAdapter.setDropDownViewResource(

                android.R.layout.simple_spinner_dropdown_item

        );

        divSpinner.setAdapter(divAdapter);

        // ROLE SPINNER

        String[] roles = {

                "Role",

                "Student",

                "Warden",

                "GFM",

                "HOD",

                "Security"

        };

        ArrayAdapter<String> roleAdapter =
                new ArrayAdapter<>(

                        this,

                        R.layout.spinner_item,

                        roles

                );

        roleAdapter.setDropDownViewResource(

                android.R.layout.simple_spinner_dropdown_item

        );

        regRole.setAdapter(roleAdapter);

        // PHOTO UPLOAD

        uploadBtn.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    );

            startActivityForResult(intent, 1);

        });

        // REGISTER BUTTON

        registerBtn.setOnClickListener(v -> {

            String username =
                    regUsername.getText().toString();

            String password =
                    regPassword.getText().toString();

            String role =
                    regRole.getSelectedItem().toString();

            // STUDENT REGISTER

            if(role.equals("Student")) {

                String full =
                        fullName.getText().toString();

                String mobile =
                        mobileNo.getText().toString();

                String addr =
                        address.getText().toString();

                String parent =
                        parentMobile.getText().toString();

                String prn =
                        prnNo.getText().toString();

                String dept =
                        deptSpinner.getSelectedItem().toString();

                String year =
                        yearSpinner.getSelectedItem().toString();

                String div =
                        divSpinner.getSelectedItem().toString();

                String photo = "";

                if(imageUri != null) {

                    photo = imageUri.toString();

                }

                // VALIDATION

                if(full.isEmpty()
                        || mobile.isEmpty()
                        || addr.isEmpty()
                        || parent.isEmpty()
                        || prn.isEmpty()
                        || username.isEmpty()
                        || password.isEmpty()
                        || imageUri == null) {

                    Toast.makeText(
                            this,
                            "Fill all fields",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;

                }

                UserModel user =

                        new UserModel(

                                username,
                                password,
                                role,
                                full,
                                mobile,
                                addr,
                                parent,
                                dept,
                                year,
                                div,
                                prn,
                                photo

                        );

                FirebaseHelper.usersRef
                        .child(username)
                        .setValue(user)

                        .addOnSuccessListener(unused -> {

                            Toast.makeText(

                                    this,

                                    "Student Account Created",

                                    Toast.LENGTH_SHORT

                            ).show();

                            finish();

                        })

                        .addOnFailureListener(e -> {

                            Toast.makeText(

                                    this,

                                    "Firebase Error",

                                    Toast.LENGTH_SHORT

                            ).show();

                        });

            }

            // ADMIN REGISTER

            else {

                if(username.isEmpty()
                        || password.isEmpty()) {

                    Toast.makeText(
                            this,
                            "Fill Username & Password",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;

                }

                UserModel user =

                        new UserModel(

                                username,
                                password,
                                role,

                                fullName.getText().toString(),

                                mobileNo.getText().toString(),

                                address.getText().toString(),

                                parentMobile.getText().toString(),

                                deptSpinner.getSelectedItem().toString(),

                                yearSpinner.getSelectedItem().toString(),

                                divSpinner.getSelectedItem().toString(),

                                prnNo.getText().toString(),

                                imageUri != null
                                        ? imageUri.toString()
                                        : ""

                        );

                FirebaseHelper.usersRef
                        .child(username)
                        .setValue(user)

                        .addOnSuccessListener(unused -> {

                            Toast.makeText(

                                    this,

                                    "Admin Account Created",

                                    Toast.LENGTH_SHORT

                            ).show();

                            finish();

                        })

                        .addOnFailureListener(e -> {

                            Toast.makeText(

                                    this,

                                    "Firebase Error",

                                    Toast.LENGTH_SHORT

                            ).show();

                        });

            }

        });

    }

    // IMAGE RESULT

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if(requestCode == 1
                && resultCode == RESULT_OK
                && data != null) {

            imageUri = data.getData();

            profileImage.setImageURI(imageUri);

        }

    }

}

