package Main.Personnel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Main.BTO.BTOProject;
import Main.BTO.Flat;
import Main.Enums.MaritalStatus;
import Main.Enums.UserRole;
import Main.Manager_control.Registration;
import Main.Enums.FlatType;

public class HDBOfficer extends Applicant
{
    private List<BTOProject> assignedProjects;
    private String HDBManagerRegistrationStatus;
    private List<Registration> registrations;

    public HDBOfficer(String nric, String password, int age, MaritalStatus martialStatus, UserRole userRole)
    {
        super(nric, password, age, martialStatus, userRole);
        this.assignedProjects = new ArrayList<>();
        this.registrations = new ArrayList<>();
        this.currentApplicationId = null;
        this.enquiryIds = new ArrayList<>();
    }

    public void joinProject(BTOProject project)
    {
        for (int i = 0; i < assignedProjects.size(); i++)
        {
            if (currentApplicationId == assignedProjects.get(i).getProjectID())
                {
                    System.out.printf("You have already applied for the project %s as an applicant%n", assignedProjects.get(i).getProjectID());
                    return;
                }
            else if (!project.getApplicationOpeningDate().isAfter(assignedProjects.get(i).getApplicationOpeningDate()))
                {
                    System.out.printf("Current application opening date clashes with pre-existing project: %s%n", assignedProjects.get(i).getProjectName());
                    return;
                }
            else if (!project.getApplicationOpeningDate().isAfter(assignedProjects.get(i).getApplicationOpeningDate()))
                {
                    System.out.printf("Current application closing date clashes with pre-existing project: %s%n", assignedProjects.get(i).getProjectName());
                    return;
                } 
        }
        
        // Missing: approval required by HDB Manager
        assignedProjects.add(project);
    }
    
    public String getHDBOfficerRegistrationStatus()
    {
        return HDBManagerRegistrationStatus;
    }

    public void updateProjectInProfile()
    {
        //?
    }

    public void applyBTO(String applyId)
    {
        for (int i = 0; i < assignedProjects.size(); i++)
        {
            if (applyId == assignedProjects.get(i).getProjectID())
                {
                    System.out.printf("You have already applied for the project %s as an officer%n", assignedProjects.get(i).getProjectID());
                    return;
                }
        }
        currentApplicationId = applyId;
        // Missing: register HDBOfficer in BTOApplication, BTOProject
    }

    public void viewAssignedProjectDetails(String projectId)
    {
        for (int i = 0; i < assignedProjects.size(); i++)
        {
            if (assignedProjects.get(i).getProjectID() == projectId)
            {
                BTOProject currproj = assignedProjects.get(i);
                System.out.printf("Project name: %s%n", currproj.getProjectName());
                // Not implemented yet
                System.out.printf("Project neighbourhood: %s%n", currproj.getProjectNeighbourhood());
                System.out.println("Flat types:");
                // Not implemented yet
                List<Flat> currflats = BTOProject.getFlatTypes();
                // consider changing getFlatTypes() function to getFlats()
                for (int j = 0; j < currflats.size(); j++)
                {
                    System.out.printf("Flat type %d: %s%n", j, currflats.get(i));
                    System.out.printf("Number of units for flat type %s: %d", currflats.get(i).getFlatType, currflats.get(i).getNumAvailableUnits());
                }
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                String openingDate = currproj.getApplicationOpeningDate().format(formatter);
                String closingDate = currproj.getApplicationOpeningDate().format(formatter);
                // note: change return type of getApplicationOpeningDate() and getApplicationClosingDate() in BTOProject to LocalDate instead of string
                System.out.printf("Application opening date: %s%n", openingDate);
                System.out.printf("Application closing date: %s%n", closingDate);
                
                // Suggestion: add getName() and Name to User
                System.out.printf("HDB Manager in charge: %s%n", currproj.getHDBManagerInCharge().getName()) //getName not implemented
                // Not implemented yet
                System.out.printf("Available HDB officer slots: %d%n", currproj.getRemainingOfficerSlots());
            }
        }
    }

    public void updateNumofFlats(int num, FlatType flatType)
    {
        // incomplete
    }
    


    
}
