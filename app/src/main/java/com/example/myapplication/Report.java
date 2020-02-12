package com.example.myapplication;

public class Report {

    private String type_of_crime;
    private String description;
    private String status;


    public Report(String type_of_crime, String description, String status){
        this.type_of_crime=type_of_crime;
        this.description=description;
        this.status=status;
    }

    public Report() {

    }

    public String getType_of_crime() {
        return type_of_crime;
    }

    public void setType_of_crime(String type_of_crime) {
        this.type_of_crime = type_of_crime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
