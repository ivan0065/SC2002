package Main.Manager_control;

import Main.BTO.*;
import Main.Enums.FilterCriteria;
import Main.Enums.FlatType;
import Main.interfaces.I_applicationManager;
import java.util.List;
import java.util.Map;
public class ApplicationManager implements I_applicationManager{
    public boolean approveBTOApplication(BTOApplication application, FlatList flatList,String newStatus){
        if ("SUCCESSFUL".equals(newStatus)) {
            FlatType flatType = application.getFlatType();
            Map<FlatType, Integer> unitCount = flatList.getavail_byroom();
            
            // Check if there are available units of the requested flat type
            if (unitCount.get(flatType) <= 0) {
                System.out.println("Error: No available units of the requested flat type: " + flatType);
                return false;
            }
        }
        
        // Set the application status
        application.setApplicationStatus(newStatus);
        
        // Only book a flat if we're setting the status to "SUCCESSFUL"
        if ("SUCCESSFUL".equals(newStatus)) {
            // Book the appropriate flat type
            if (application.getFlatType() == FlatType.Three_Room) {
                flatList.book_3room();
            } else {
                flatList.book_2room();
            }
        }
        
        return true;
    }

    public void approveBTOWithdrawal(BTOApplication application, FlatList flatList, String newStatus){
        application.setWithdrawalRequestStatus(newStatus);
    }

    public void generateApplicantReport(FilterCriteria criteria, List<BTOApplication> applications, BTOProject project) {
        ReportGenerator reportGen= new ReportGenerator();
        List<ApplicantReport> report= reportGen.generateReport(applications, criteria, project);

        for(ApplicantReport r: report){
            System.out.println(r.toString());
        }
        }

}