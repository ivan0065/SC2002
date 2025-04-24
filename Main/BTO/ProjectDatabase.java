package Main.BTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import Main.Enquiries.EnquiryList;
import Main.Enquiries.Enquiry;
import Main.Enums.FilterType;
import Main.Enums.FlatType;
import Main.Enums.MaritalStatus;
import Main.Enums.SortType;
import Main.Enums.UserRole;
import Main.Manager_control.BTOApplication;
import Main.Personnel.Applicant;
import Main.Personnel.HDBManager;
import Main.Personnel.HDBOfficer;
import Main.Personnel.User;

/**
 * ProjectDatabase class that manages all BTO projects and their enquiries
 * Reads project data from CSV file and provides access to project information
 */
public class ProjectDatabase {
    private static ProjectDatabase instance;
    private static List<BTOProject> projects;
    private static EnquiryList enquiryList;
    private static List<User> users;
    
    // CSV file paths
    private static final String PROJECT_CSV_PATH = "ProjectList.csv";
    private static final String APPLICANT_CSV_PATH = "data/ApplicantList.csv";
    private static final String OFFICER_CSV_PATH = "data/OfficerList.csv";
    private static final String MANAGER_CSV_PATH = "data/ManagerList.csv";
    
    /**
     * Constructor for ProjectDatabase
     * Initializes empty lists and loads project data from CSV
     */
    public ProjectDatabase() {
        this.projects = new ArrayList<>();
        this.enquiryList = new EnquiryList();
        this.users = new ArrayList<>();
        
        // Load all CSV data
        loadProjectsFromCSV();
        loadApplicantsFromCSV();
        loadOfficersFromCSV();
        loadManagersFromCSV();
}
/* Get the singleton instance of ProjectDatabase
     * Creates instance if it doesn't exist
     * @return The singleton instance
     */
    public static synchronized ProjectDatabase getInstance() {
        if (instance == null) {
            instance = new ProjectDatabase();
        }
        return instance;
    }
    /**
     * Loads project data from the CSV file using Scanner
     */
    private void loadProjectsFromCSV() {
        try {
            File file = new File(PROJECT_CSV_PATH);
            Scanner scanner = new Scanner(file);
            
            // Skip header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            // Process each line of data
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
                
                try {
                    // Read project data
                    String projectName = lineScanner.next().trim();
                    String neighborhood = lineScanner.next().trim();
                    
                    // Parse flat types and units
                    String flatType1Str = lineScanner.next().trim();
                    FlatType flatType1 = parseFlatType(flatType1Str);
                    int unitsType1 = Integer.parseInt(lineScanner.next().trim());
                    int priceType1 = Integer.parseInt(lineScanner.next().trim());
                    
                    String flatType2Str = lineScanner.next().trim();
                    FlatType flatType2 = parseFlatType(flatType2Str);
                    int unitsType2 = Integer.parseInt(lineScanner.next().trim());
                    int priceType2 = Integer.parseInt(lineScanner.next().trim());
                    
                    // Parse dates
                    String openingDateStr = lineScanner.next().trim();
                    String closingDateStr = lineScanner.next().trim();
                    LocalDate openingDate = parseDate(openingDateStr);
                    LocalDate closingDate = parseDate(closingDateStr);
                    
                    // Parse manager and officer info
                    String managerNRIC = lineScanner.next().trim();
                    int officerSlot = Integer.parseInt(lineScanner.next().trim());
                    
                    // Get officer info if available
                    String officerNRIC = "";
                    if (lineScanner.hasNext()) {
                        officerNRIC = lineScanner.next().trim();
                    }
                    
                    // create and adding FlatList
                    List<Flat> flatList = new ArrayList<>();
                    for( int i=0;i<unitsType1;i++){
                        flatList.add(new Flat(flatType1, priceType1));
                    }
                    for( int i=0;i<unitsType2;i++){
                        flatList.add(new Flat(flatType2, priceType2));
                    }
                    
                    // Create project ID
                    String projectId = java.util.UUID.randomUUID().toString();
                    
                    // Create and add the project
                    BTOProject project = new BTOProject(null, new ArrayList<>(), new ArrayList<>(), 
                            new ArrayList<>(), projectName, openingDate, closingDate, true,
                            "OPEN", new ArrayList<>(), neighborhood, new FlatList(flatList), projectId);
                    
                    // Add flat types
                    project.addFlatType(flatType1);
                    project.addFlatType(flatType2);
                    
                    // Add officer if present
                    if (!officerNRIC.isEmpty() && !officerNRIC.equalsIgnoreCase("null")) {
                        String[] officers = officerNRIC.split(";");
                        for (String officer : officers) {
                            project.addOfficer(officer.trim());
                        }
                    }
                    
                    projects.add(project);
                    
                } catch (Exception e) {
                    System.err.println("Error processing project line: " + line);
                    System.err.println("Error details: " + e.getMessage());
                }
                
                lineScanner.close();
            }
            
            scanner.close();
            System.out.println("Successfully loaded " + projects.size() + " projects from CSV.");
            
        } catch (FileNotFoundException e) {
            System.err.println("Project CSV file not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing project data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads applicant data from the CSV file using Scanner
     */
    private void loadApplicantsFromCSV() {
        try {
            File file = new File(APPLICANT_CSV_PATH);
            Scanner scanner = new Scanner(file);
            
            // Skip header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            int applicantCount = 0;
            
            // Process each line of data
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
                
                try {
                    // Parse applicant data
                    String name = lineScanner.next().trim();
                    String nric = lineScanner.next().trim();
                    int age = Integer.parseInt(lineScanner.next().trim());
                    String maritalStatusStr = lineScanner.next().trim();
                    String password = lineScanner.next().trim();
                    
                    // Convert string to MaritalStatus enum
                    MaritalStatus maritalStatus = MaritalStatus.SINGLE; // Default
                    if (maritalStatusStr.equalsIgnoreCase("Married")) {
                        maritalStatus = MaritalStatus.MARRIED;
                    }
                    
                    // Create applicant and add to users list
                    Applicant applicant = new Applicant(name, nric, password, age, maritalStatus);
                    users.add(applicant);
                    applicantCount++;
                    
                } catch (Exception e) {
                    System.err.println("Error processing applicant line: " + line);
                    System.err.println("Error details: " + e.getMessage());
                }
                
                lineScanner.close();
            }
            
            scanner.close();
            System.out.println("Successfully loaded " + applicantCount + " applicants from CSV.");
            
        } catch (FileNotFoundException e) {
            System.err.println("Applicant CSV file not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing applicant data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads HDB officer data from the CSV file using Scanner
     */
    private void loadOfficersFromCSV() {
        try {
            File file = new File(OFFICER_CSV_PATH);
            Scanner scanner = new Scanner(file);
            
            // Skip header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            int officerCount = 0;
            
            // Process each line of data
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
                
                try {
                    // Parse officer data
                    String name = lineScanner.next().trim();
                    String nric = lineScanner.next().trim();
                    int age = Integer.parseInt(lineScanner.next().trim());
                    String maritalStatusStr = lineScanner.next().trim();
                    String password = lineScanner.next().trim();
                    
                    // Convert string to MaritalStatus enum
                    MaritalStatus maritalStatus = MaritalStatus.SINGLE; // Default
                    if (maritalStatusStr.equalsIgnoreCase("Married")) {
                        maritalStatus = MaritalStatus.MARRIED;
                    }
                    
                    // Create officer and add to users list
                    HDBOfficer officer = new HDBOfficer(name, nric, password, age, maritalStatus);
                    users.add(officer);
                    officerCount++;
                    
                    // Assign officer to projects if needed
                    for (BTOProject project : projects) {
                        List<HDBOfficer> officers = project.getHDBOfficerList();
                        for (HDBOfficer existingOfficer : officers) {
                            if (existingOfficer != null && existingOfficer.getUserID().equals(nric)) {
                                officer.joinProject(project);
                                break;
                            }
                        }
                    }
                    
                } catch (Exception e) {
                    System.err.println("Error processing officer line: " + line);
                    System.err.println("Error details: " + e.getMessage());
                }
                
                lineScanner.close();
            }
            
            scanner.close();
            System.out.println("Successfully loaded " + officerCount + " officers from CSV.");
            
        } catch (FileNotFoundException e) {
            System.err.println("Officer CSV file not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing officer data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads HDB manager data from the CSV file using Scanner
     */
    private void loadManagersFromCSV() {
        try {
            File file = new File(MANAGER_CSV_PATH);
            Scanner scanner = new Scanner(file);
            
            // Skip header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            int managerCount = 0;
            
            // Process each line of data
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");
                
                try {
                    // Parse manager data
                    String name = lineScanner.next().trim();
                    String nric = lineScanner.next().trim();
                    int age = Integer.parseInt(lineScanner.next().trim());
                    String maritalStatusStr = lineScanner.next().trim();
                    String password = lineScanner.next().trim();
                    
                    // Convert string to MaritalStatus enum
                    MaritalStatus maritalStatus = MaritalStatus.SINGLE; // Default
                    if (maritalStatusStr.equalsIgnoreCase("Married")) {
                        maritalStatus = MaritalStatus.MARRIED;
                    }
                    
                    // Create manager and add to users list
                    // Note: This constructor requires different arguments than what your class might have
                    // Adjust as needed based on your implementation
                    HDBManager manager = new HDBManager(name, nric, password, age, maritalStatus, null, null, null);
                    users.add(manager);
                    managerCount++;
                    
                    // Assign manager to projects
                    for (BTOProject project : projects) {
                        if (project.getHDBManagerInCharge() == null) {
                            project.setHDBManagerInCharge(manager);
                        }
                    }
                    
                } catch (Exception e) {
                    System.err.println("Error processing manager line: " + line);
                    System.err.println("Error details: " + e.getMessage());
                }
                
                lineScanner.close();
            }
            
            scanner.close();
            System.out.println("Successfully loaded " + managerCount + " managers from CSV.");
            
        } catch (FileNotFoundException e) {
            System.err.println("Manager CSV file not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing manager data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to parse flat type string to enum
     */
    private FlatType parseFlatType(String typeStr) {
        if (typeStr.contains("2-Room")) {
            return FlatType.Two_Room; 
        } else if (typeStr.contains("3-Room")) {
            return FlatType.Three_Room; 
        } else {
            throw new IllegalArgumentException("Unknown flat type: " + typeStr);
        }
    }
    
    /**
     * Helper method to parse date string to LocalDate
     */
    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }
    
    /**
     * Get a list of BTO projects based on sorting/filtering criteria
     * 
     * @param sortFilter The enum specifying how to sort or filter the projects
     */
    public List<BTOProject> getBTOProjectsList() {
        return new ArrayList<>(projects);
    }

    // Update the existing getBTOProjectsList method to properly handle VISIBLE_ONLY filter
    public void getBTOProjectsList(SortType sortFilter) {
        List<BTOProject> filteredProjects = new ArrayList<>(projects);
        
        // Apply visibility filter first if needed
        if (sortFilter == SortType.VISIBLE_ONLY) {
            filteredProjects.removeIf(p -> !p.getVisibilitySetting());
        }
            
        // Apply sorting/filtering based on the provided enum
        switch(sortFilter) {
            case ALPHABETICAL:
                filteredProjects.sort((p1, p2) -> p1.getProjectName().compareTo(p2.getProjectName()));
                break;
            case NEIGHBORHOOD:
                filteredProjects.sort((p1, p2) -> p1.getProjectNeighbourhood().compareTo(p2.getProjectNeighbourhood()));
                break;
            case DATE_ASCENDING:
                filteredProjects.sort((p1, p2) -> p1.getApplicationOpeningDate().compareTo(p2.getApplicationOpeningDate()));
                break;
            case DATE_DESCENDING:
                filteredProjects.sort((p1, p2) -> p2.getApplicationOpeningDate().compareTo(p1.getApplicationOpeningDate()));
                break;
            case VISIBLE_ONLY:
                filteredProjects.removeIf(p -> !p.getVisibilitySetting());
                break;
            default:
                // Default is alphabetical order as specified in requirements
                filteredProjects.sort((p1, p2) -> p1.getProjectName().compareTo(p2.getProjectName()));
                break;
        }
        
        // Display the filtered projects
        displayProjects(filteredProjects);
    }
    
    /**
     * Get project enquiries based on filtering criteria
     * 
     * @param filter The enum specifying how to filter the enquiries
     */
    public void getProjectEnquiries(FilterType filter) {
        // Implementation will depend on how you want to filter enquiries
        List<Enquiry> allEnquiries = enquiryList.getEnquiries();
        List<Enquiry> filteredEnquiries = new ArrayList<>();
        
        switch(filter) {
            case ALL:
                filteredEnquiries = allEnquiries;
                break;
            case PENDING:
                for (Enquiry enquiry : allEnquiries) {
                    // Check if the enquiry has a reply
                    if (enquiry.getReply() == null || enquiry.getReply().isEmpty()) {
                        filteredEnquiries.add(enquiry);
                    }
                }
                break;
            case ANSWERED:
                for (Enquiry enquiry : allEnquiries) {
                    if (enquiry.getReply() != null && !enquiry.getReply().isEmpty()) {
                        filteredEnquiries.add(enquiry);
                    }
                }
                break;
            default:
                filteredEnquiries = allEnquiries;
                break;
        }
        
        // Display filtered enquiries
        displayEnquiries(filteredEnquiries);
    }
    
    /**
     * Helper method to display a list of projects
     * 
     * @param projectList The list of projects to display
     */
    private void displayProjects(List<BTOProject> projectList) {
        if (projectList.isEmpty()) {
            System.out.println("No projects available.");
            return;
        }
        
        System.out.println("=== BTO PROJECTS ===");
        for (BTOProject project : projectList) {
            System.out.println("Project Name: " + project.getProjectName());
            System.out.println("Neighborhood: " + project.getProjectNeighbourhood());
            System.out.println("Application Period: " + project.getApplicationOpeningDate() + 
                             " to " + project.getApplicationClosingDate());
            System.out.println("Visibility: " + (project.getVisibilitySetting() ? "Visible" : "Hidden"));
            System.out.println("---------------------------");
        }
    }
    
    /**
     * Helper method to display a list of enquiries
     * 
     * @param enquiryList The list of enquiries to display
     */
    private void displayEnquiries(List<Enquiry> enquiryList) {
        if (enquiryList.isEmpty()) {
            System.out.println("No enquiries available.");
            return;
        }
        
        System.out.println("=== ENQUIRIES ===");
        for (Enquiry enquiry : enquiryList) {
            enquiry.printEnquiry();
            System.out.println("---------------------------");
        }
    }
    
    /**
     * Get a project by its name
     * 
     * @param projectName The name of the project to retrieve
     * @return The project with the specified name, or null if not found
     */
    public BTOProject getProjectByName(String projectName) {
        if (projectName == null) {
            return null;
        }
        
        for (BTOProject project : projects) {
            if (project.getProjectName().equalsIgnoreCase(projectName.trim())) {
                return project;
            }
        }
        return null;
    }
    
    /**
     * Get a project by its ID
     * 
     * @param projectId The ID of the project to retrieve
     * @return The project with the specified ID, or null if not found
     */
    public BTOProject getProjectById(String projectId) {
        if (projectId == null) {
            return null;
        }
        
        for (BTOProject project : projects) {
            if (project.getProjectId().equals(projectId)) {
                return project;
            }
        }
        return null;
    }
    
    /**
     * Get the list of all projects
     * 
     * @return The list of all projects
     */
    public List<BTOProject> getAllProjects() {
        return new ArrayList<>(projects);
    }
    
    /**
     * Get the enquiry list
     * 
     * @return The enquiry list
     */
    public EnquiryList getEnquiryList() {
        return enquiryList;
    }
    
    /**
     * Add a new enquiry to the system
     * 
     * @param enquiry The enquiry to add
     */
    public void addEnquiry(Enquiry enquiry) {
        enquiryList.addEnquiry(enquiry);
    }
    
    /**
     * Add a new project to the system
     * 
     * @param project The project to add
     */
    public void addProject(BTOProject project) {
        projects.add(project);
    }
    
    /**
     * Get the list of all users
     * 
     * @return The list of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    /**
     * Get user by NRIC
     * 
     * @param nric The NRIC of the user
     * @return The user with the specified NRIC, or null if not found
     */
    public User getUserByNRIC(String nric) {
        for (User user : users) {
            if (user.getUserID().equals(nric)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Reload all data from CSV files
     * Useful for refreshing data after changes
     */
    public void reloadAllData() {
        projects.clear();
        users.clear();
        enquiryList = new EnquiryList();
        
        loadProjectsFromCSV();
        loadApplicantsFromCSV();
        loadOfficersFromCSV();
        loadManagersFromCSV();
    }
}
