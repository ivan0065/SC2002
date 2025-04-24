package Main.interfaces;

public interface I_applicant_EnquiryM extends EnquiryManager {
    public int addEnquiry(String enquiryContent, String project);
    public boolean removeEnquiry(int enquiryId,String project);
    public void editEnquiry(int enquiryID,String newQuestion,String project);
}
