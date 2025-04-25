package Main.interfaces;

import Main.BTO.*;
import Main.Enums.FilterCriteria;
import Main.Manager_control.BTOApplication;
import java.util.List;
public interface I_applicationManager{
    boolean approveBTOApplication(BTOApplication application, FlatList flatList,String newStatus);
    void approveBTOWithdrawal(BTOApplication application, FlatList flatList, String newStatus);
    void generateApplicantReport(FilterCriteria criteria, List<BTOApplication> applications, BTOProject project);
}