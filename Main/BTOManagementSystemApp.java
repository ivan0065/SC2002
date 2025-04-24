package Main;

import Main.BTO.BTOProject;
import Main.BTO.ProjectDatabase;
import Main.Personnel.*;
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
    System.out.println("Linking managers to projects...");
    
    // Create a map of managers by NRIC for quick lookup
    Map<String, HDBManager> managerMap = new HashMap<>();
    for (User user : users) {
        if (user instanceof HDBManager) {
            HDBManager manager = (HDBManager) user;
            managerMap.put(manager.getUserID(), manager);
            System.out.println("Found manager: " + manager.getUserID());
        }
    }
    
    // Link projects to managers
    for (BTOProject project : projectDatabase.getAllProjects()) {
        if (project.getHDBManagerInCharge() != null) {
            String managerNRIC = project.getHDBManagerInCharge().getUserID();
            HDBManager manager = managerMap.get(managerNRIC);
            
            if (manager != null) {
                // Update the project with the proper manager instance
                project.setHDBManagerInCharge(manager);
                
                // Add project to manager's managed projects
                manager.addManagedProject(project);
                System.out.println("Linked project " + project.getProjectName() + 
                                  " to manager " + managerNRIC);
            } else {
                System.out.println("WARNING: Manager " + managerNRIC + 
                                  " not found for project " + project.getProjectName());
            }
        }
    }
}
}
