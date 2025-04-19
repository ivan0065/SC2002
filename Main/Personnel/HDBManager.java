package Main.Personnel;

import java.util.List;

import Main.BTO.*;
import Main.Enums.FilterCriteria;
import Main.Enums.FlatType;
import Main.Enums.MaritalStatus;
import Main.Enums.UserRole;
import Main.Manager_control.*;

public class HDBManager extends User{
    private ProjectManager projectManager;
    private RegistrationManager registrationManager;
    private ApplicationManager appManager;
    private BTOProject project;

        // Constructor for HDBManager
    public HDBManager(String username, String password, int age, MaritalStatus maritalStatus, UserRole role) {
        super(username, password, age, maritalStatus, role); // Call the User constructor
        this.projectManager = new ProjectManager();
        this.registrationManager = new RegistrationManager();
        this.appManager = new ApplicationManager();
    }
    //AppManager part
    public boolean approveBTOApplication(String application_id, String newStatus){
        List<BTOApplication> app_list=project.getApplications();
        BTOApplication cur_Application=null;
        FlatList flatList=project.getflatList();
        for( BTOApplication application: app_list){
            if(application.getID.equals(application_id)){
                cur_Application= application;
                break;
            }
        }
        if (cur_Application == null) {
            throw new IllegalArgumentException("Application with ID " + application_id + " not found.");
        }
        return appManager.approveBTOApplication(cur_Application, flatList, newStatus);
        //change the edits in BTOProject
    }

    public void approveBTOWithdrawal(String application_id, String newStatus){
        List<BTOApplication> app_list=project.getApplications();
        FlatList flatList=project.getflatList();
        BTOApplication cur_Application=null;
        for( BTOApplication application: app_list){
            if(application.getID.equals(application_id)){
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
        appManager.generateApplicantReport(criteria, app_list);
    }

    //ProjectManager part

    public void createBTOProject(){
        projectManager.createBTOProject();
    }

    public void editBTOProject(int choice){
        projectManager.editBTOProject(choice);
    }

    public void deleteBTOProject(String ProjectName){
        projectManager.deleteBTOProject(ProjectName);
    }

    public void toggleProjectVisibility(){
        projectManager.toggleProjectVisibility();
    }

    public List<BTOProject> getManagedProject(){
        return projectManager.getManagedProject();
    }

    public void viewManagedProjects(){
        projectManager.viewBTOProjects();
    }

    public void viewALLprojects(){
        //use projectdatabase
    }
    //RegistrationManager part
    
}