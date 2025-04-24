package Main.Enquiries;
import Main.BTO.BTOProject;
import Main.BTO.ProjectDatabase;
import Main.Personnel.Applicant;
import Main.interfaces.I_applicant_EnquiryM;
public class ApplicantEnquiryManager implements I_applicant_EnquiryM{
    private Applicant applicant;

    public ApplicantEnquiryManager(Applicant applicant) {
        this.applicant = applicant;
    }

    @Override
    public void viewEnquiry(){
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
        // Get the project by name
        for(BTOProject project: projectDatabase.getAllProjects()){
            // Get the enquiry list from the project
            EnquiryList enquirylist=project.getEnquiryList();
            
            // Print the enquiries in the list
            if(enquirylist.isEmpty()){
                System.out.println("No enquiries available.");
                return;
            }
            for(Enquiry enquiry: enquirylist.getEnquiries()){
                if(enquiry.getSender().equals(applicant)){
                    enquiry.printEnquiry();
                }
            }
        }
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
    
    public int addEnquiry(String enquiryContent, String project){ 
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
        // Get the project by name
        BTOProject project1 = projectDatabase.getProjectByName(project);
        
        // Create a new enquiry using the enquiry manager
        EnquiryList enquirylist=project1.getEnquiryList();
        int enquiryID = enquirylist.getEnquiries().size() + 1; 
        Enquiry newEnquiry = new Enquiry(enquiryContent, enquiryID, applicant);
        enquirylist.addEnquiry(newEnquiry);
        System.out.println("Enquiry added successfully.");
        return enquiryID; // Return the ID of the new enquiry
    }

    public boolean removeEnquiry(int enquiryId,String project){
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
        // Get the project by name
        BTOProject project1 = projectDatabase.getProjectByName(project);
        // Get the enquiry list from the project
        EnquiryList enquirylist=project1.getEnquiryList();
        // Find the enquiry by ID
        Enquiry enquiry = enquirylist.getEnquiryByID(enquiryId);

        if(enquiry != null && enquiry.getSender().equals(applicant)){
            enquirylist.removeEnquiry(enquiry);
            System.out.println("Enquiry removed successfully.");
            return true;
        } else {
            System.out.println("Enquiry not found or you do not have permission to remove it.");
            return false;
        }
    }
    
    public void editEnquiry(int enquiryID,String newQuestion,String project){
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
        // Get the project by name
        BTOProject project1 = projectDatabase.getProjectByName(project);
        // Get the enquiry list from the project
        EnquiryList enquirylist=project1.getEnquiryList();
        // Find the enquiry by ID
        Enquiry enquiry = enquirylist.getEnquiryByID(enquiryID);

        if(enquiry != null && enquiry.getSender().equals(applicant)){
            enquiry.setQuestion(newQuestion);
            System.out.println("Enquiry edited successfully.");
        } else {
            System.out.println("Enquiry not found or you do not have permission to edit it.");
        }
    }
    

    

}