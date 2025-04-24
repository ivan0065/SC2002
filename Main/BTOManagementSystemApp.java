package Main;

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
}
