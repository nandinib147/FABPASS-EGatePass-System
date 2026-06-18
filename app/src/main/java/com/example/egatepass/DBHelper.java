package com.example.egatepass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {

        super(context, "GatePassDB", null, 7);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // USERS TABLE

        db.execSQL(

                "CREATE TABLE users(" +

                        "username TEXT PRIMARY KEY," +

                        "password TEXT," +

                        "role TEXT," +

                        "fullName TEXT," +

                        "mobile TEXT," +

                        "address TEXT," +

                        "parentMobile TEXT," +

                        "department TEXT," +

                        "year TEXT," +

                        "division TEXT," +

                        "prn TEXT," +

                        "photo TEXT" +

                        ")"

        );

        // DEFAULT USERS

        db.execSQL(
                "INSERT INTO users(username,password,role,department) VALUES('student','123','Student','Computer')"
        );

        db.execSQL(
                "INSERT INTO users(username,password,role,department) VALUES('warden','123','Warden','Computer')"
        );

        db.execSQL(
                "INSERT INTO users(username,password,role,department) VALUES('gfm','123','GFM','Computer')"
        );

        db.execSQL(
                "INSERT INTO users(username,password,role,department) VALUES('hod','123','HOD','Computer')"
        );

        db.execSQL(
                "INSERT INTO users(username,password,role,department) VALUES('security','123','Security','Computer')"
        );

        // REQUESTS TABLE

        db.execSQL(

                "CREATE TABLE requests(" +

                        "id TEXT PRIMARY KEY," +

                        "studentName TEXT," +

                        "reason TEXT," +

                        "date TEXT," +

                        "status TEXT," +

                        "department TEXT," +

                        "type TEXT" +

                        ")"

        );

    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        // KEEP OLD DATA

    }

    // INSERT REQUEST

    public boolean insertRequest(

            String id,
            String name,
            String reason,
            String datetime,
            String department,
            String type

    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put("id", id);

        cv.put("studentName", name);

        cv.put("reason", reason);

        cv.put("date", datetime);

        cv.put("department", department);

        cv.put("type", type);

        // STATUS FLOW

        if(type.equals("Hostel GatePass")) {

            cv.put(
                    "status",
                    "Pending for Warden"
            );

        }

        else {

            cv.put(
                    "status",
                    "Pending for GFM"
            );

        }

        long result =
                db.insert(
                        "requests",
                        null,
                        cv
                );

        return result != -1;

    }

    // UPDATE STATUS

    public void updateStatus(
            String id,
            String status
    ){

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put("status", status);

        db.update(

                "requests",

                cv,

                "id=?",

                new String[]{id}

        );

    }

    // GET STATUS

    public String getStatusById(String id){

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(

                        "SELECT status FROM requests WHERE id=?",

                        new String[]{id}

                );

        if(cursor.moveToFirst()){

            return cursor.getString(0);

        }

        return "Not Found";

    }

    // LOGIN CHECK

    public boolean checkLogin(

            String username,
            String password,
            String role

    ){

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(

                        "SELECT * FROM users WHERE username=? AND password=? AND role=?",

                        new String[]{

                                username,
                                password,
                                role

                        }

                );

        return cursor.getCount() > 0;

    }

    // REGISTER USER

    public boolean registerUser(

            String username,
            String password,
            String role,
            String fullName,
            String mobile,
            String address,
            String parentMobile,
            String department,
            String year,
            String division,
            String prn,
            String photo

    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        Cursor cursor =
                db.rawQuery(

                        "SELECT * FROM users WHERE username=?",

                        new String[]{username}

                );

        if(cursor.getCount() > 0) {

            return false;

        }

        ContentValues cv =
                new ContentValues();

        cv.put("username", username);

        cv.put("password", password);

        cv.put("role", role);

        cv.put("fullName", fullName);

        cv.put("mobile", mobile);

        cv.put("address", address);

        cv.put("parentMobile", parentMobile);

        cv.put("department", department);

        cv.put("year", year);

        cv.put("division", division);

        cv.put("prn", prn);

        cv.put("photo", photo);

        long result =
                db.insert(
                        "users",
                        null,
                        cv
                );

        return result != -1;

    }

    // GET USER REQUESTS

    public Cursor getUserRequests(
            String username
    ){

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(

                "SELECT * FROM requests WHERE studentName=?",

                new String[]{username}

        );

    }

    // WARDEN REQUESTS

    public Cursor getPendingWardenRequests() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(

                "SELECT * FROM requests WHERE type=? AND status=?",

                new String[]{

                        "Hostel GatePass",
                        "Pending for Warden"

                }

        );

    }

    // GFM/HOD DEPARTMENT REQUESTS

    public Cursor getDepartmentRequests(
            String department
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(

                "SELECT * FROM requests " +
                        "WHERE department=?",

                new String[]{

                        department

                }

        );

    }

    // USER DATA

    public Cursor getUserData(
            String username
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(

                "SELECT * FROM users WHERE username=?",

                new String[]{username}

        );

    }

}