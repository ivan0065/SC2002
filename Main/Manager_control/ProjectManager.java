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
public class ProjectManager implements I_projectManager{
    private List<BTOProject> managedProjects;

    public void createBTOProject(HDBManager HDBManagerInCharge,
					List<HDBOfficer> HDBOfficerList,
		            List<BTOApplication> applications,
		            List<Applicant> applicantList,
		            String projectName,
		            LocalDate applicationOpeningDate,
		            LocalDate applicationClosingDate,
		            boolean isVisible,
		            String projectStatus,
		            List<FlatType> flatTypes,
		            String projectNeighbourhood,
					FlatList flatLists,
					String projectId){
        BTOProject newProject = new BTOProject(HDBManagerInCharge,HDBOfficerList,applications,applicantList,projectName,applicationOpeningDate,applicationClosingDate,isVisible,projectStatus,flatTypes,projectNeighbourhood,flatLists,projectId);
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

    //not done
    public void toggleProjectVisibility(){
        
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