package Main.Personnel;

import Main.BTO.BTOProject;
import Main.BTO.ProjectDatabase;
import Main.Enums.FlatType;
import Main.Enums.MaritalStatus;
import Main.Enums.UserRole;
import Main.Manager_control.BTOApplication;
import Main.TimeManager;
import java.util.ArrayList;
import java.util.List;

public class Applicant extends User
{
    protected String currentApplicationId; // ID of current application
    protected List<String> enquiryIds; // List containing ID of all enquiries made by applicant
    protected List<BTOApplication> applications; // List of BTO applications made by the applicant
    protected BTOApplication newApp; // New application object
    // Constructor
    public Applicant(String name, String nric, String password, int age, MaritalStatus martialStatus)
    {
        super(name, nric, password, age, martialStatus, UserRole.APPLICANT); // Call the User constructor
        this.currentApplicationId = null; // No BTO application by default
        this.enquiryIds = new ArrayList<>();
    }

    // incomplete
    public void viewOpenToUserGroup()
    {
        MaritalStatus status = getMaritalStatus();
        if (status == MaritalStatus.SINGLE)
        {
            System.out.println("BTO Projects available for Singles: ");
            //incomplete
        }
        else if (status == MaritalStatus.MARRIED)
        {
            System.out.println("BTO Projects available for Married: ");
            //incomplete
        }
    }

    public void applyBTO(String projectId, FlatType flatType)
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
            if (project.getProjectId().equals(projectId)) {
                targetProject = project;
                break;
            }
        }
        
        // Check if project is found
        if (targetProject == null) 
        {
            System.out.println("Project with ID " + projectId + " not found.");
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
        String applicationId = java.util.UUID.randomUUID().toString();
        // Create new BTOApplication object
        newApp = new BTOApplication(applicationId, this.getUserID(), projectId, flatType);
        //Add new application to applicaitions list and project
        applications.add(newApp);
        currentApplicationId = applicationId;
        targetProject.addApplication(newApp);

        System.out.printf("Successfully applied for project %s with flat type %s%n", targetProject.getProjectName(), flatType);

    }

    public void withdrawApplication(String applicationId)
    {
        // Check if the application ID is valid
        if (currentApplicationId == null || !currentApplicationId.equals(applicationId)) 
        {
            System.out.println("Invalid application ID. Please check and try again.");
            return;
        }

        // Remove the application from the list and reset currentApplicationId
        applications.removeIf(app -> app.getApplicationId().equals(applicationId));
        currentApplicationId = null;
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
        // Find the project associated with the application ID and remove it from the project as well
        for (BTOProject project : projectDatabase.getAllProjects()) 
        {
            if (project.getApplications().removeIf(app -> app.getApplicationId().equals(applicationId))) 
            {
                System.out.printf("Successfully withdrew application with ID %s from project %s%n", applicationId, project.getProjectName());
                return;
            }
        }
    }
    public String viewApplicationStatus()
    {
        return newApp.getApplicationStatus();
    }


    public String getCurrentApplicationId()
    {
        return currentApplicationId;
    }

    public List<String> getEnquiryIds()
    {
        // Returns list of enquiry IDs
        return new ArrayList<>(enquiryIds);
    }


    public void createEnquiry(String enquiryId)
    {
        if (enquiryIds.contains(enquiryId) == false)
        {
            enquiryIds.add(enquiryId);
        }
    }

    public boolean deleteEnquiry(String enquiryId)
    {
        // Returns True is removed, False if enquiryId is not found
        return enquiryIds.remove(enquiryId);
    }


    @Override
    public IUserInterface getUserInterface() {
        return new I_applicant(this);
    }

}
