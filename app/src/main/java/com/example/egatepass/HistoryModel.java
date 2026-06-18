package com.example.egatepass;

public class HistoryModel {

    String reason,
            date,
            status;

    public HistoryModel(

            String reason,
            String date,
            String status

    ) {

        this.reason = reason;

        this.date = date;

        this.status = status;

    }

    public String getReason() {

        return reason;

    }

    public String getDate() {

        return date;

    }

    public String getStatus() {

        return status;

    }

}