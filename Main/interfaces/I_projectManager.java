package Main.interfaces;

import Main.BTO.BTOProject;
import Main.BTO.FlatList;
import Main.Enums.FlatType;
import Main.Manager_control.BTOApplication;
import Main.Personnel.Applicant;
import Main.Personnel.HDBManager;
import Main.Personnel.HDBOfficer;
import java.time.LocalDate;
import java.util.List;
public interface I_projectManager {
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
					FlatList flatLists);
    void editBTOProject(int choice,BTOProject project);
    void deleteBTOProject(String projectName);
    void toggleProjectVisibility(BTOProject project,boolean isVisible);
    List<BTOProject> getManagedProject();
    void viewBTOProjects();
}