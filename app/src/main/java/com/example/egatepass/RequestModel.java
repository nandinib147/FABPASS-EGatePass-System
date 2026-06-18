package com.example.egatepass;

public class RequestModel {

    public String id,
            studentName,
            username,
            reason,
            date,
            status,
            department,
            year,
            division,
            type;

    public RequestModel() {

    }

    public RequestModel(

            String id,
            String studentName,
            String username,
            String reason,
            String date,
            String status,
            String department,
            String year,
            String division,
            String type

    ) {

        this.id = id;

        this.studentName = studentName;

        this.username = username;

        this.reason = reason;

        this.date = date;

        this.status = status;

        this.department = department;

        this.year = year;

        this.division = division;

        this.type = type;

    }

}