package Main.Enquiries;

import java.util.ArrayList;
import java.util.List;

public class EnquiryList{
    private List<Enquiry> enquires;

    public EnquiryList(){
        enquires = new ArrayList<>();
    }
    
    
    public void ViewEnquiry(){
        if(enquires.isEmpty()){
            System.out.println("No enquiries available.");
            return;
        }
        for(Enquiry enquiry: enquires){
            enquiry.printEnquiry();
        }
    }

    public List<Enquiry> getEnquiries(){
        return enquires;
    }
    
    public void addEnquiry(Enquiry enquiry){
        enquires.add(enquiry);
    }

    public void removeEnquiry(Enquiry enquiry){
        enquires.remove(enquiry);
    }

    public Enquiry getEnquiryByID(int enquiryID){
        for(Enquiry enquiry: enquires){
            if(enquiry.getEnquiryID() == enquiryID){
                return enquiry;
            }
        }
        return null;
    }
    public String getReply() {
    return reply;
}

    public boolean isEmpty(){
        return enquires.isEmpty();
    }
}
