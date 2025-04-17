package Main.Manager_control;

import Main.Enums.FlatType;
// Must be in this package to allow only classes in this pacakage to modify status of application

public class BTOApplication{
    private String applicationStatus;

    private FlatType flatType;
    
    public String withdrawalRequestStatus;

    // as a form of protection such that only projectmanager can edit
    void setapplicationStatus(String status){
        this.applicationStatus=status;
    }

    void setwithdrawalStatus(String status){
        this.withdrawalRequestStatus=status;
    }

    public FlatType getFlatType(){
        return flatType;
    }
}