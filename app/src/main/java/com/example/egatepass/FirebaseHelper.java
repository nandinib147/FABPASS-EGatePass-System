package com.example.egatepass;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    public static DatabaseReference usersRef =
            FirebaseDatabase
                    .getInstance()
                    .getReference("Users");

    public static DatabaseReference requestsRef =
            FirebaseDatabase
                    .getInstance()
                    .getReference("Requests");

}