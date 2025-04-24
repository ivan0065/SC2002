package Main.Enquiries;
import Main.BTO.BTOProject;
import Main.Personnel.HDBManager;
import Main.Personnel.HDBOfficer;
import Main.interfaces.I_officer_EnquiryM;
import java.util.List;
public class OfficerEnquiryManager implements I_officer_EnquiryM {
    private HDBOfficer officer;

    public OfficerEnquiryManager(HDBOfficer officer) {
        this.officer = officer;
    }
    public OfficerEnquiryManager(HDBManager hdbManager) {
        //TODO Auto-generated constructor stub
    }
    @Override
    public void ViewEnquiry(EnquiryList enquiryList){
        Boolean status=false;
        if(enquiryList.isEmpty()){
            System.out.println("No enquiries available.");
            return;
        }
        for(Enquiry enquiry: enquiryList.getEnquiries()){
            List<BTOProject> assigned_projects = officer.getAssignedProjects();
            for (BTOProject project : assigned_projects) {
                if (enquiry.getProject().equals(project.getProjectName()) ) {
                    enquiry.printEnquiry();
                    status = true;
                }
            }
            
        }
        if(status==false){
            System.out.println("No enquiries available for this applicant.");
        }
    }
    public void replyEnquiry(Enquiry enquiry, String reply) {
        enquiry.addReply(reply);
        System.out.println("Reply sent successfully.");
    }

}