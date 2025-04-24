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

public class UserFileLoader {
    private static ProjectDatabase projectDatabase;
    private static Map<String, HDBManager> managersByNRIC = new HashMap<>();
    private static Map<String, HDBOfficer> officersByNRIC = new HashMap<>();
    private static Map<String, Applicant> applicantsByNRIC = new HashMap<>();

    public static List<User> loadAllUsers(String applicantPath, String officerPath, String managerPath) {
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
                officers.add(new HDBOfficer(name, nric, password, age, maritalStatus));
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
                        
                        managers.add(new HDBManager(name, nric, password, age, maritalStatus, 
                                     projectManager, registrationManager, applicationManager));
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
            String managerNRIC = project.getHDBManagerInCharge() != null ? 
                                 project.getHDBManagerInCharge().getUserID() : null;
            
            if (managerNRIC != null && managersByNRIC.containsKey(managerNRIC)) {
                project.setHDBManagerInCharge(managersByNRIC.get(managerNRIC));
            }
        }
    }
    
    private static void linkOfficersToProjects() {
        if (projectDatabase == null) return;
        
        for (BTOProject project : projectDatabase.getAllProjects()) {
            // For each project, check officers assigned by NRIC
            List<String> officerNRICs = new ArrayList<>();
            
            // Extract officer NRICs from project (you'll need to implement this 
            // based on how your ProjectDatabase stores this information)
            // This is just a placeholder:
            // officerNRICs = project.getOfficerNRICList();
            
            for (String officerNRIC : officerNRICs) {
                if (officersByNRIC.containsKey(officerNRIC)) {
                    HDBOfficer officer = officersByNRIC.get(officerNRIC);
                    project.addHDBOfficer(officer);
                    officer.joinProject(project);
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
