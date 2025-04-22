package Main.interfaces;

import Main.Enquiries.Enquiry;

public interface I_officer_EnquiryM extends EnquiryManager {
    public void replyEnquiry(Enquiry enquiry, String reply);
}
