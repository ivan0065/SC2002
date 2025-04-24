package Main.Personnel;

import Main.BTO.*;
import Main.Enquiries.Enquiry;
import Main.Enquiries.EnquiryList;
import Main.Enquiries.OfficerEnquiryManager;
import Main.Enums.FilterCriteria;
import Main.Enums.FlatType;
import Main.Enums.MaritalStatus;
import Main.Enums.UserRole;
import Main.Manager_control.*;
import Main.interfaces.I_RegistrationManager;
import Main.interfaces.I_applicationManager;
import Main.interfaces.I_officer_EnquiryM;
import Main.interfaces.I_projectManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import AppInterface.I_UserInterface;
import AppInterface.I_manager;


public class HDBManager extends User implements I_officer_EnquiryM{
    private I_projectManager projectManager;
    private I_RegistrationManager registrationManager;
    private I_applicationManager appManager;
    private I_officer_EnquiryM enquiryManager;
    private BTOProject project;

        // Constructor for HDBManager
    public HDBManager(String name, String username, String password, int age, MaritalStatus maritalStatus,
            I_projectManager projectManager,
            I_RegistrationManager registrationManager,
            I_applicationManager appManager){
    super(name, username, password, age, maritalStatus,UserRole.MANAGER); // Call the User constructor
    this.projectManager = projectManager;
    this.registrationManager = registrationManager;
    this.appManager = appManager;
    this.project = null;
    this.enquiryManager = new OfficerEnquiryManager(this);
    }

    public void setProject()
    {
        List<BTOProject> projects = projectManager.getManagedProject();

        if (projects.isEmpty()) 
        {
            System.out.println("No assigned projects available.");
            return;
        }

        for(BTOProject project:projects)
        {
            if (project.getApplicationClosingDate().isAfter(LocalDate.now()) 
             && project.getApplicationOpeningDate().isBefore(LocalDate.now()))
        {
            this.project=project;
            return;
        }
        }

        System.out.println("No active projects found within current application period.");
    }

    //AppManager part
    public boolean approveBTOApplication(String application_id, String newStatus)
    {
        if (project == null) 
        {
            System.out.println("No active project to approve");
            return false;
        }

        List<BTOApplication> app_list=project.getApplications();
        if (app_list.isEmpty() || app_list == null) 
        {
            System.out.println("No applications available for approval.");
            return false;
        }

        BTOApplication cur_Application = null;
        for(BTOApplication application: app_list)
        {
            if (application.getApplicationId().equals(application_id))
            {
                cur_Application= application;
                break;
            }
        }

        if (cur_Application == null) 
        {
            throw new IllegalArgumentException("Application with ID " + application_id + " not found.");
        }

        FlatList flatList=project.getFlatLists();
        try
        {
            return appManager.approveBTOApplication(cur_Application, flatList, newStatus);
        }
        catch (IllegalArgumentException e) 
        {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        //change the edits in BTOProject
    }

    public void approveBTOWithdrawal(String application_id, String newStatus){
        List<BTOApplication> app_list=project.getApplications();
        FlatList flatList=project.getFlatLists();
        BTOApplication cur_Application=null;
        for( BTOApplication application: app_list){
            if(application.getApplicationId().equals(application_id)){
                cur_Application= application;
                break;
            }
        }
        if (cur_Application == null) {
            throw new IllegalArgumentException("Application with ID " + application_id + " not found.");
        }
        appManager.approveBTOWithdrawal(cur_Application, flatList, newStatus);
    }

    public void generateReport(FilterCriteria criteria){
        List<BTOApplication> app_list=project.getApplications();
        appManager.generateApplicantReport(criteria, app_list,project);
    }
    
    //ProjectManager part

    public void createBTOProject(String projectName,LocalDate openingDate,LocalDate closingDate,String projectNeighbourhood,List<FlatType> flatTypes, Boolean isVisible,FlatList flatLists){
        List<HDBOfficer> HDBOfficerlist= new ArrayList<HDBOfficer>();
        List<BTOApplication> applications= new ArrayList<BTOApplication>();
        List<Applicant> applicantList= new ArrayList<Applicant>();
        projectManager.createBTOProject(this,HDBOfficerlist,applications,applicantList,projectName,openingDate,closingDate,isVisible,flatTypes,projectNeighbourhood,flatLists);
    }

    public void editBTOProject(int choice,BTOProject project){
        projectManager.editBTOProject(choice,project);
    }

    public void deleteBTOProject(String ProjectName){
        projectManager.deleteBTOProject(ProjectName);
    }

    public void toggleProjectVisibility(BTOProject project,boolean isVisible){
        projectManager.toggleProjectVisibility(project,isVisible);
    }

    public List<BTOProject> getManagedProject(){
        return projectManager.getManagedProject();
    }

    public void viewManagedProjects(){
        projectManager.viewBTOProjects();
    }

    public void viewALLprojects(){
        //use projectdatabase
        ProjectDatabase BTOdatabase= ProjectDatabase.getInstance();
        for (BTOProject proj: BTOdatabase.getAllProjects()){
            proj.displayProject();
        }
    }
    public String getuserID(){
        return super.getUserID();
    }
    //RegistrationManager part
    
    public void addRegistration(Registration registration){
        registrationManager.addRegistration(registration);
    }

    public List<Registration> getRegistrationList(){
        return registrationManager.getRegistrationList();
    }

    public List<BTOProject> getAvailForRegistration() {
        HDBOfficer officer = null;
		return registrationManager.getAvailForRegistration(officer);
    }

    public boolean validateOfficerEligibility(String officerUserID, String projectName) {
        return registrationManager.validateOfficerEligibility(officerUserID, projectName);
    }

    public void createRegistration(BTOProject project, HDBOfficer officer) {
        registrationManager.createRegistration(project, officer);
    }

    public boolean updateRegistrationStatus(String registrationId){
        return registrationManager.updateRegistrationStatus(registrationId);
    }    
    
    public boolean checkApplicationPeriodClash(HDBOfficer officer,BTOProject project){
        return registrationManager.checkApplicationPeriodClash(officer, project);
    }
    public void ViewEnquiry() {
        if(projectManager.getManagedProject().isEmpty()){
            System.out.println("No assigned projects available.");
            return;
        }
        for (BTOProject project : projectManager.getManagedProject()) {
            EnquiryList enquiries = project.getEnquiryList();
            if (enquiries.isEmpty()) {
                System.out.println("No enquiries available for project: " + project.getProjectName());
            } else {
                System.out.println("Enquiries for project: " + project.getProjectName());
                enquiries.ViewEnquiry();
            }
        }
    }
    //EnquiryManager part
    public void replyEnquiry(Enquiry enquiry,String reply){
        enquiryManager.replyEnquiry(enquiry, reply);
    }
    public void getEnquiries(){
        if(projectManager.getManagedProject().isEmpty()){
            System.out.println("No assigned projects available.");
            return;
        }
        for (BTOProject project : projectManager.getManagedProject()) {
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
        if(projectManager.getManagedProject().isEmpty()){
            System.out.println("No assigned projects available.");
            return null;
        }
        for (BTOProject project : projectManager.getManagedProject()) {
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

    
    @Override
    public void ViewEnquiry(EnquiryList enquiryList) {
        if(projectManager.getManagedProject().isEmpty()){
            System.out.println("No assigned projects available.");
            return;
        }
        for (BTOProject project : projectManager.getManagedProject()) {
            EnquiryList enquiries = project.getEnquiryList();
            if (enquiries.isEmpty()) {
                System.out.println("No enquiries available for project: " + project.getProjectName());
            } else {
                System.out.println("Enquiries for project: " + project.getProjectName());
                enquiries.ViewEnquiry();
            }
        }
    }  
    
    @Override
    public I_UserInterface getUserInterface() {
        return new I_manager(this);
    }


}
