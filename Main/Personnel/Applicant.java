package Main.Personnel;

import Main.Enums.MaritalStatus;
import Main.Enums.UserRole;
import Main.Manager_control.BTOApplication;

import java.util.List;
import java.util.ArrayList;

public class Applicant extends User
{
    protected String currentApplicationId; // ID of current application
    protected List<String> enquiryIds; // List containing ID of all enquiries made by applicant

    // Constructor
    public Applicant(String nric, String password, int age, MaritalStatus martialStatus, UserRole userRole)
    {
        super(nric, password, age, martialStatus, UserRole.APPLICANT);
        this.currentApplicationId = null; // No BTO application by default
        this.enquiryIds = new ArrayList<>();
    }

    // incomplete
    public void viewOpenToUserGroup()
    {
        MaritalStatus status = getMaritalStatus();
        if (status == MaritalStatus.SINGLE)
        {
            System.out.println("BTO Projects available for Singles: ");
            //incomplete
        }
        else if (status == MaritalStatus.MARRIED)
        {
            System.out.println("BTO Projects available for Married: ");
            //incomplete
        }
    }

    public void applyBTO(String projectId, String flatType)
    {
        if (currentApplicationId == null)
        {
            // Randomly generated ID from UUID, converted to a string
            String newApplicationId = java.util.UUID.randomUUID().toString();

            // Create new BTO Application object
            BTOApplication newApp = new BTOApplication(newApplicationId, nric, projectId, flatType);
            // will require a setter in BTO Application class to set application ID
        }
        else
            System.out.println("You already have a pre-existing BTO application");
    }

    public String viewApplicationStatus()
    {
        return newApp.getApplicationStatus();
    }


    public String getCurrentApplicationId()
    {
        return currentApplicationId;
    }

    public List<String> getEnquiryIds()
    {
        // Returns list of enquiry IDs
        return new ArrayList<>(enquiryIds);
    }


    public void createEnquiry(String enquiryId)
    {
        if (enquiryIds.contains(enquiryId) == false)
        {
            enquiryIds.add(enquiryId);
        }
    }

    public boolean deleteEnquiry(String enquiryId)
    {
        // Returns True is removed, False if enquiryId is not found
        return enquiryIds.remove(enquiryId);
    }

    


}
