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
    public String viewApplicationStatus()
    {
        return newApp.toString();
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
        EnquiryList enquirylist=project1.getEnquiryList();
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
