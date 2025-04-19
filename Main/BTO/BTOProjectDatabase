package Main.Manager_control;

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
import Main.Enums.SortType;
import Main.Enums.FlatType;

/**
 * ProjectDatabase class that manages all BTO projects and their enquiries
 * Reads project data from CSV file and provides access to project information
 */
public class ProjectDatabase {
    private List<BTOProject> projects;
    private EnquiryList enquiryList;
    private static final String CSV_FILE_PATH = "ProjectList.csv";
    
    /**
     * Constructor for ProjectDatabase
     * Initializes empty lists and loads project data from CSV
     */
    public ProjectDatabase() {
        this.projects = new ArrayList<>();
        this.enquiryList = new EnquiryList();
        loadProjectsFromCSV();
    }
    
    /**
     * Loads project data from the CSV file using Scanner
     */
    private void loadProjectsFromCSV() {
        try {
            File file = new File(CSV_FILE_PATH);
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
                    
                    // Create and add the project
                    BTOProject project = new BTOProject(
                        projectName, neighborhood, openingDate, closingDate, 
                        managerNRIC, officerSlot
                    );
                    
                    // Add flat types
                    project.addFlatType(flatType1, unitsType1, priceType1);
                    project.addFlatType(flatType2, unitsType2, priceType2);
                    
                    // Add officer if present
                    if (!officerNRIC.isEmpty() && !officerNRIC.equalsIgnoreCase("null")) {
                        String[] officers = officerNRIC.split(";");
                        for (String officer : officers) {
                            project.addOfficer(officer.trim());
                        }
                    }
                    
                    projects.add(project);
                    
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line);
                    System.err.println("Error details: " + e.getMessage());
                }
                
                lineScanner.close();
            }
            
            scanner.close();
            System.out.println("Successfully loaded " + projects.size() + " projects from CSV.");
            
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing project data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to parse flat type string to enum
     */
    private FlatType parseFlatType(String typeStr) {
        if (typeStr.contains("2-Room")) {
            return FlatType.TWO_ROOM;
        } else if (typeStr.contains("3-Room")) {
            return FlatType.THREE_ROOM;
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
    public void getBTOProjectsList(SortType sortFilter) {
        List<BTOProject> filteredProjects = new ArrayList<>(projects);
        
        // Apply sorting/filtering based on the provided enum
        switch(sortFilter) {
            case ALPHABETICAL:
                filteredProjects.sort((p1, p2) -> p1.getProjectName().compareTo(p2.getProjectName()));
                break;
            case NEIGHBORHOOD:
                filteredProjects.sort((p1, p2) -> p1.getNeighborhood().compareTo(p2.getNeighborhood()));
                break;
            case DATE_ASCENDING:
                filteredProjects.sort((p1, p2) -> p1.getOpeningDate().compareTo(p2.getOpeningDate()));
                break;
            case DATE_DESCENDING:
                filteredProjects.sort((p1, p2) -> p2.getOpeningDate().compareTo(p1.getOpeningDate()));
                break;
            case VISIBLE_ONLY:
                filteredProjects.removeIf(p -> !p.isVisible());
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
                    // We need to check if the enquiry has a reply
                    // Since the Enquiry class might not have hasReply(), we can adjust this
                    // based on your implementation
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
            System.out.println("Neighborhood: " + project.getNeighborhood());
            System.out.println("Application Period: " + project.getOpeningDate() + 
                             " to " + project.getClosingDate());
            System.out.println("Visibility: " + (project.isVisible() ? "Visible" : "Hidden"));
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
        for (BTOProject project : projects) {
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
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
     * Reload projects from CSV file
     * Useful for refreshing data after changes
     */
    public void reloadProjects() {
        projects.clear();
        loadProjectsFromCSV();
    }
}
