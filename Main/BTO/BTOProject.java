package Main.BTO;

import Main.Enquiries.EnquiryList;
import Main.Enums.FlatType;
import Main.Manager_control.BTOApplication;
import Main.Personnel.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BTOProject {
		
	private HDBManager HDBManagerInCharge;
	
	private List<HDBOfficer> HDBOfficerList; 
	
	private List<BTOApplication> applications;
	
	private List<Applicant> applicantList;
		
	private String projectName;
	
	private LocalDate applicationOpeningDate;
	
	private LocalDate applicationClosingDate;
	
	private boolean isVisible;
	
	private String projectStatus;
	
	private List<FlatType> flatTypes;
	
	private String projectNeighbourhood;

	private FlatList flatLists;
	
	private EnquiryList enquiryList;
	
	private String projectId;
	
	public BTOProject(HDBManager HDBManagerInCharge,
					List<HDBOfficer> HDBOfficerList,
		            List<BTOApplication> applications,
		            List<Applicant> applicantList,
		            String projectName,
		            LocalDate applicationOpeningDate,
		            LocalDate applicationClosingDate,
		            boolean isVisible,
		            List<FlatType> flatTypes,
		            String projectNeighbourhood,
					FlatList flatLists) {
		
		this.HDBManagerInCharge = HDBManagerInCharge;
		this.HDBOfficerList = HDBOfficerList;
		this.applications = applications;
		this.applicantList = applicantList;
		this.projectName = projectName;
		this.applicationOpeningDate = applicationOpeningDate;
		this.applicationClosingDate = applicationClosingDate;
		this.isVisible = isVisible;
		this.projectStatus = "PENDING";
		this.flatTypes = flatTypes;
		this.projectNeighbourhood = projectNeighbourhood;
		this.flatLists= flatLists;
		
		this.HDBOfficerList = new ArrayList<>();
	    this.applications = new ArrayList<>();
	    this.applicantList = new ArrayList<>();
	    this.flatTypes = new ArrayList<>();
		this.enquiryList =	new EnquiryList();
	}


	public String getProjectName() {
	    return projectName;
	}

	public void setProjectName(String projectName) {
	    this.projectName = projectName;
	}

	public LocalDate getApplicationOpeningDate() {
	    return applicationOpeningDate;
	}

	public void setApplicationOpeningDate(LocalDate applicationOpeningDate) {
	    this.applicationOpeningDate = applicationOpeningDate;
	}

	public LocalDate getApplicationClosingDate() {
	    return applicationClosingDate;
	}

	public void setApplicationClosingDate(LocalDate applicationClosingDate) {
	    this.applicationClosingDate = applicationClosingDate;
	}

	public boolean getVisibilitySetting() {
	    return isVisible;
	}

	public void setVisible(boolean visible) {
	    this.isVisible = visible;
	}

	public String getProjectStatus() {
	    return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
	    this.projectStatus = projectStatus;
	}

	public EnquiryList getEnquiryList() {
	    return enquiryList;
	}
	public String getProjectNeighbourhood() {
	    return projectNeighbourhood;
	}

	public void setProjectNeighbourhood(String projectNeighbourhood) {
	    this.projectNeighbourhood = projectNeighbourhood;
	}

	public HDBManager getHDBManagerInCharge() {
	    return HDBManagerInCharge;
	}

	public void setHDBManagerInCharge(HDBManager HDBManagerInCharge) {
	    this.HDBManagerInCharge = HDBManagerInCharge;
	}


	// Editing Lists
	
	public List<HDBOfficer> getHDBOfficerList() {
	    return HDBOfficerList;
	}

	public void addHDBOfficer(HDBOfficer officer) {
	    HDBOfficerList.add(officer);
	}
	public void addOfficer(String officerName) {
        // This method might be called before all officers are loaded,
        // so we can't directly find the officer here.
        // In a real implementation, we would add the officer from the user database
        // For now, we just make a note that this project should have this officer
        System.out.println("Note: Officer " + officerName + " will be assigned to project " + projectName + " after loading.");
        
        // The actual officer will be added in the loadOfficersFromCSV method
        // when we have loaded all officers from the CSV file
    }
	public List<BTOApplication> getApplications() {
	    return applications;
	}

	public void addApplication(BTOApplication application) {
	    applications.add(application);
	}

	public List<Applicant> getApplicantList() {
	    return applicantList;
	}

	public void addApplicant(Applicant applicant) {
	    applicantList.add(applicant);
	}

	public List<FlatType> getFlatTypes() {
	    return flatTypes;
	}

	public void addFlatType(FlatType flatType) {
	    flatTypes.add(flatType);
	}
	
	public FlatList getFlatLists() {
	    return flatLists;
	}
	// methods

	public int getRemainingOfficerSlots() {
	    return 10 - HDBOfficerList.size();
	}

	public boolean isApplicableFor(int age, boolean isMarried) {
	    if (isMarried) {
	        if (age >= 21) {
	            return true; 
	        }
	    } else {
	        if (age >= 35) {
	            for (FlatType ft : flatTypes) {
	                if (ft == FlatType.Two_Room) {
	                    return true;
	                }
	            }
	        }
	    }
	    return false; 
	}
	//IDK if needed cus when when manager approve application, auto updated
	/*public boolean updateFlatAvailability(String flatTypeStr, int count) {
	    // Find the matching FlatType object from this.flatTypes
	    FlatType targetType = null;
	    for (FlatType ft : flatTypes) {
	        if (ft.toString().equalsIgnoreCase(flatTypeStr)) {
	            targetType = ft;
	            break;
	        }
	    }

	    if (targetType == null) {
	        return false; // No matching FlatType found in this project
	    }

	    // Find the corresponding FlatList and update count
	    for (Flat flatList : flatLists) {
	        FlatType typeInList = flatList.getFlatType();
	        if (typeInList == targetType) {
	            flatLists.getUnitCount().put(targetType, count);
	            flatLists.setNumAvailableUnits(count);
	            return true;
	        }
	    }

	    return false; // Matching FlatList not found
	}*/
	

	public void displayProject() {
	    System.out.println("=== BTO Project Details ===");
	    System.out.println("Project Name        : " + projectName);
	    System.out.println("Neighbourhood       : " + projectNeighbourhood);
	    System.out.println("Project Status      : " + projectStatus);
	    System.out.println("Visible to Public   : " + (isVisible ? "Yes" : "No"));
	    System.out.println("Application Period  : " + applicationOpeningDate + " to " + applicationClosingDate);
	    System.out.println("Available Officer Slots : " + getRemainingOfficerSlots());
	    System.out.println();

	    System.out.println("Available Flat Types and Units:");
	    flatLists.print_unitCount();
	    System.out.println("============================\n");
	}

}
