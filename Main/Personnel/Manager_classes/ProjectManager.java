package Main.Personnel.Manager_classes;

import Main.BTO.BTOProject;
import java.util.List;
public class ProjectManager{
    private List<BTOProject> managedProjects;

    public void createBTOProject(){
        BTOProject project= new BTOProject();
    }

    public void editBTOProject(int choice){

    }

    public void deleteBTOProject(String ProjectName){
        boolean deleted=false;
        for(BTOProject project: managedProjects){
            if(project.getProjectName()==ProjectName){
                managedProjects.remove(project);
                deleted=true;
                System.out.println(ProjectName+"is deleted Successfully");
            }
        }
        if(deleted==false){
            System.out.println(ProjectName+"is not found");
        }
    }

    //not done
    public void toggleProjectVisibility(){

    }

    public List<BTOProject> getManagedProjects(){
        return this.managedProjects;
    }

    public void viewBTOProject(){
        for(BTOProject project: managedProjects){
            //not implemented yet
            project.displayProject();
        }
    }

}