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
    private I_projectManager projectManager= new ProjectManager();
    private I_RegistrationManager registrationManager=new RegistrationManager();
    private I_applicationManager appManager= new ApplicationManager();
    private OfficerEnquiryManager enquiryManager;
    private BTOProject project;

    // Constructor for HDBManager
    public HDBManager(String name, String username, String password, int age, MaritalStatus maritalStatus,
            I_projectManager projectManager,
            I_RegistrationManager registrationManager,
            I_applicationManager appManager){
    super(name, username, password, age, maritalStatus,UserRole.MANAGER); // Call the User constructor
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
        if (project == null) {
            // Try to find a project managed by this manager that has the application
            List<BTOProject> managedProjects = projectManager.getManagedProject();
            for (BTOProject managedProject : managedProjects) {
                for (BTOApplication app : managedProject.getApplications()) {
                    if (app.getApplicationId().equals(application_id)) {
                        // Found the application in this project
                        project = managedProject;
                        break;
                    }
                }
                if (project != null) break;
            }
            
            if (project == null) {
                System.out.println("No project found with application ID: " + application_id);
                return false;
            }
        }
    
        List<BTOApplication> app_list = project.getApplications();
        if (app_list == null || app_list.isEmpty()) {
            System.out.println("No applications available for approval in project: " + project.getProjectName());
            return false;
        }
    
        BTOApplication cur_Application = null;
        for (BTOApplication application : app_list) {
            if (application.getApplicationId().equals(application_id)) {
                cur_Application = application;
                break;
            }
        }
    
        if (cur_Application == null) {
            System.out.println("Application with ID " + application_id + " not found in project: " + project.getProjectName());
            return false;
        }
    
        FlatList flatList = project.getFlatLists();
        try {
            // Map the UI status strings to application status strings
            String applicationStatus;
            if (newStatus.equalsIgnoreCase("Approved")) {
                applicationStatus = "SUCCESSFUL";
            } else if (newStatus.equalsIgnoreCase("Rejected")) {
                applicationStatus = "UNSUCCESSFUL";
            } else {
                applicationStatus = newStatus; // Use as-is for direct status names
            }
            
            // Use the application manager to approve the application
            boolean result = appManager.approveBTOApplication(cur_Application, flatList, applicationStatus);
            if (result) {
                System.out.println("Application " + application_id + " has been set to " + applicationStatus);
            } else {
                System.out.println("Failed to update application status");
            }
            return result;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public void viewAllApplications() {
        List<BTOProject> managedProjects = projectManager.getManagedProject();
        if (managedProjects == null || managedProjects.isEmpty()) {
            System.out.println("You don't manage any projects.");
            return;
        }
        
        boolean foundApplications = false;
        System.out.println("\n===== ALL BTO APPLICATIONS =====");
        
        for (BTOProject managedProject : managedProjects) {
            List<BTOApplication> applications = managedProject.getApplications();
            if (applications != null && !applications.isEmpty()) {
                System.out.println("\nProject: " + managedProject.getProjectName());
                System.out.println("---------------------------");
                
                for (BTOApplication app : applications) {
                    System.out.println("Application ID: " + app.getApplicationId());
                    System.out.println("Applicant ID: " + app.getApplicantId());
                    System.out.println("Flat Type: " + app.getFlatType());
                    System.out.println("Status: " + app.getApplicationStatus());
                    
                    if (app.getWithdrawalRequestStatus() != null) {
                        System.out.println("Withdrawal Request: " + app.getWithdrawalRequestStatus());
                    }
                    
                    System.out.println("---------------------------");
                    foundApplications = true;
                }
            }
        }
        
        if (!foundApplications) {
            System.out.println("No applications found in any of your managed projects.");
        }
    }
    
    /**
     * View applications for a specific project
     * @param projectName The name of the project to view applications for
     */
    public void viewProjectApplications(String projectName) {
        BTOProject targetProject = null;
        List<BTOProject> managedProjects = projectManager.getManagedProject();
        
        for (BTOProject proj : managedProjects) {
            if (proj.getProjectName().equals(projectName)) {
                targetProject = proj;
                break;
            }
        }
        
        if (targetProject == null) {
            System.out.println("Project not found or you don't manage it: " + projectName);
            return;
        }
        
        List<BTOApplication> applications = targetProject.getApplications();
        if (applications == null || applications.isEmpty()) {
            System.out.println("No applications found for project: " + projectName);
            return;
        }
        
        System.out.println("\n===== APPLICATIONS FOR " + projectName + " =====");
        for (BTOApplication app : applications) {
            System.out.println("Application ID: " + app.getApplicationId());
            System.out.println("Applicant ID: " + app.getApplicantId());
            System.out.println("Flat Type: " + app.getFlatType());
            System.out.println("Status: " + app.getApplicationStatus());
            
            if (app.getWithdrawalRequestStatus() != null) {
                System.out.println("Withdrawal Request: " + app.getWithdrawalRequestStatus());
            }
            
            System.out.println("---------------------------");
        }
    }
    
    /**
     * Find application by ID across all managed projects
     * @param applicationId The ID of the application to find
     * @return The application if found, null otherwise
     */
    public BTOApplication findApplicationById(String applicationId) {
        List<BTOProject> managedProjects = projectManager.getManagedProject();
        
        for (BTOProject proj : managedProjects) {
            List<BTOApplication> applications = proj.getApplications();
            if (applications != null) {
                for (BTOApplication app : applications) {
                    if (app.getApplicationId().equals(applicationId)) {
                        return app;
                    }
                }
            }
        }
        
        return null;
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

    public void generateReport(FilterCriteria criteria,String Project){
        List<BTOApplication> app_list=project.getApplications();
        ProjectDatabase DB= ProjectDatabase.getInstance();
        for (BTOProject project: DB.getAllProjects()){
            if (project.getProjectName().equals(Project)){
                BTOProject proj=project;
                appManager.generateApplicantReport(criteria, app_list,proj);
                break;
            }
        }
        
    }
    
    //ProjectManager part
    public I_projectManager  getProjectManager(){
        return projectManager;
    }
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

    public void addManagedProject(BTOProject project){
        projectManager.addManagedProject(project);
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
    
    public I_RegistrationManager getRegistrationManager(){
        return registrationManager;
    }
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

    public List<Registration> getPendingRegistrations() {
        List<Registration> pendingRegs = new ArrayList<>();
        for (Registration reg : registrationManager.getRegistrationList()) {
            if (reg.getRegistrationStatus().equals("PENDING") && 
                reg.getProject().getHDBManagerInCharge().getUserID().equals(this.getUserID())) {
                pendingRegs.add(reg);
            }
        }
        return pendingRegs;
    }

    public void approveRegistration(String registrationId) {
        Registration reg = registrationManager.getRegistrationByRegId(registrationId);
        if (reg == null) {
            System.out.println("Registration not found: " + registrationId);
            return;
        }
        
        if (!reg.getProject().getHDBManagerInCharge().getUserID().equals(this.getUserID())) {
            System.out.println("You are not authorized to approve this registration.");
            return;
        }
        
        // Check if there are available officer slots
        if (reg.getProject().getRemainingOfficerSlots() <= 0) {
            System.out.println("No available officer slots for this project.");
            reg.updateStatus("REJECTED");
            return;
        }
        
        // Check for application period clash
        if (registrationManager.checkApplicationPeriodClash(reg.getOfficer(), reg.getProject())) {
            System.out.println("Application period clashes with another project the officer is handling.");
            reg.updateStatus("REJECTED");
            return;
        }
        
        // Approve the registration
        reg.updateStatus("APPROVED");
        
        // Add officer to the project
        reg.getProject().addHDBOfficer(reg.getOfficer());
        
        // Add project to officer's assigned projects
        reg.getOfficer().getAssignedProjects().add(reg.getProject());
        
        System.out.println("Registration approved: " + registrationId);
    }

    public void rejectRegistration(String registrationId) {
        Registration reg = registrationManager.getRegistrationByRegId(registrationId);
        if (reg == null) {
            System.out.println("Registration not found: " + registrationId);
            return;
        }
        
        if (!reg.getProject().getHDBManagerInCharge().getUserID().equals(this.getUserID())) {
            System.out.println("You are not authorized to reject this registration.");
            return;
        }
        
        reg.updateStatus("REJECTED");
        System.out.println("Registration rejected: " + registrationId);
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
    public void viewAllEnquiries(){
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
        for (BTOProject project : projectDatabase.getAllProjects()) {
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
        enquiryManager.replyEnquiryM(enquiry, reply);
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
