package Main;

import Main.BTO.BTOProject;
import Main.BTO.ProjectDatabase;
import Main.Enums.MaritalStatus;
import Main.Personnel.*;
import Main.interfaces.I_RegistrationManager;
import Main.interfaces.I_applicationManager;
import Main.interfaces.I_projectManager;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserFileLoader {
    private static ProjectDatabase projectDatabase;
    private static Map<String, HDBManager> managersByNRIC = new HashMap<>();
    private static Map<String, HDBOfficer> officersByNRIC = new HashMap<>();
    private static Map<String, Applicant> applicantsByNRIC = new HashMap<>();

    public static List<User> loadAllUsers(String applicantPath, String officerPath, String managerPath,ProjectDatabase db) {
        projectDatabase = db;
        List<User> users = new ArrayList<>();
        users.addAll(loadApplicants(applicantPath));
        users.addAll(loadOfficers(officerPath));
        users.addAll(loadManagers(managerPath));
        // Now link managers and officers to projects
        linkManagersToProjects();
        linkOfficersToProjects();
        return users;
    }

    private static List<User> loadApplicants(String filePath) {
        List<User> applicants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String nric = parts[1];
                int age = Integer.parseInt(parts[2]);
                MaritalStatus maritalStatus = parseMaritalStatus(parts[3]);
                String password = parts[4];
                applicants.add(new Applicant(name, nric, password, age, maritalStatus));
            }
        } catch (IOException e) {
            System.err.println("Error loading applicants: " + e.getMessage());
        }
        return applicants;
    }

    private static List<User> loadOfficers(String filePath) {
        List<User> officers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String nric = parts[1];
                int age = Integer.parseInt(parts[2]);
                MaritalStatus maritalStatus = parseMaritalStatus(parts[3]);
                String password = parts[4];
                HDBOfficer officer = new HDBOfficer(name, nric, password, age, maritalStatus);
                officers.add(officer);
                officersByNRIC.put(nric, officer); // Store officer by NRIC for quick access
                System.out.println("Loaded officer: " + officer.getUserID());
            }
        } catch (IOException e) {
            System.err.println("Error loading officers: " + e.getMessage());
        }
        return officers;
    }

    private static List<User> loadManagers(String filePath) {
        List<User> managers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String nric = parts[1];
                int age = Integer.parseInt(parts[2]); // Optional
                MaritalStatus maritalStatus = parseMaritalStatus(parts[3]); // Optional
                String password = parts[4];
                I_projectManager projectManager = new Main.Manager_control.ProjectManager();
                I_RegistrationManager registrationManager = new Main.Manager_control.RegistrationManager();
                I_applicationManager applicationManager = new Main.Manager_control.ApplicationManager();
                
                HDBManager manager = new HDBManager(name, nric, password, age, maritalStatus, 
                     projectManager, registrationManager, applicationManager);
                managers.add(manager);
                managersByNRIC.put(nric, manager); 
            }
        } catch (IOException e) {
            System.err.println("Error loading managers: " + e.getMessage());
        }
        return managers;
    }
    private static MaritalStatus parseMaritalStatus(String status) {
        if (status == null) return MaritalStatus.SINGLE;
        
        status = status.trim().toUpperCase();
        
        switch (status) {
            case "MARRIED":
                return MaritalStatus.MARRIED;
            case "DIVORCED":
                return MaritalStatus.DIVORCED;
            case "WIDOWED":
                return MaritalStatus.WIDOWED;
            case "SINGLE":
            default:
                return MaritalStatus.SINGLE;
        }
    }
    private static void linkManagersToProjects() {
    if (projectDatabase == null) return;
    
    for (BTOProject project : projectDatabase.getAllProjects()) {
        // Check if the project has a manager assigned by NRIC
        if (project.getHDBManagerInCharge() != null) {
            String managerNRIC = project.getHDBManagerInCharge().getUserID();
            
            if (managerNRIC != null && managersByNRIC.containsKey(managerNRIC)) {
                HDBManager manager = managersByNRIC.get(managerNRIC);
                project.setHDBManagerInCharge(manager);
                
                // Also add this project to the manager's list of managed projects
                if (manager != null) {
                    manager.addManagedProject(project);
                    System.out.println("Linked project " + project.getProjectName() + 
                                      " to manager " + managerNRIC);
                }
            }
        }
    }
}
    
    private static void linkOfficersToProjects() {
        if (projectDatabase == null) return;
        // Cache officers for quick lookup
        Map<String, HDBOfficer> officerMap = new HashMap<>();
        for (User user : projectDatabase.getAllUsers()) {
            if (user instanceof HDBOfficer) {
                HDBOfficer officer = (HDBOfficer) user;
                officersByNRIC.put(officer.getUserID(), officer);
                officerMap.put(officer.getUserID(), officer);
                System.out.println("Found officer: " + officer.getUserID());
            }
        }
        for (BTOProject project : projectDatabase.getAllProjects()) {
            System.out.println("Processing project: " + project.getProjectName());
            
            // Parse officers from project data
            // The officer field in ProjectList.csv has comma-separated NRICs
            String projectName = project.getProjectName();
            
            // Get raw officer data from CSV (assuming it's in "Daniel,Emily" format)
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
                    
                    // Check if this line is for our project
                    if (parts.length >= 13 && parts[0].trim().equals(projectName)) {
                        String officerData = parts[12].trim();
                        
                        if (!officerData.isEmpty() && !officerData.equalsIgnoreCase("null")) {
                            String[] officerNames = officerData.split(";");
                            
                            for (String officerName : officerNames) {
                                // Find officer by name in our officer map
                                for (HDBOfficer officer : officerMap.values()) {
                                    if (officer.getName().trim().equalsIgnoreCase(officerName.trim())) {
                                        System.out.println("Linking officer " + officer.getUserID() + " to project " + projectName);
                                        project.addHDBOfficer(officer);
                                        officer.getAssignedProjects().add(project);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                scanner.close();
            } catch (Exception e) {
                System.err.println("Error linking officers to project: " + e.getMessage());
                e.printStackTrace();
            }
        } 
        // Verify officer assignments
        System.out.println("\n===== OFFICER ASSIGNMENTS =====");
        for (HDBOfficer officer : officerMap.values()) {
            List<BTOProject> assignedProjects = officer.getAssignedProjects();
            int projectCount = (assignedProjects != null) ? assignedProjects.size() : 0;
            System.out.println("Officer " + officer.getName() + " (" + officer.getUserID() + ") has " + projectCount + " projects");
            
            if (assignedProjects != null && !assignedProjects.isEmpty()) {
                for (BTOProject project : assignedProjects) {
                    System.out.println("  - " + project.getProjectName());
                }
            }
        }
    }

    // Helper method to get a manager by NRIC
    public static HDBManager getManagerByNRIC(String nric) {
        return managersByNRIC.get(nric);
    }
    
    // Helper method to get an officer by NRIC
    public static HDBOfficer getOfficerByNRIC(String nric) {
        return officersByNRIC.get(nric);
    }
    
    // Helper method to get an applicant by NRIC
    public static Applicant getApplicantByNRIC(String nric) {
        return applicantsByNRIC.get(nric);
    }
}
