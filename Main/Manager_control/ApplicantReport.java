package Main.Manager_control;

import Main.Enums.*;
public class ApplicantReport {
    private String applicantID;
    private int age;
    private MaritalStatus maritalStatus;
    private FlatType flatType;
    private String projectName;

    public ApplicantReport(String applicantID, int age, MaritalStatus maritalStatus, FlatType flatType, String projectName) {
        this.applicantID = applicantID;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.flatType = flatType;
        this.projectName = projectName;
    }

    // Getters and toString() for printing
    @Override
    public String toString() {
        return applicantID + " | " + age + " | " + maritalStatus + " | " + flatType + " | " + projectName;
    }
}