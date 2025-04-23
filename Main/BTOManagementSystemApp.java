package Main;

import AppInterface.I_applicant;
import AppInterface.I_manager;
import Main.Personnel.*;
import Main.Enums.FlatType;
import Main.Enums.UserRole;

import java.util.*;

public class BTOManagementSystemApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        List<User> users = new ArrayList<>();
        /**
        create sample users (with at least one of each userRole and load csv file to add users into 'users' list
        **/

        BTOManagementSystem system = new BTOManagementSystem(users);

        System.out.println("Welcome to the BTO Management System");
        System.out.print("Enter UserID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = authenticate(userId, password, users);
        if (user == null) {
            System.out.println("Invalid credentials. Exiting.");
            return;
        }

        // Role-based routing using userRole attribute
        UserRole role = user.getRole();
        switch (role) {
            case APPLICANT:
                I_applicant applicantUI = new I_applicant((Applicant) user);
                applicantUI.show_menu();
                break;
            case OFFICER:
            	I_officer officerUI = new I_officer((Officer) user);
            	officerUI.show_menu();
            case MANAGER:
                I_manager managerUI = new I_manager((HDBManager) user);
                managerUI.showMenu();
                break;
            default:
                System.out.println("Unknown user role. Exiting.");
        }
    }

    private static User authenticate(String userId, String password, List<User> users) {
        for (User user : users) {
            if (user.getUserID().equals(userId) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
