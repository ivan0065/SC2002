package Main.BTO;

import java.time.LocalDate;
import java.util.List;

import Main.Personnel.*;
import Main.Manager_control.BTOApplication;
import Main.Enums.FlatType;

public class BTOProject {
		
	private HDBManager HDBManagerInCharge;
	
	private List<HDBOfficer> HDBOfficerList; 
	
	private List<Application> applications;
	
	private List<Applicant> applicantList;
		
	private String projectName;
	
	private LocalDate applicationOpeningDate;
	
	private LocalDate applicationClosingDate;
	
	private boolean isVisible;
	
	private String projectStatus;
	
	private List<FlatType> flatTypes;
	
	private String projectNeighbourhood;
	
	public BTOProject(HDBManager HDBManagerInCharge,
					List<HDBOfficer> HDBOfficerList,
		            List<Application> applications,
		            List<Applicant> applicantList,
		            String projectName,
		            LocalDate applicationOpeningDate,
		            LocalDate applicationClosingDate,
		            boolean isVisible,
		            String projectStatus,
		            List<FlatType> flatTypes,
		            String projectNeighbourhood) {
		this.HDBManagerInCharge = HDBManagerInCharge;
		this.HDBOfficerList = HDBOfficerList;
		this.applications = applications;
		this.applicantList = applicantList;
		this.projectName = projectName;
		this.applicationOpeningDate = applicationOpeningDate;
		this.applicationClosingDate = applicationClosingDate;
		this.isVisible = isVisible;
		this.projectStatus = projectStatus;
		this.flatTypes = flatTypes;
		this.projectNeighbourhood = projectNeighbourhood;
		
		this.HDBOfficerList = new ArrayList<>();
	    this.applications = new ArrayList<>();
	    this.applicantList = new ArrayList<>();
	    this.flatTypes = new ArrayList<>();
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

	public List<Application> getApplications() {
	    return applications;
	}

	public void addApplication(Application application) {
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
	
	// methods

	public int getAvailableOfficerSlots() {
	    return 10 - HDBOfficerList.size();
	}

	public boolean isApplicableFor(int age, boolean isMarried) {
	    if (isMarried) {
]	        if (age >= 21) {
	            return true; 
	        }
	    } else {
	        if (age >= 35) {
	            for (FlatType ft : flatTypes) {
	                if (ft.getFlatTypeName().equalsIgnoreCase("2-Room")) {
	                    return true;
	                }
	            }
	        }
	    }
	    return false; 
	}

	public boolean updateFlatAvailability(String flatTypeStr, int count) {
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
	    for (FlatList flatList : flatLists) {
	        FlatType typeInList = flatList.getFlatType();
	        if (typeInList == targetType) {
	            flatList.getUnitCount().put(targetType, count);
	            flatList.setNumAvailableUnits(count);
	            return true;
	        }
	    }

	    return false; // Matching FlatList not found
	}

	public void displayProject() {
	    System.out.println("=== BTO Project Details ===");
	    System.out.println("Project Name        : " + projectName);
	    System.out.println("Neighbourhood       : " + projectNeighbourhood);
	    System.out.println("Project Status      : " + projectStatus);
	    System.out.println("Visible to Public   : " + (isVisible ? "Yes" : "No"));
	    System.out.println("Application Period  : " + applicationOpeningDate + " to " + applicationClosingDate);
	    System.out.println("Available Officer Slots : " + getAvailableOfficerSlots());
	    System.out.println();

	    System.out.println("Available Flat Types and Units:");
	    for (FlatType flatType : flatTypes) {
	        int unitCount = 0;
	        for (FlatList flatList : flatLists) {
	            if (flatList.getFlatType() == flatType) {
	                unitCount = flatList.getUnitCount().getOrDefault(flatType, 0);
	                break;
	            }
	        }
	        System.out.printf("  - %s : %d unit(s)%n", flatType.toString(), unitCount);
	    }

	    System.out.println("============================\n");
	}

}
