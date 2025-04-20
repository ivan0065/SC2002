package Main.Manager_control;

import java.util.ArrayList;
import java.util.List;

import Main.BTO.ProjectDatabase;
import Main.BTO.BTOProject;
import Main.Personnel.HDBOfficer;
import Main.interfaces.I_RegistrationManager;

public class RegistrationManager implements I_RegistrationManager{

	private List<Registration> allRegistrations;  // List to store all registrations

	// Constructor
	public RegistrationManager() {
	this.allRegistrations = new ArrayList<>();
	}

	// Method to add a registration 
        @Override
	public void addRegistration(Registration registration) {
	allRegistrations.add(registration);
	}
    
	public List<Registration> getRegistrationList(){
		return allRegistrations;
	}
	
	public List<BTOProject> getAvailForRegistration() {
		List<BTOProject> allProjects = ProjectDatabase.getBTOProjectsList(); 
	    List<BTOProject> availProjects = new ArrayList<>();

	    for (BTOProject project : allProjects) {
	    	int availableSlots = project.getRemainingOfficerSlots(); 
	        if (availableSlots > 0) {
	        	availProjects.add(project);
	        	}
	        }

	        return availProjects;
	}
	
	public boolean validateOfficerEligibility(String officerUserID, String projectID) {
		// Get the list of applications tied to the project the officer is trying to register for
		List<BTOApplication> applications = project.getApplications();  
		
		for (BTOApplication application : applications) {
		    // If the officer's userID matches any applicant's userID for the given projectID, they are not eligible
		    if (application.getUserID().equals(officerUserID) && application.getProjectID().equals(projectID)) {
			return false;  // Officer cannot register as an officer for a project they have applied for
		    }
		}
		
		// If no match was found, the officer is eligible
		return true;
	}
	
	public void createRegistration(BTOProject project, HDBOfficer officer) {
	    if (validateOfficerEligibility(officer)) {
	        Registration reg = new Registration(project, officer);
	        allRegistrations.add(reg);              
	        officer.addRegistration(reg);        
		}
   	}
	
	public List<Registration> getRegistrationsForOfficer(String officerUserID) {
	        List<Registration> officerRegistrations = new ArrayList<>();
	
	        for (Registration registration : allRegistrations) {
	            if (registration.getOfficer().getUserID().equals(officerUserID)) {
	                officerRegistrations.add(registration);
	            }
	        }
	
	        return officerRegistrations;  
	}

	public Registration getRegistrationByRegId(String registrationID) {
	        for (Registration registration : allRegistrations) {
	            if (registration.getRegistrationId().equals(registrationID)) {
	                return registration;  
	            }
	        }
	        
	        return null;  
	}
	
	// Method to check if the application period of the project the officer is trying to register for clashes
    // with any other project the officer is already handling
    public boolean checkApplicationPeriodClash(HDBOfficer officer, BTOProject newProject) {
        // Iterate through all registrations to check for any overlapping application periods
        for (Registration registration : allRegistrations) {
            if (registration.getOfficer().getUserID().equals(officer.getUserID())) {
                BTOProject existingProject = registration.getProject();

                if (isPeriodClash(existingProject, newProject)) {
                    return true;  
                }
            }
        }
        
        return false;  
    }

    // Helper method to check if two projects' application periods overlap
    private boolean isPeriodClash(BTOProject existingProject, BTOProject newProject) {
        return !(newProject.getApplicationClosingDate().isBefore(existingProject.getApplicationOpeningDate()) ||
                 newProject.getApplicationOpeningDate().isAfter(existingProject.getApplicationClosingDate()));
    }
	
    public boolean approveOfficerRegistration(String registrationId) {
        Registration registration = getRegistrationByRegId(registrationId);

        if (registration != null) {
            // Check for application period clash
            boolean isClash = checkApplicationPeriodClash(registration.getOfficer(), registration.getProject());

            // If there is a clash, registration is unsuccessful
            if (isClash) {
                return false;  // Unsuccessful
            }
            return true;  // Successful
        }

        return false;  // If no registration found, return false (unsuccessful)
    }
	
    public boolean updateRegistrationStatus(String registrationId) {
        boolean isApproved = approveOfficerRegistration(registrationId);

        // Iterate through all registrations to find the one with the matching registrationId
        for (Registration registration : allRegistrations) {
            if (registration.getRegistrationId().equals(registrationId)) {
                if (isApproved) {
                    registration.updateStatus("SUCCESSFUL");  
                } else {
                    registration.updateStatus("UNSUCCESSFUL");  
                }
                return true;  // Status updated successfully
            }
        }

        return false;  // No registration found with the given ID, update failed
    }
	
    private void updateProjectHDBOfficers(String registrationId) {
        Registration registration = getRegistrationByRegId(registrationId);

        if (registration != null) {
            // Get the officer and project from the registration
            HDBOfficer officer = registration.getOfficer();
            BTOProject project = registration.getProject();

            // Add the officer to the project's list of officers
            project.addHDBOfficer(officer);
        }
    }
}

	
