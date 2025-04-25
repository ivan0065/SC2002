package Main.Manager_control;

import Main.BTO.BTOProject;
import Main.BTO.FlatList;
import Main.BTO.ProjectDatabase;
import Main.Enums.FlatType;
import Main.Personnel.Applicant;

// Must be in this package to allow only classes in this pacakage to modify status of application
public class BTOApplication {
    private String applicationId;
    private String applicantId; // NRIC of applicant
    private String projectName;
    private String applicationStatus; // "PENDING", "SUCCESSFUL", "UNSUCCESSFUL", "BOOKED"
    private FlatType flatType;
    private String applicationDate;
    private String withdrawalRequestStatus; // "PENDING", "APPROVED", "REJECTED", null
    
    // Constructor
    public BTOApplication(String applicantId, String projectId, FlatType flatType) {
        // Note: applicationId is not set in constructor anymore
        this.applicationId = "APP-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.applicantId = applicantId;
        this.projectName = projectId;
        this.flatType = flatType;
        this.applicationStatus = "PENDING"; // Default status upon creation
        this.applicationDate = java.time.LocalDate.now().toString();
        this.withdrawalRequestStatus = null; // No withdrawal request initially
    }
    
    // Overloaded constructor that includes applicationId
    public BTOApplication(String applicationId, String applicantId, String projectId, FlatType flatType) {
        this(applicantId, projectId, flatType); // Call the other constructor
        this.applicationId = applicationId;
    }
    
    // Getters
    public String getApplicationId() {
        return applicationId;
    }
    
    // New setter for applicationId
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    public String getApplicantId() {
        return applicantId;
    }
    
    public String getProjectId() {
        return projectName;
    }
    
    public String getApplicationStatus() {
        return applicationStatus;
    }
    
    public FlatType getFlatType() {
        return flatType;
    }
    
    public String getApplicationDate() {
        return applicationDate;
    }
    
    public String getWithdrawalRequestStatus() {
        return withdrawalRequestStatus;
    }
    
    // These methods are package-private (default access modifier)
    // so that only classes in the same package can modify the status
    void setApplicationStatus(String status) {
        this.applicationStatus = status;
    }
    
    void setWithdrawalRequestStatus(String status) {
        this.withdrawalRequestStatus = status;
    }
    
    // Public method to request withdrawal
    public void requestWithdrawal() {
        if (this.withdrawalRequestStatus == null) {
            this.withdrawalRequestStatus = "PENDING";
        }
    }
    
    /**
     * Displays all relevant details of the application including project information.
     * This method would typically require accessing the BTOProject object to show full details.
     * In a real implementation, we might inject a ProjectManager to fetch project details.
     */
    public void displayApplication() {
        System.out.println("===== APPLICATION DETAILS =====");
        System.out.println("Application ID: " + applicationId);
        System.out.println("Applicant ID: " + applicantId);
        System.out.println("Project Name: " + projectName);
        System.out.println("Flat Type: " + flatType);
        System.out.println("Application Status: " + applicationStatus);
        System.out.println("Application Date: " + applicationDate);
        System.out.println("Withdrawal Request: " + (withdrawalRequestStatus == null ? "None" : withdrawalRequestStatus));
        
        // Note: In a full implementation, you would fetch and display project details
        // System.out.println("Project Name: " + projectDetails.getName());
        // System.out.println("Location: " + projectDetails.getNeighborhood());
        System.out.println("=============================");
    }
    
    /**
     * Books a flat for this application.
     * In a complete implementation, we would check if:
     * 1. The application status is SUCCESSFUL before booking
     * 2. HDB Officer has permission to book this flat
     * 
     * @param officerId The ID of the HDB Officer making the booking
     * @return true if booking was successful, false otherwise
     */
    public boolean bookFlat(String officerId) {
        // In a real implementation, we would verify if the officer is assigned to this project
        if (!"SUCCESSFUL".equals(applicationStatus)) {
            System.out.println("Cannot book flat. Application status must be SUCCESSFUL.");
            return false;
        }
        //can consider removing this if needed
            ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
            BTOProject project = projectDatabase.getProjectByName(projectName);
        if (project == null) {
            System.out.println("Project not found: " + projectName);
            return false;
        }
    
        FlatList flatList = project.getFlatLists();
         // Check flat availability
        if (flatType == FlatType.Three_Room && flatList.get3roomAvailUnit() > 0) {
            flatList.book_3room();
        } else if (flatType == FlatType.Two_Room && flatList.get2roomAvailUnit() > 0) {
            flatList.book_2room();
        } else {
            System.out.println("No available units of the selected flat type: " + flatType);
            return false;
        }
        
        // Update the application status to "BOOKED"
        this.applicationStatus = "BOOKED";
        System.out.println("Flat successfully booked by officer " + officerId + " for application " + applicationId);
        return true;
    }
    
    // Display application details
    @Override
    public String toString() {
        return "Application ID: " + applicationId +
               "\nApplicant ID: " + applicantId +
               "\nProject ID: " + projectName +
               "\nFlat Type: " + flatType +
               "\nApplication Status: " + applicationStatus +
               "\nApplication Date: " + applicationDate +
               "\nWithdrawal Request Status: " + 
                  (withdrawalRequestStatus == null ? "None" : withdrawalRequestStatus);
    }
}
