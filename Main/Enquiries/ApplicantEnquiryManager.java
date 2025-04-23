package Main.Enquiries;
import Main.BTO.BTOProject;
import Main.Personnel.Applicant;
import Main.interfaces.I_applicant_EnquiryM;
public class ApplicantEnquiryManager implements I_applicant_EnquiryM{
    private Applicant applicant;

    public ApplicantEnquiryManager(Applicant applicant) {
        this.applicant = applicant;
    }

    public void ViewEnquiry(EnquiryList enquiryList){
        if(enquiryList.isEmpty()){
            System.out.println("No enquiries available.");
            return;
        }
        for(Enquiry enquiry: enquiryList.getEnquiries()){
            if(enquiry.getSender().equals(applicant)){
                enquiry.printEnquiry();
            }
        }  
    }
    
    public void addEnquiry(EnquiryList enquiryList, String question,BTOProject project){ 
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