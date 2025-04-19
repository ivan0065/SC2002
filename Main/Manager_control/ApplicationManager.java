package Main.Manager_control;

import Main.BTO.*;
import Main.Enums.FilterCriteria;
import Main.Enums.FlatType;
import java.util.List;
import java.util.Map;
public class ApplicationManager{
    public boolean approveBTOApplication(BTOApplication application, FlatList flatList,String newStatus){
        FlatType flatType=application.getFlatType();
        Map<FlatType,Integer> unitCount= flatList.getavail_byroom();
        if (unitCount.get(flatType) == 0 && "Approved".equals(newStatus)){
            throw new IllegalArgumentException("No available units of the selected flat type: " + flatType);
        }
        application.setapplicationStatus(newStatus);
        if (application.getFlatType()==FlatType.Three_Room){
            flatList.book_3room();
        }
        else{
            flatList.book_2room();
        } 
        return true;
    }

    public void approveBTOWithdrawal(BTOApplication application, FlatList flatList, String newStatus){
        application.setwithdrawalStatus(newStatus);
    }

    public void generateApplicantReport(FilterCriteria criteria, List<BTOApplication> applications){
        ReportGenerator reportGen= new ReportGenerator();
        List<ApplicantReport> report= reportGen.generateReport(applications, criteria);

        for(ApplicantReport r: report){
            r.toString();
        }
        }
}