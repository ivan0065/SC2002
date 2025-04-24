package Main.Personnel;

import AppInterface.I_UserInterface;
import AppInterface.I_applicant;
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
        this.applications = new ArrayList<>(); // Initialize the list of applications

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
            filteredProjects.add(project);
            
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
        String applicationId = java.util.UUID.randomUUID().toString();
        // Create new BTOApplication object
        newApp = new BTOApplication(applicationId, this.getUserID(), projectname, flatType);
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
    public void viewApplicationStatus()
    {
        System.out.println(newApp.toString());
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
    public I_UserInterface getUserInterface() {
        return new I_applicant(this);
    }

}
