package com.example.egatepass;

public class UserModel {

    public String username,
            password,
            role,
            fullName,
            mobile,
            address,
            parentMobile,
            department,
            year,
            division,
            prn,
            photo;

    public UserModel() {

    }

    public UserModel(

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

        this.username = username;

        this.password = password;

        this.role = role;

        this.fullName = fullName;

        this.mobile = mobile;

        this.address = address;

        this.parentMobile = parentMobile;

        this.department = department;

        this.year = year;

        this.division = division;

        this.prn = prn;

        this.photo = photo;

    }

}