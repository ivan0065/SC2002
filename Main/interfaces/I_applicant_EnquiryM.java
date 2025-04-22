package Main.interfaces;

import Main.BTO.BTOProject;
import Main.Enquiries.EnquiryList;

public interface I_applicant_EnquiryM extends EnquiryManager {
    public void addEnquiry(EnquiryList enquiryList, String question, BTOProject project);
    public void removeEnquiry(EnquiryList enquiryList, int enquiryID);
    public void editEnquiry(EnquiryList enquiryList, int enquiryID, String newQuestion);
}
