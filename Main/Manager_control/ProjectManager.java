package Main.Manager_control;

import Main.BTO.BTOProject;
import Main.interfaces.I_projectManager;
import java.util.Iterator;
import java.util.List;
public class ProjectManager implements I_projectManager{
    private List<BTOProject> managedProjects;

    public void createBTOProject(){
        BTOProject project= new BTOProject();
        managedProjects.add(project);
    }

    public void editBTOProject(int choice){

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
            //not implemented yet
            //project.displayProject();
        }
    }
}