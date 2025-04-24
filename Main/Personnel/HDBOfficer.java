package Main.Personnel;

import AppInterface.I_UserInterface;
import AppInterface.I_officer;
import Main.BTO.BTOProject;
import Main.BTO.FlatList;
import Main.BTO.ProjectDatabase;
import Main.Enquiries.*;
import Main.Enums.FlatType;
import Main.Enums.MaritalStatus;
import Main.Manager_control.Registration;
import Main.interfaces.I_officer_EnquiryM;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
public class HDBOfficer extends Applicant implements I_officer_EnquiryM
{
    private List<BTOProject> assignedProjects;
    private String HDBManagerRegistrationStatus;
    private List<Registration> registrations;
    private I_officer_EnquiryM enquiryManager;
    private BTOProject curProject;
    public HDBOfficer(String name,String nric, String password, int age, MaritalStatus martialStatus)
    {
        super(name, nric, password, age, martialStatus);
        this.assignedProjects = new ArrayList<>();
        this.registrations = new ArrayList<>();
        this.currentApplicationId = null;
        this.enquiryIds = new ArrayList<>();
        this.enquiryManager = new OfficerEnquiryManager(this);
        this.curProject=null;
    }
    
    
    public void ViewEnquiry() {
        // This method should show all enquiries for all assigned projects
        if (assignedProjects == null || assignedProjects.isEmpty()) {
            System.out.println("You are not assigned to any projects.");
            return;
        }
        
        boolean foundEnquiries = false;
        
        System.out.println("\n===== Viewing All Enquiries for Your Assigned Projects =====");
        
        for (BTOProject project : assignedProjects) {
            EnquiryList projectEnquiries = project.getEnquiryList();
            
            if (projectEnquiries != null && !projectEnquiries.isEmpty()) {
                System.out.println("\nProject: " + project.getProjectName());
                System.out.println("---------------------------");
                
                for (Enquiry enquiry : projectEnquiries.getEnquiries()) {
                    enquiry.printEnquiry();
                    System.out.println("---------------------------");
                    foundEnquiries = true;
                }
            }
        }
        
        if (!foundEnquiries) {
            System.out.println("No enquiries found for any of your assigned projects.");
        }
    }
    public void replyEnquiry(Enquiry enquiry,String reply){
        enquiryManager.replyEnquiry(enquiry, reply);
    }
    public void getEnquiries(){
        if(assignedProjects.isEmpty()){
            System.out.println("No assigned projects available.");
            return;
        }
        for (BTOProject project : assignedProjects) {
            EnquiryList enquiries = project.getEnquiryList();
            if (enquiries.isEmpty()) {
                System.out.println("No enquiries available for project: " + project.getProjectName());
            } else {
                System.out.println("Enquiries for project: " + project.getProjectName());
                enquiries.getEnquiries();
            }
        }
    }
    public Enquiry getEnquiryByID(int enquiryID){
        if(assignedProjects.isEmpty()){
            System.out.println("No assigned projects available.");
            return null;
        }
        for (BTOProject project : assignedProjects) {
            EnquiryList enquiries = project.getEnquiryList();
            if (enquiries != null && !enquiries.isEmpty()) {
                Enquiry enquiry = enquiries.getEnquiryByID(enquiryID);
                
                if (enquiry != null) {
                    System.out.println("Found enquiry " + enquiryID + " in project " + project.getProjectName());
                    return enquiry;
                }
            }
        }
        System.out.println("Enquiry with ID " + enquiryID + " not found in any assigned projects.");
        return null;
    }

    @Override
    public void ViewEnquiry(EnquiryList enquiryList) {
        // Delegate to the enquiry manager
        enquiryManager.ViewEnquiry(enquiryList);
    }

    public void joinProject(BTOProject project) {
        if (currentApplicationId != null) {
            for (BTOProject existingProject : assignedProjects) {
                if (existingProject.getProjectName().equals(project.getProjectName())) {
                    System.out.printf("You have already applied for the project %s as an applicant%n", 
                                    project.getProjectName());
                    return;
                }
            }
        }
    
        // Check for application period clashes with existing projects
        for (BTOProject existingProject : assignedProjects) {
            boolean openingDateClash = !(project.getApplicationOpeningDate().isBefore(existingProject.getApplicationOpeningDate()) || 
                                       project.getApplicationOpeningDate().isAfter(existingProject.getApplicationClosingDate()));
                                       
            boolean closingDateClash = !(project.getApplicationClosingDate().isBefore(existingProject.getApplicationOpeningDate()) || 
                                       project.getApplicationClosingDate().isAfter(existingProject.getApplicationClosingDate()));
            
            if (openingDateClash || closingDateClash) {
                System.out.printf("Current application period clashes with pre-existing project: %s%n", 
                                existingProject.getProjectName());
                return;
            }
        }
        
        // Get the manager of the project
        HDBManager manager = project.getHDBManagerInCharge();
        
        if (manager == null) {
            System.out.println("Cannot register for project: No manager assigned to this project.");
            return;
        }
        
        // Create a registration request through the manager
        manager.createRegistration(project, this);
        
        System.out.println("Registration request submitted for project: " + project.getProjectName());
        System.out.println("Your request is pending approval from the HDB Manager.");
        
        // Check if the registration was created successfully
        String status = getRegistrationStatusForProject(project);
        System.out.println("Current registration status: " + status);
    }
    
    // Add this overloaded method to handle direct joining during initialization
    public void joinProject(BTOProject project, boolean isDirectJoin) {
        if (isDirectJoin) {
            // Direct join without checks (for CSV loading)
            if (this.assignedProjects == null) {
                this.assignedProjects = new ArrayList<>();
            }
            
            // Check if already assigned to avoid duplicates
            for (BTOProject existingProject : this.assignedProjects) {
                if (existingProject.getProjectName().equals(project.getProjectName())) {
                    // Already assigned, just return quietly
                    return;
                }
            }
            
            this.assignedProjects.add(project);
            return;
        } else {
            // Normal operation - call the standard method
            joinProject(project);
        }
    }


    public String getRegistrationStatusForProject(BTOProject project) {
        System.out.println("DEBUG: Checking registration status for project " + project.getProjectName());
        
        // First, check officer's own registrations list
        if (this.registrations != null && !this.registrations.isEmpty()) {
            System.out.println("DEBUG: Officer has " + this.registrations.size() + " registrations");
            
            for (Registration reg : this.registrations) {
                if (reg.getProject() != null && 
                    reg.getProject().getProjectName().equals(project.getProjectName())) {
                    
                    String status = reg.getRegistrationStatus();
                    System.out.println("DEBUG: Found registration with status: " + status);
                    return status;
                }
            }
        } else {
            System.out.println("DEBUG: Officer's registration list is empty or null");
        }
        
        // If not found in officer's list, check manager's list
        HDBManager manager = project.getHDBManagerInCharge();
        if (manager != null) {
            List<Registration> managerRegs = manager.getRegistrationList();
            if (managerRegs != null && !managerRegs.isEmpty()) {
                System.out.println("DEBUG: Manager has " + managerRegs.size() + " registrations");
                
                for (Registration reg : managerRegs) {
                    if (reg.getOfficer() != null && reg.getProject() != null && 
                        reg.getOfficer().getUserID().equals(this.getUserID()) && 
                        reg.getProject().getProjectName().equals(project.getProjectName())) {
                        
                        String status = reg.getRegistrationStatus();
                        System.out.println("DEBUG: Found registration in manager's list with status: " + status);
                        return status;
                    }
                }
            } else {
                System.out.println("DEBUG: Manager's registration list is empty or null");
            }
        } else {
            System.out.println("DEBUG: Project has no manager assigned");
        }
        
        // Last resort: check global registration list from registration manager
        // We need a way to access the global registration list
        // This might require restructuring your code or passing a reference
        
        System.out.println("DEBUG: No registration found after all checks");
        return "NO_REGISTRATION";
    }

    public void updateProjectInProfile()
    {
        //?
    }

    @Override
    public void applyBTO(String projectName, FlatType flatType) 
    {
        // Check if the officer is handling this project
        for (BTOProject project : assignedProjects) {
            if (projectName.equals(project.getProjectName())) {
                System.out.printf("You cannot apply for project %s as you are an officer handling this project.%n", project.getProjectName());
                return;
            }
        }
        super.applyBTO(projectName, flatType);
    }

    public void viewAvailableProjectsToJoin(){
        ProjectDatabase database =ProjectDatabase.getInstance();
        List<BTOProject> availableProjects = database.getBTOProjectsList();
        if (availableProjects.isEmpty()) {
            System.out.println("No available projects to join.");
            return;
        }
        System.out.println("Available Projects to Join:");
        for (int i = 0; i < availableProjects.size(); i++) {
            BTOProject project = availableProjects.get(i);
            System.out.println((i + 1) + ". " + project.getProjectName() + 
                " | Remaining Officer Slots: " + project.getRemainingOfficerSlots());
        }
    }

    public void viewAssignedProjectDetails(String projectName)
    {
        boolean projectFound = false;
        for (int i = 0; i < assignedProjects.size(); i++)
        {
            if (assignedProjects.get(i).getProjectName().equals(projectName))
            {
                projectFound = true;
                BTOProject currproj = assignedProjects.get(i);
                System.out.printf("Project name: %s%n", currproj.getProjectName());
                System.out.printf("Project neighbourhood: %s%n", currproj.getProjectNeighbourhood());
                System.out.println("Flat types:");
                
                List<FlatType> currflats = currproj.getFlatTypes();
                FlatList curflatList= currproj.getFlatLists();
                for (int j = 0; j < currflats.size(); j++)
                {
                    System.out.printf("Flat type %d: %s%n", j, currflats.get(j));
                    System.out.printf("Number of units for flat type %s: %d%n", currflats.get(j).toString(), curflatList.getavail_byroom().get(currflats.get(j)));
                }
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                String openingDate = currproj.getApplicationOpeningDate().format(formatter);
                String closingDate = currproj.getApplicationClosingDate().format(formatter); // Fixed to use closing date
                
                System.out.printf("Application opening date: %s%n", openingDate);
                System.out.printf("Application closing date: %s%n", closingDate);
                
                System.out.printf("HDB Manager in charge: %s%n", currproj.getHDBManagerInCharge().getUserID()); 
                System.out.printf("Available HDB officer slots: %d%n", currproj.getRemainingOfficerSlots());
            }
        }
        
        if (!projectFound) {
            System.out.println("Project '" + projectName + "' not found in your assigned projects.");
        }
    }

    //if user book flat, auto updated
    public void updateNumofFlats(int num, FlatType flatType)
    {
        // incomplete
    }
    
    public List<BTOProject> getAssignedProjects()
    {
        return assignedProjects;
    }

    public void addRegistration(Registration registration) {
        if (this.registrations == null) {
            this.registrations = new ArrayList<>();
        }
        
        // Check for duplicates
        boolean isDuplicate = false;
        for (Registration reg : this.registrations) {
            if (reg.getRegistrationId().equals(registration.getRegistrationId())) {
                isDuplicate = true;
                break;
            }
        }
        
        if (!isDuplicate) {
            this.registrations.add(registration);
            System.out.println("Added registration " + registration.getRegistrationId() + 
                            " to officer " + this.getUserID());
        }
    }

    @Override
    public I_UserInterface getUserInterface(){
        return new I_officer(this);
    }
}
