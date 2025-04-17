package Main.Manager_control;

import Main.BTO.*;
import Main.Enums.FlatType;
import java.util.Map;
public class ApplicationManager{
    public boolean approveBTOApplication(BTOApplication application, FlatList flatList,String newStatus){
        FlatType flatType=application.getFlatType();
        Map<FlatType,Integer> unitCount= flatList.getavail_byroom();
        if (unitCount.get(flatType) == 0){
            throw new IllegalArgumentException("No available units of the selected flat type: " + flatType);
        }
        application.setapplicationStatus(newStatus);
        return true;
    }

    public void approveBTOWithdrawal(BTOApplication application, FlatList flatList, String newStatus){
        application.setwithdrawalStatus(newStatus);
    }
}