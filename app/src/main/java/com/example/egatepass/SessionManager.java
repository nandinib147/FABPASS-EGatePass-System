package com.example.egatepass;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences sp;

    SharedPreferences.Editor editor;

    public SessionManager(Context context){

        sp = context.getSharedPreferences(
                "LoginSession",
                Context.MODE_PRIVATE
        );

        editor = sp.edit();

    }

    // SAVE USER DATA

    public void saveUser(

            String username,
            String fullName,
            String department,
            String year,
            String division,
            String role,
            String photo

    ){

        editor.putString("username", username);

        editor.putString("fullName", fullName);

        editor.putString("department", department);

        editor.putString("year", year);

        editor.putString("division", division);

        editor.putString("role", role);

        editor.putString("photo", photo);

        editor.apply();

    }

    // USERNAME

    public String getUser(){

        return sp.getString(
                "username",
                ""
        );

    }

    // FULL NAME

    public String getFullName(){

        return sp.getString(
                "fullName",
                ""
        );

    }

    // DEPARTMENT

    public String getDepartment(){

        return sp.getString(
                "department",
                ""
        );

    }

    // YEAR

    public String getYear(){

        return sp.getString(
                "year",
                ""
        );

    }

    // DIVISION

    public String getDivision(){

        return sp.getString(
                "division",
                ""
        );

    }

    // ROLE

    public String getRole(){

        return sp.getString(
                "role",
                ""
        );

    }

    // PHOTO

    public String getPhoto(){

        return sp.getString(
                "photo",
                ""
        );

    }

    // LOGOUT

    public void logout(){

        editor.clear();

        editor.apply();

    }

}