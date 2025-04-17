package Main.Manager_control;

import java.time.LocalDate;
import Main.BTO.BTOProject;
import Main.Personnel.HDBOfficer;

public class Registration {
	
	private static int regCount;
	
	private String registrationId;
	
	private String registrationStatus;
	
	private LocalDate registrationDate;
	
	private BTOProject project;
	
	private HDBOfficer officer;
	
	public Registration(BTOProject bto_project, HDBOfficer hdb_officer){
		this.registrationId = "REG" + String.format("%05d", regCount++);
		
		this.registrationStatus = "PENDING";
		
		this.registrationDate = LocalDate.now();
		
		this.project = bto_project;
		
		this.officer = hdb_officer;
	}
	//getters
	public String getRegistrationId() { return registrationId; }

	public String getRegistrationStatus() { return registrationStatus; }

	public LocalDate getRegistrationDate() { return registrationDate; }

	public BTOProject getProject() { return project; }

	public HDBOfficer getOfficer() { return officer; }
	
	//setter (there is only a setter method for registrationStatus as the other attributes should be immutable)
	public void updateStatus(String newStatus) {
		this.registrationStatus = newStatus;
	}
}
