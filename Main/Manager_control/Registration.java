package Main.Manager_control;

import Main.BTO.BTOProject;
import Main.Personnel.HDBOfficer;
import java.time.LocalDate;

public class Registration {
	
	private static int regCount;
	
	private String registrationId;
	
	private String registrationStatus;
	
	private LocalDate registrationDate;
	
	private BTOProject project;
	
	private HDBOfficer officer;
	
	public Registration(BTOProject bto_project, HDBOfficer hdb_officer){
		this.registrationId = "REG" + String.format("%05d", regCount++);
    
    this.registrationStatus = "PENDING";  // Always initialize to PENDING
    
    this.registrationDate = LocalDate.now();
    
    this.project = bto_project;
    
    this.officer = hdb_officer;
    
    System.out.println("DEBUG: Created new registration: " + this.registrationId + 
                     " for officer " + hdb_officer.getUserID() + 
                     " for project " + bto_project.getProjectName() +
                     " with status " + this.registrationStatus);
}

	// Make sure this updateStatus method is visible and working correctly
	public void updateStatus(String newStatus) {
		String oldStatus = this.registrationStatus;
		this.registrationStatus = newStatus;
		System.out.println("DEBUG: Updated registration " + this.registrationId + 
						" status from " + oldStatus + " to " + newStatus);
	}
	//getters
	public String getRegistrationId() { return registrationId; }

	public String getRegistrationStatus() { return registrationStatus; }

	public LocalDate getRegistrationDate() { return registrationDate; }

	public BTOProject getProject() { return project; }

	public HDBOfficer getOfficer() { return officer; }
	
	//setter (there is only a setter method for registrationStatus as the other attributes should be immutable)

}
