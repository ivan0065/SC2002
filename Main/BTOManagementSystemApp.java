package Main;

import Main.BTO.BTOProject;
import Main.BTO.ProjectDatabase;
import Main.Personnel.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BTOManagementSystemApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();

        List<User> users = UserFileLoader.loadAllUsers(
            "data/ApplicantList.csv", 
            "data/OfficerList.csv", 
            "data/ManagerList.csv", 
            projectDatabase
        );
        projectDatabase.setUsers(users);
        linkAllEntities(users, projectDatabase);
        fixManagerProjectLinks(users, projectDatabase);
        linkOfficersFromCSV(users, projectDatabase);
        BTOManagementSystem system = new BTOManagementSystem(users);

        boolean exitSystem = false;
        
        while (!exitSystem) {
            System.out.println("\n==== Welcome to the BTO Management System ====");
            System.out.println("1. Login");
            System.out.println("2. Exit System");
            System.out.print("Enter your choice: ");
            
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            
            switch (choice) {
                case 1:
                    loginUser(system, users);
                    break;
                case 2:
                    exitSystem = true;
                    System.out.println("Thank you for using the BTO Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void fixManagerProjectLinks(List<User> users, ProjectDatabase projectDatabase) {
        // Create a map of managers by name for easy lookup
        Map<String, HDBManager> managersByName = new HashMap<>();
        for (User user : users) {
            if (user instanceof HDBManager) {
                HDBManager manager = (HDBManager) user;
                String name = manager.getName();
                managersByName.put(name, manager);
                System.out.println("Found manager: " + name);
            }
        }
        
        // Link projects to managers by name
        for (BTOProject project : projectDatabase.getAllProjects()) {
            HDBManager placeholderManager = project.getHDBManagerInCharge();
            if (placeholderManager != null) {
                // In our updated code, the placeholder manager's userID contains the name from CSV
                String managerName = placeholderManager.getUserID();
                System.out.println("Project " + project.getProjectName() + " is looking for manager: " + managerName);
                
                HDBManager actualManager = managersByName.get(managerName);
                if (actualManager != null) {
                    // Replace placeholder with actual manager
                    project.setHDBManagerInCharge(actualManager);
                    
                    // Add project to manager's managed projects
                    actualManager.addManagedProject(project);
                    System.out.println("Linked project " + project.getProjectName() + " to manager " + managerName);
                } else {
                    System.out.println("WARNING: Could not find manager with name: " + managerName);
                }
            }
        }
        
        // Verify all managers have their projects
        System.out.println("\n===== VERIFICATION =====");
        for (HDBManager manager : managersByName.values()) {
            List<BTOProject> managedProjects = manager.getManagedProject();
            int projectCount = (managedProjects != null) ? managedProjects.size() : 0;
            System.out.println("Manager " + manager.getName() + " has " + projectCount + " projects");
            
            if (managedProjects != null && !managedProjects.isEmpty()) {
                for (BTOProject project : managedProjects) {
                    System.out.println("  - " + project.getProjectName());
                }
            }
        }
    }
        

    
    private static void loginUser(BTOManagementSystem system, List<User> users) {
        System.out.print("Enter UserID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = authenticate(userId, password, users);
        if (user == null) {
            System.out.println("Invalid credentials. Please try again.");
            return;
        }
        
        System.out.println("Login successful! Welcome, " + userId);
        user.getUserInterface().launchInterface();
        System.out.println("Logged out successfully.");
    }

    private static User authenticate(String userId, String password, List<User> users) {
        for (User user : users) {
            if (user.getUserID().equals(userId) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }

    private static void linkAllEntities(List<User> users, ProjectDatabase projectDatabase) {
        System.out.println("Starting entity linking process...");
        
        // Create maps for quick lookup
        Map<String, HDBManager> managerMap = new HashMap<>();
        Map<String, HDBOfficer> officerMap = new HashMap<>();
        Map<String, Applicant> applicantMap = new HashMap<>();
        
        // Categorize users by type
        for (User user : users) {
            if (user instanceof HDBManager) {
                HDBManager manager = (HDBManager) user;
                managerMap.put(manager.getUserID(), manager);
                System.out.println("Found manager: " + manager.getUserID() + " (" + manager.getName() + ")");
            } 
            else if (user instanceof HDBOfficer) {
                HDBOfficer officer = (HDBOfficer) user;
                officerMap.put(officer.getUserID(), officer);
                System.out.println("Found officer: " + officer.getUserID() + " (" + officer.getName() + ")");
            }
            else if (user instanceof Applicant) {
                Applicant applicant = (Applicant) user;
                applicantMap.put(applicant.getUserID(), applicant);
            }
        }
        
        // Link managers to projects
        for (BTOProject project : projectDatabase.getAllProjects()) {
            HDBManager placeholderManager = project.getHDBManagerInCharge();
            if (placeholderManager != null) {
                String managerNRIC = placeholderManager.getUserID();
                
                // Try to find manager by NRIC
                HDBManager actualManager = managerMap.get(managerNRIC);
                
                if (actualManager != null) {
                    // Update the project with the proper manager instance
                    project.setHDBManagerInCharge(actualManager);
                    
                    // Add project to manager's managed projects
                    actualManager.addManagedProject(project);
                    System.out.println("Linked project " + project.getProjectName() + 
                                    " to manager " + managerNRIC);
                } else {
                    // Try to find by name (if NRIC is actually the name in CSV)
                    for (HDBManager manager : managerMap.values()) {
                        if (manager.getName().equalsIgnoreCase(managerNRIC)) {
                            project.setHDBManagerInCharge(manager);
                            manager.addManagedProject(project);
                            System.out.println("Linked project " + project.getProjectName() + 
                                            " to manager " + manager.getUserID() + " by name match");
                            break;
                        }
                    }
                }
            }
        }
        
        // Link officers to projects using ProjectList.csv directly
        try {
            File file = new File("data/ProjectList.csv");
            Scanner scanner = new Scanner(file);
            
            // Skip header
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length >= 13) {
                    String projectName = parts[0].trim();
                    String officerData = parts[12].trim();
                    
                    // Find the project
                    BTOProject project = null;
                    for (BTOProject p : projectDatabase.getAllProjects()) {
                        if (p.getProjectName().equals(projectName)) {
                            project = p;
                            break;
                        }
                    }
                    
                    if (project != null && !officerData.isEmpty() && !officerData.equalsIgnoreCase("null")) {
                        // Parse officer data (could be comma or semicolon separated)
                        String[] officerNames = officerData.split("[,;]");
                        
                        for (String officerName : officerNames) {
                            officerName = officerName.trim();
                            
                            // Try to find officer by name
                            for (HDBOfficer officer : officerMap.values()) {
                                if (officer.getName().equalsIgnoreCase(officerName)) {
                                    // Link officer to project
                                    project.addHDBOfficer(officer);
                                    
                                    // Also add project to officer's assigned projects
                                    List<BTOProject> assignedProjects = officer.getAssignedProjects();
                                    if (assignedProjects == null) {
                                        assignedProjects = new ArrayList<>();
                                        // You'd need a setter for this case, not shown here
                                    }
                                    
                                    // Check if already assigned to avoid duplicates
                                    boolean alreadyAssigned = false;
                                    for (BTOProject p : assignedProjects) {
                                        if (p.getProjectName().equals(project.getProjectName())) {
                                            alreadyAssigned = true;
                                            break;
                                        }
                                    }
                                    
                                    if (!alreadyAssigned) {
                                        assignedProjects.add(project);
                                    }
                                    
                                    System.out.println("Linked officer " + officer.getUserID() + 
                                                    " (" + officer.getName() + ") to project " + 
                                                    projectName);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println("Error linking officers to projects: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Verification of links
        System.out.println("\n===== VERIFICATION =====");
        
        // Verify manager links
        for (HDBManager manager : managerMap.values()) {
            List<BTOProject> managedProjects = manager.getManagedProject();
            int projectCount = (managedProjects != null) ? managedProjects.size() : 0;
            System.out.println("Manager " + manager.getName() + " has " + projectCount + " projects");
            
            if (managedProjects != null && !managedProjects.isEmpty()) {
                for (BTOProject project : managedProjects) {
                    System.out.println("  - " + project.getProjectName());
                }
            }
        }
        
        // Verify officer links
        for (HDBOfficer officer : officerMap.values()) {
            List<BTOProject> assignedProjects = officer.getAssignedProjects();
            int projectCount = (assignedProjects != null) ? assignedProjects.size() : 0;
            System.out.println("Officer " + officer.getName() + " has " + projectCount + " projects");
            
            if (assignedProjects != null && !assignedProjects.isEmpty()) {
                for (BTOProject project : assignedProjects) {
                    System.out.println("  - " + project.getProjectName());
                }
            }
        }
    }
    private static void linkOfficersFromCSV(List<User> users, ProjectDatabase projectDatabase) {
        System.out.println("Linking officers to projects...");
    
        // Create a map of officers by name for easy lookup
        Map<String, HDBOfficer> officerMap = new HashMap<>();
        for (User user : users) {
            if (user instanceof HDBOfficer) {
                HDBOfficer officer = (HDBOfficer) user;
                officerMap.put(officer.getName(), officer);
            }
        }
        
        try {
            File file = new File("data/ProjectList.csv");
            Scanner scanner = new Scanner(file);
            
            // Skip header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                // Handle the CSV line manually to account for quoted fields
                List<String> fields = parseCSVLine(line);
                
                if (fields.size() >= 13) {
                    String projectName = fields.get(0);
                    String officerData = fields.get(12);
                    
                    // Process officer data only if it's not empty
                    if (officerData != null && !officerData.isEmpty() && !officerData.equals("null")) {
                        // Find the project by name
                        BTOProject project = projectDatabase.getProjectByName(projectName);
                        if (project != null) {
                            // Split officer data by comma (officers might be comma-separated)
                            for (String officerName : officerData.split(",")) {
                                officerName = officerName.trim();
                                
                                // Find the officer by name
                                HDBOfficer officer = officerMap.get(officerName);
                                if (officer != null) {
                                    // Link the officer to the project
                                    project.addHDBOfficer(officer);
                                    officer.joinProject(project,true);
                                    // Add the project to the officer's list of assigned projects
                                    if (officer.getAssignedProjects() == null) {
                                        System.out.println("Officer's assigned projects list is null");
                                    } else {
                                        officer.getAssignedProjects().add(project);
                                        System.out.println("Linked officer " + officerName + " to project " + projectName);
                                    }
                                } else {
                                    System.out.println("Could not find officer: " + officerName);
                                }
                            }
                        } else {
                            System.out.println("Could not find project: " + projectName);
                        }
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("ProjectList.csv file not found: " + e.getMessage());
        }
    }
    private static List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString().trim());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        
        // Add the last field
        result.add(field.toString().trim());
        
        return result;
    }
}
