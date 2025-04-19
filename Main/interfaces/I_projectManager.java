package Main.interfaces;

import java.util.List;
import Main.BTO.BTOProject;
public interface I_projectManager {
    void createBTOProject();
    void editBTOProject(int choice);
    void deleteBTOProject(String projectName);
    void toggleProjectVisibility();
    List<BTOProject> getManagedProject();
    void viewBTOProjects();
}