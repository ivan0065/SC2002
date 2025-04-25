package Main.Enquiries;
import Main.BTO.BTOProject;
import Main.Personnel.HDBManager;
import Main.Personnel.HDBOfficer;
import Main.interfaces.I_officer_EnquiryM;
import java.util.List;
public class OfficerEnquiryManager implements I_officer_EnquiryM {
    private HDBOfficer officer;
    private HDBManager manager;
 
    public OfficerEnquiryManager(HDBOfficer officer) {
        this.officer = officer;
    }
    public OfficerEnquiryManager(HDBManager hdbManager) {
        this.manager = hdbManager;
    }
    public void replyEnquiryM(Enquiry enquiry, String reply) {
        if (enquiry == null) {
            System.out.println("Cannot reply to a null enquiry.");
            return;
        }
        
        if (reply == null || reply.trim().isEmpty()) {
            System.out.println("Reply cannot be empty.");
            return;
        }
        
        // Check if the manager is authorized to reply to this enquiry
        // Check if the officer is assigned to the project this enquiry belongs to
        boolean isAuthorized = false;
        List<BTOProject> assignedProjects = manager.getProjectManager().getManagedProject();

        if (assignedProjects != null) {
            for (BTOProject project : assignedProjects) {
                if (project.getProjectName().equals(enquiry.getProject())) {
                    isAuthorized = true;
                    break;
                }
            }
        }

        if (!isAuthorized) {
            System.out.println("You are not authorized to reply to enquiries for project: " + enquiry.getProject());
            return;
        }

        // Add the reply
        enquiry.addReply(reply);
        System.out.println("Reply added successfully to enquiry ID: " + enquiry.getEnquiryID());
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
        if (enquiry == null) {
            System.out.println("Cannot reply to a null enquiry.");
            return;
        }
        
        if (reply == null || reply.trim().isEmpty()) {
            System.out.println("Reply cannot be empty.");
            return;
        }
        
        // Check if the officer is assigned to the project this enquiry belongs to
        boolean isAuthorized = false;
        List<BTOProject> assignedProjects = officer.getAssignedProjects();
        
        if (assignedProjects != null) {
            for (BTOProject project : assignedProjects) {
                if (project.getProjectName().equals(enquiry.getProject())) {
                    isAuthorized = true;
                    break;
                }
            }
        }
        
        if (!isAuthorized) {
            System.out.println("You are not authorized to reply to enquiries for project: " + enquiry.getProject());
            return;
        }
        
        // Add the reply
        enquiry.addReply(reply);
        System.out.println("Reply added successfully to enquiry ID: " + enquiry.getEnquiryID());
    }
    // Overloaded ViewEnquiry method that fetches all enquiries for the officer's assigned projects
    public void ViewEnquiry() {
        List<BTOProject> assignedProjects = officer.getAssignedProjects();
        
        if (assignedProjects == null || assignedProjects.isEmpty()) {
            System.out.println("You are not assigned to any projects.");
            return;
        }
        
        boolean foundEnquiries = false;
        
        for (BTOProject project : assignedProjects) {
            EnquiryList projectEnquiries = project.getEnquiryList();
            
            if (projectEnquiries != null && !projectEnquiries.isEmpty()) {
                System.out.println("\n===== Enquiries for project: " + project.getProjectName() + " =====");
                for (Enquiry enquiry : projectEnquiries.getEnquiries()) {
                    enquiry.printEnquiry();
                    System.out.println("---------------------------");
                    foundEnquiries = true;
                }
            }
        }
        
        if (!foundEnquiries) {
            System.out.println("No enquiries found for any of your assigned projects.");
        }
    }
}