package Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Main.Personnel.*;

public class UserFileLoader {
    public static List<User> loadAllUsers(String applicantPath, String officerPath, String managerPath) {
        List<User> users = new ArrayList<>();
        users.addAll(loadApplicants(applicantPath));
        users.addAll(loadOfficers(officerPath));
        users.addAll(loadManagers(managerPath));
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
                boolean married = parts[3].equalsIgnoreCase("Married");
                String password = parts[4];
                applicants.add(new Applicant(name, nric, password, age, married));
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
                boolean married = parts[3].equalsIgnoreCase("Married");
                String password = parts[4];
                officers.add(new HDBOfficer(name, nric, password, age, married));
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
                boolean married = parts[3].equalsIgnoreCase("Married"); // Optional
                String password = parts[4];
                managers.add(new HDBManager(name, nric, password, age, married));
            }
        } catch (IOException e) {
            System.err.println("Error loading managers: " + e.getMessage());
        }
        return managers;
    }
}
