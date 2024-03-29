package com.nickli.scheduleapp.emailStaff;

import org.json.JSONArray;

// Class to handle getting information from database
public class EmailUpload {
    private String staffName;
    private String staffEmail;
    private String staffType;

    // Calls class EmailUpload and defines variables
    public EmailUpload(String staffName, String staffEmail, String staffType) {
        this.staffName = staffName;
        this.staffEmail = staffEmail;
        this.staffType = staffType;
    }
    public String getStaffName() {
        return staffName;
    }
    public String getStaffEmail() {
        return staffEmail;
    }
    public String getStaffType() {
        return staffType;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }
    public void setStaffType(String staffType) {
        this.staffType = "Employee";
    }
    public EmailUpload() {

    }
}
