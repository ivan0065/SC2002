package Main.Personnel;

import java.util.List;

import Main.BTO.*;
import Main.Enums.FilterCriteria;
import Main.Manager_control.*;

public class HDBManager{
    private ProjectManager projectManager;
    private RegistrationManager registrationManager;
    private ApplicationManager appManager;
    private BTOProject project;


    //AppManager part
    public boolean approveBTOApplication(BTOApplication application, FlatList flatList, String newStatus){
        return appManager.approveBTOApplication(application, flatList, newStatus);
        //change the edits in BTOProject
    }

    public void approveBTOWithdrawal(BTOApplication application, FlatList flatList, String newStatus){
        appManager.approveBTOWithdrawal(application, flatList, newStatus);
    }

    public void generateReport(FilterCriteria criteria, List<BTOApplication> applications){
        appManager.generateApplicantReport(criteria, applications);
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
        return projectManager.getManagedProjects();
    }

    public void viewBTOProjects(){
        projectManager.viewBTOProject();
    }

    //RegistrationManager part
    
}