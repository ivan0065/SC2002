package Main.Manager_control;

import Main.BTO.BTOProject;
import Main.BTO.FlatList;
import Main.Enums.FlatType;
import Main.Personnel.Applicant;
import Main.Personnel.HDBManager;
import Main.Personnel.HDBOfficer;
import Main.interfaces.I_projectManager;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
public class ProjectManager implements I_projectManager{
    private List<BTOProject> managedProjects;

    @Override
    public void createBTOProject(HDBManager HDBManagerInCharge,
                    List<HDBOfficer> HDBOfficerList,
                    List<BTOApplication> applications,
                    List<Applicant> applicantList,
                    String projectName,
                    LocalDate applicationOpeningDate,
                    LocalDate applicationClosingDate,
                    boolean isVisible,
                    List<FlatType> flatTypes,
                    String projectNeighbourhood,
                    FlatList flatLists,
                    String projectId) {
        BTOProject newProject = new BTOProject(HDBManagerInCharge,HDBOfficerList,applications,applicantList,projectName,applicationOpeningDate,applicationClosingDate,isVisible,flatTypes,projectNeighbourhood,flatLists);
        managedProjects.add(newProject);
    }

    public void editBTOProject(int choice,BTOProject project){
        //choice 1: edit project name
        //choice 2: edit application opening date
        //choice 3: edit application closing date
        //choice 4: edit project status
        //choice 5: edit project neighbourhood
        //choice 6: edit flat types
        //choice 7: edit project visibility
        //choice 8: edit project id
        //choice 9: edit flat list
        //choice 10: edit HDBManager in charge
        //choice 11: edit HDBOfficer list
        Scanner scanner = new Scanner(System.in);
        switch (choice) {
            case 1 -> {
                System.out.println("Enter new project name: ");
                String newProjectName = scanner.nextLine();
                project.setProjectName(newProjectName);
            }
            case 2 -> {
                System.out.println("Enter new application opening date (YYYY-MM-DD): ");
                LocalDate newOpeningDate = LocalDate.parse(scanner.nextLine());
                project.setApplicationOpeningDate(newOpeningDate);
            }
            case 3 -> {
                System.out.println("Enter new application closing date (YYYY-MM-DD): ");
                LocalDate newClosingDate = LocalDate.parse(scanner.nextLine());
                project.setApplicationClosingDate(newClosingDate);
            }
            case 4 -> {
                System.out.println("Enter new project status: ");
                String newStatus = scanner.nextLine();
                project.setProjectStatus(newStatus);
            }
            case 5 -> {
                System.out.println("Enter new project neighbourhood: ");
                String newNeighbourhood = scanner.nextLine();
                project.setProjectNeighbourhood(newNeighbourhood);
            }
            case 6 -> {
                System.out.println("Enter visibility (true/false): ");
                boolean isVisible = Boolean.parseBoolean(scanner.nextLine());
                toggleProjectVisibility(project, isVisible);
            }
            
            default -> System.out.println("Invalid choice.");
        }
        // Implement flat list editing logic here
        // Implement HDBManager editing logic here
        // Implement HDBOfficer list editing logic here
        scanner.close();       
    }

    public void deleteBTOProject(String ProjectName){
        boolean deleted=false;
        Iterator<BTOProject> iterator = managedProjects.iterator();
        while (iterator.hasNext()) {
            BTOProject project = iterator.next();
            if (project.getProjectName().equals(ProjectName)) {
                iterator.remove();
                deleted = true;
                System.out.println(ProjectName + " is deleted successfully");
                break;
            }
        }
        if(deleted==false){
            System.out.println(ProjectName+"is not found");
        }
    }

    public void toggleProjectVisibility(BTOProject project,boolean isVisible){
        if(isVisible==true){
            project.setVisible(true);
        }else{
            project.setVisible(false);
        }
        
    }

    public List<BTOProject> getManagedProject(){
        return this.managedProjects;
    }

    public void viewBTOProjects(){
        for(BTOProject project: managedProjects){
            project.displayProject();
        }
    }
}
