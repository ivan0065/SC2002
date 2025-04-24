package Main.Personnel;

import AppInterface.I_UserInterface;
import AppInterface.I_officer;
import Main.BTO.BTOProject;
import Main.BTO.FlatList;
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
    private String HDBOfficerRegistrationStatus;
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

    public void setOfficerRegistrationStatus(String status){
        this.HDBOfficerRegistrationStatus = status;
    }
    
    public void ViewEnquiry() {
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
                enquiries.ViewEnquiry();;
            }
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
            if (enquiries.isEmpty()) {
                System.out.println("No enquiries available for project: " + project.getProjectName());
            } else {
                Enquiry enquiry = enquiries.getEnquiryByID(enquiryID);
                return enquiry;
            }
        }
        return null;
    }

    public void ViewEnquiry(EnquiryList enquiryList) {
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
                enquiries.ViewEnquiry();;
            }
        }
    }

    public void joinProject(BTOProject project)
    {
        for (int i = 0; i < assignedProjects.size(); i++)
        {
            if (currentApplicationId == null ? assignedProjects.get(i).getProjectId() == null : currentApplicationId.equals(assignedProjects.get(i).getProjectId()))
                {
                    System.out.printf("You have already applied for the project %s as an applicant%n", assignedProjects.get(i).getProjectName());
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
    
    public String getHDBManagerRegistrationStatus()
    {
        return HDBManagerRegistrationStatus;
    }

    public String getHDBOfficerRegistrationStatus()
    {
        return HDBManagerRegistrationStatus;
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


    public void viewAssignedProjectDetails(String projectId)
    {
        for (int i = 0; i < assignedProjects.size(); i++)
        {
            if (assignedProjects.get(i).getProjectId() == projectId)
            {
                BTOProject currproj = assignedProjects.get(i);
                System.out.printf("Project name: %s%n", currproj.getProjectName());
                // Not implemented yet
                System.out.printf("Project neighbourhood: %s%n", currproj.getProjectNeighbourhood());
                System.out.println("Flat types:");
                // Not implemented yet
                List<FlatType> currflats = currproj.getFlatTypes();
                FlatList curflatList= currproj.getFlatLists();
                for (int j = 0; j < currflats.size(); j++)
                {
                    System.out.printf("Flat type %d: %s%n", j, currflats.get(i));
                    System.out.printf("Number of units for flat type %s: %d", currflats.get(i).toString(), curflatList.getavail_byroom().get(currflats.get(i)));
                }
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                String openingDate = currproj.getApplicationOpeningDate().format(formatter);
                String closingDate = currproj.getApplicationOpeningDate().format(formatter);
                // note: change return type of getApplicationOpeningDate() and getApplicationClosingDate() in BTOProject to LocalDate instead of string
                System.out.printf("Application opening date: %s%n", openingDate);
                System.out.printf("Application closing date: %s%n", closingDate);
                
                // Suggestion: add getName() and Name to User
                System.out.printf("HDB Manager in charge: %s%n", currproj.getHDBManagerInCharge().getuserID()); 
                // Not implemented yet
                System.out.printf("Available HDB officer slots: %d%n", currproj.getRemainingOfficerSlots());
            }
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
        registrations.add(registration);
    }

    @Override
    public I_UserInterface getUserInterface(){
        return new I_officer(this);
    }
}
