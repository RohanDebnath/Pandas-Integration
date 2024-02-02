package com.example.pandasintegration;

// Student.java
public class Student {

    private String name;
    private String xMarks;
    private String xiiMarks;
    private String activities;
    private String email;

    public Student(String name, String xMarks, String xiiMarks, String activities, String email) {
        this.name = name;
        this.xMarks = xMarks;
        this.xiiMarks = xiiMarks;
        this.activities = activities;
        this.email = email;
    }

    // Add getters if necessary
    public String getName() {
        return name;
    }

    public String getXMarks() {
        return xMarks;
    }

    public String getXiiMarks() {
        return xiiMarks;
    }

    public String getActivities() {
        return activities;
    }

    public String getEmail() {
        return email;
    }
}
