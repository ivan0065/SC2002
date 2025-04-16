package Main.Enquiries;
import Main.Personnel.User;
import Main.interfaces.EnquiryManager;
public class OfficerEnquiryManager implements EnquiryManager {
    //private HDBOfficer officer; officer class not created
    private User officer;

    public OfficerEnquiryManager(User officer) {
        this.officer = officer;
    }
    @Override
    public void ViewEnquiry(EnquiryList enquiryList){
        Boolean status=false;
        if(enquiryList.isEmpty()){
            System.out.println("No enquiries available.");
            return;
        }
        for(Enquiry enquiry: enquiryList.getEnquiries()){
            if(enquiry.getProject().equals(officer.getProject())){
                enquiry.printEnquiry();
                status=true;
            }
        }
        if(status==false){
            System.out.println("No enquiries available for this applicant.");
        }
    }

}