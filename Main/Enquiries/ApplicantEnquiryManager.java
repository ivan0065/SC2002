package Main.Enquiries;
import Main.Personnel.User;
import Main.interfaces.EnquiryManager;
public class ApplicantEnquiryManager implements EnquiryManager{
    private User applicant;

    public ApplicantEnquiryManager(User applicant) {
        this.applicant = applicant;
    }
    @Override
    public void ViewEnquiry(EnquiryList enquiryList) {
        Boolean status=false;
        if(enquiryList.isEmpty()){
            System.out.println("No enquiries available.");
            return;
        }
        for(Enquiry enquiry: enquiryList.getEnquiries()){
            if(enquiry.getSender().equals(applicant)){
                enquiry.printEnquiry();
                status=true;
            }
        }
        if(status==false){
            System.out.println("No enquiries available for this applicant.");
        }
    }
    public void addEnquiry(EnquiryList enquiryList, String question,Project project){ //project not created yet
        int enquiryID = enquiryList.getEnquiries().size() + 1; 
        Enquiry newEnquiry = new Enquiry(question, enquiryID, applicant);
        enquiryList.addEnquiry(newEnquiry);
        System.out.println("Enquiry added successfully.");
    }

    public void removeEnquiry(EnquiryList enquiryList, int enquiryID){
        Enquiry enquiry = enquiryList.getEnquiryByID(enquiryID);
        if(enquiry != null && enquiry.getSender().equals(applicant)){
            enquiryList.removeEnquiry(enquiry);
            System.out.println("Enquiry removed successfully.");
        } else {
            System.out.println("Enquiry not found or you do not have permission to remove it.");
        }
    }
    public void editEnquiry(EnquiryList enquiryList, int enquiryID, String newQuestion){
        Enquiry enquiry = enquiryList.getEnquiryByID(enquiryID);
        if(enquiry != null && enquiry.getSender().equals(applicant)){
            enquiry.setQuestion(newQuestion);
            System.out.println("Enquiry edited successfully.");
        } else {
            System.out.println("Enquiry not found or you do not have permission to edit it.");
        }
    }
}