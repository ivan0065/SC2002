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

        user.getUserInterface().launchInterface();
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
