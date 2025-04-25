package Main.Personnel;

import AppInterface.I_UserInterface;
import AppInterface.I_applicant;
import Main.BTO.BTOProject;
import Main.BTO.ProjectDatabase;
import Main.Enquiries.ApplicantEnquiryManager;
import Main.Enquiries.Enquiry;
import Main.Enquiries.EnquiryList;
import Main.Enums.FlatType;
import Main.Enums.MaritalStatus;
import Main.Enums.UserRole;
import Main.Manager_control.BTOApplication;
import Main.TimeManager;
import Main.interfaces.I_applicant_EnquiryM;
import java.util.ArrayList;
import java.util.List;

public class Applicant extends User implements I_applicant_EnquiryM
{
    protected String currentApplicationId; // ID of current application
    protected List<Integer> enquiryIds; // List containing ID of all enquiries made by applicant
    protected List<BTOApplication> applications; // List of BTO applications made by the applicant
    protected BTOApplication newApp; // New application object
    protected I_applicant_EnquiryM enquiryManager; // Enquiry manager for the applicant
    // Constructor
    public Applicant(String name, String nric, String password, int age, MaritalStatus martialStatus)
    {
        super(name, nric, password, age, martialStatus, UserRole.APPLICANT); // Call the User constructor
        this.currentApplicationId = null; // No BTO application by default
        this.enquiryIds = new ArrayList<>();
        this.applications = new ArrayList<>(); // Initialize the list of applications
        this.enquiryManager = new ApplicantEnquiryManager(this); // Initialize the enquiry manager
    }

    // can check if project is full etc(havent do this)
    public void viewOpenToUserGroup()
    {
        System.out.println("BTO Projects available: ");
        //incomplete
        // Access the ProjectDatabase through HDBManager's static reference
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
        // Get all projects and filter based on marital status
        List<BTOProject> allProjects = projectDatabase.getAllProjects();
        List<BTOProject> filteredProjects = new ArrayList<>();
        for (BTOProject project : allProjects) 
        {
            // Only add visible projects
            if (project.getVisibilitySetting()) {
                // Check if this project is applicable for the user
                boolean isMarried = getMaritalStatus() == MaritalStatus.MARRIED;
                if (project.isApplicableFor(getAge(), isMarried)) {
                    filteredProjects.add(project);
                }
            }
        }
        
        // Display the filtered projects to the user
        if (filteredProjects.isEmpty()) 
        {
            System.out.println("No projects available for your marital status and age.");
        } 
        else 
        {
            System.out.println("Available projects: ");
            for (BTOProject project : filteredProjects) 
            {
                System.out.println("Project Name: " + project.getProjectName());
            }
        }
    }

    public void applyBTO(String projectname, FlatType flatType)
    {
        if (currentApplicationId != null) 
        {
            System.out.println("You already have a pre-existing BTO application");
            return;
        }

        // Access the ProjectDatabase through HDBManager's static reference
        ProjectDatabase projectDatabase=ProjectDatabase.getInstance();
        
        // Get all projects and find the one with matching projectId
        List<BTOProject> allProjects = projectDatabase.getAllProjects();
        BTOProject targetProject = null;
        
        for (BTOProject project : allProjects) 
        {
            System.out.println(project.getProjectName());
            System.out.println(projectname);
            System.out.println("- '" + project.getProjectName() + "'");
            if (project.getProjectName().trim().equalsIgnoreCase(projectname.trim())) {
                targetProject = project;
                System.out.println("Match found: " + project.getProjectName());
        }
        }
        
        // Check if project is found
        if (targetProject == null) 
        {
            System.out.println("Project with Name " + projectname + " not found.");
            return;
        }
        // Check visibility
        if (!targetProject.getVisibilitySetting()) 
        {
            System.out.println("This project is not currently open for applications.");
            return;
        }
        // Check eligibility
        boolean isMarried = getMaritalStatus() == MaritalStatus.MARRIED;
        if (!targetProject.isApplicableFor(getAge(), isMarried)) 
        {
            System.out.println("You are not eligible to apply for this project. Singles must be 35 or older and can only apply for 2-Room flats.");
            return;
        }
        if (!isMarried && flatType == FlatType.Three_Room)
        {
            System.out.println("Singles are only eligible to apply for 2-Room flats.");
            return;
        }

        // Check if project is accepting applications in current time window
        try 
        {
            boolean isValid = TimeManager.isValid(targetProject.getApplicationOpeningDate(), targetProject.getApplicationClosingDate());
            if (!isValid)
            {
                System.out.println("This project is not currently accepting applications.");
                return;
            }
        } 
        catch (java.util.concurrent.TimeoutException e) 
        {
            System.out.println(e.getMessage());
            return;
        }

        // Create random application ID
        String applicationId = "APP-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        // Create new BTOApplication object
        newApp = new BTOApplication(applicationId, this.getUserID(), projectname, flatType);
        // Add the application to the applicant's list
        if (this.applications == null) {
            this.applications = new ArrayList<>();
        }
        this.applications.add(newApp);
        this.currentApplicationId = applicationId;
        
        // Add the application to the project
        targetProject.addApplication(newApp);
        
        // Add the applicant to the project's applicant list if not already there
        boolean applicantExists = false;
        for (Applicant existingApplicant : targetProject.getApplicantList()) {
            if (existingApplicant != null && existingApplicant.getUserID().equals(this.getUserID())) {
                applicantExists = true;
                break;
            }
        }
        
        if (!applicantExists) {
            targetProject.addApplicant(this);
        }
        
        System.out.println("Successfully applied for project " + targetProject.getProjectName() + 
                        " with flat type " + flatType + ".");
        System.out.println("Your application ID is: " + applicationId);
        System.out.println("Please keep this ID for future reference.");
    }


    public void withdrawApplication(String applicationId) {
        // Check if the application ID matches the current application
        if (currentApplicationId == null || !currentApplicationId.equals(applicationId)) {
            System.out.println("Invalid application ID. Please check and try again.");
            return;
        }
    
        BTOApplication appToWithdraw = null;
        for (BTOApplication app : applications) {
            if (app.getApplicationId().equals(applicationId)) {
                appToWithdraw = app;
                break;
            }
        }
        
        if (appToWithdraw == null) {
            System.out.println("Application not found in your applications list.");
            return;
        }
        
        // Check the current status of the application
        String currentStatus = appToWithdraw.getApplicationStatus();
        
        // If the application is already in UNSUCCESSFUL status, directly remove it
        if ("UNSUCCESSFUL".equals(currentStatus)) {
            applications.remove(appToWithdraw);
            currentApplicationId = null;
            
            // Remove from project as well
            ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
            BTOProject project = projectDatabase.getProjectByName(appToWithdraw.getProjectId());
            if (project != null) {
                project.getApplications().remove(appToWithdraw);
                System.out.println("Unsuccessful application has been withdrawn successfully.");
            }
            return;
        }
        
        // For other statuses, submit a withdrawal request
        appToWithdraw.requestWithdrawal();
        System.out.println("Withdrawal request submitted for application " + applicationId);
        System.out.println("Your request will be reviewed by the HDB manager.");
    }
    
    // Enhanced viewApplicationStatus method to show more application details
    public String viewApplicationStatus() {
        if (currentApplicationId == null) {
            return "You do not have any active BTO applications.";
        }
        
        BTOApplication currentApp = null;
        for (BTOApplication app : applications) {
            if (app.getApplicationId().equals(currentApplicationId)) {
                currentApp = app;
                break;
            }
        }
        
        if (currentApp == null) {
            return "Application not found. Please contact customer support.";
        }
        
        StringBuilder status = new StringBuilder();
        status.append("\n===== APPLICATION STATUS =====\n");
        status.append("Application ID: ").append(currentApp.getApplicationId()).append("\n");
        status.append("Project: ").append(currentApp.getProjectId()).append("\n");
        status.append("Flat Type: ").append(currentApp.getFlatType()).append("\n");
        status.append("Application Date: ").append(currentApp.getApplicationDate()).append("\n");
        status.append("Status: ").append(currentApp.getApplicationStatus()).append("\n");
        
        if (currentApp.getWithdrawalRequestStatus() != null) {
            status.append("Withdrawal Request Status: ").append(currentApp.getWithdrawalRequestStatus()).append("\n");
        }
        
        status.append("=============================\n");
        
        // Print and return the status
        System.out.println(status.toString());
        return status.toString();
    }

    public String getCurrentApplicationId()
    {
        return currentApplicationId;
    }

    public void viewEnquiry()
    {
        // View all enquiries made by the applicant
        enquiryManager.viewEnquiry();
    }
    public List<Integer> getEnquiryIds()
    {
        // Returns list of enquiry IDs
        return enquiryIds;
    }


    public int addEnquiry(String enquiryContent, String project)
    {
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
        // Get the project by name
        BTOProject project1 = projectDatabase.getProjectByName(project);
        if (project1 == null) 
        {
            System.out.println("Project not found: " + project);
            return -1;
        }
        // Create a new enquiry using the enquiry manager
        int enquiryId=enquiryManager.addEnquiry(enquiryContent, project);
        // Add the enquiry ID to the list of enquiry IDs
        if (enquiryIds.contains(enquiryId) == false)
        {
            enquiryIds.add(enquiryId);
        }
        return 0;
    }

    public boolean removeEnquiry(int enquiryId,String project)
    {
        // Check if the enquiry ID is valid
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
        // Get the project by name
        BTOProject project1 = projectDatabase.getProjectByName(project);
        if (project1 == null) 
        {
            System.out.println("Project not found: " + project);
            return false;
        }
        // Get the enquiry list from the project
        EnquiryList enquirylist=project1.getEnquiryList();
        // Find the enquiry by ID
        Enquiry enquiry = enquirylist.getEnquiryByID(enquiryId);
        if (enquiry == null) 
        {
            System.out.println("Enquiry not found: " + enquiryId);
            return false;
        }
        // Check if the enquiry belongs to the applicant
        if (!enquiry.getSender().equals(this)) 
        {
            System.out.println("You do not have permission to delete this enquiry.");
            return false;
        }
        // Remove the enquiry from the list
        return enquiryManager.removeEnquiry(enquiryId, project);
        // Returns True is removed, False if enquiryId is not found

    }

    public void ViewEnquiry(EnquiryList enquiryList) {
        // View all enquiries made by the applicant
        enquiryManager.ViewEnquiry(enquiryList);
    }

    public void editEnquiry(int enquiryID, String newQuestion, String project) {
        // Edit an enquiry made by the applicant
        enquiryManager.editEnquiry(enquiryID,newQuestion,project);
    }
    @Override
    public I_UserInterface getUserInterface() {
        return new I_applicant(this);
    }

}
