package AppInterface;

import Main.BTO.BTOProject;
import Main.BTO.ProjectDatabase;
import Main.Enquiries.Enquiry;
import Main.Enums.*;
import Main.Personnel.*;
import java.util.Scanner;

public class I_officer implements I_UserInterface {
    private HDBOfficer officer;
    private Scanner scanner = new Scanner(System.in);

    public I_officer(HDBOfficer officer) {
        this.officer = officer;
    }

    public void showMenu(){
        int choice;
        int officerChoice;
        int applicantchoice;
        do { 
            System.out.println("Welcome to the HDB Officer Interface!");
            System.out.println("1. View Officer Menu");
            System.out.println("2. View Applicant Menu");
            System.out.println("3. Exit");
            System.out.print("Please select an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            switch (choice){
                case 1:
                    do { 
                        System.out.println("Officer Menu:");
                        System.out.println("1. View Assigned Projects");
                        System.out.println("2. Join Project");
                        System.out.println("3. View Registration Status");
                        System.out.println("4. View Enquires");
                        System.out.println("5. Reply to Enquiry");
                        System.out.println("6. Exit Officer Menu");
                        System.out.print("Please select an option: ");
                        officerChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        switch (officerChoice) {
                            case 1:
                                System.out.println("Assigned Projects:");
                                for (BTOProject project : officer.getAssignedProjects()) {
                                    System.out.println(project.getProjectName());
                                }
                                break;
                            case 2:
                                System.out.print("Enter Project Name: ");
                                String projectName = scanner.nextLine();
                                // should go through DB and find the project
                                ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
                                BTOProject existingProject = projectDatabase.getProjectByName(projectName);
                                if (existingProject == null) {
                                    System.out.println("Project not found.");
                                    break;
                                }
                                officer.joinProject(existingProject);
                                System.out.println("Joined Project: " + projectName);
                                break;
                            case 3:
                                System.out.println("Registration Status: " + officer.getHDBOfficerRegistrationStatus());
                                break;
                            case 4:
                                System.out.println("Enquiries:");
                                officer.ViewEnquiry();
                                break;
                            case 5:
                                System.out.print("Enter Enquiry ID to reply: ");
                                int enquiryId = scanner.nextInt();
                                scanner.nextLine(); // Consume newline character
                                Enquiry enquiry=officer.getEnquiryByID(enquiryId);
                                if (enquiry == null) {
                                    System.out.println("Enquiry not found.");
                                    break;
                                }
                                System.out.print("Enter Reply: ");
                                String reply = scanner.nextLine();
                                officer.replyEnquiry(enquiry, reply);
                                break;
                            case 6:
                                return;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    } while (officerChoice != 6);
                    break;
                case 2:
                    do {
                        System.out.println("Welcome to the Applicant Portal!");
                        System.out.println("1.Apply for BTO");
                        System.out.println("2.View Application Status");
                        System.out.println("3.Make Enquiry");
                        System.out.println("4.Delete Enquiry");
                        System.out.println("5.View Open Projects");
                        Scanner scanner = new Scanner(System.in);
                        applicantchoice = scanner.nextInt();
                        switch (applicantchoice) {
                            case 1:
                                System.out.println("Enter project ID and flat type to apply for BTO:");
                                String projectId = scanner.next(); // Get project ID from user input
                                String flatType = scanner.next(); // Get flat type from user input
                                FlatType flatType1 = FlatType.valueOf(flatType.toUpperCase());
                                officer.applyBTO(projectId, flatType1); // Call applyBTO method with user input
                                break;
                            case 2:
                                officer.viewApplicationStatus(); // Call viewApplicationStatus method with user input
                                break;
                            case 3:
                                System.out.println("Enter enquiry ID to make an enquiry:");
                                String enquiryId = scanner.next(); // Get enquiry ID from user input
                                officer.createEnquiry(enquiryId); // Call makeEnquiry method with user input
                                break;
                            case 4:
                                System.out.println("Enter enquiry ID to delete an enquiry:");
                                String enquiryIdToDelete = scanner.next(); // Get enquiry ID from user input
                                officer.deleteEnquiry(enquiryIdToDelete); // Call deleteEnquiry method with user input
                                break;
                            case 5:
                                System.out.println("Viewing open projects for the user group:");
                                officer.viewOpenToUserGroup(); // Call viewOpenToUserGroup method with user input
                                break;
                            default:
                                System.out.println("Invalid choice, please try again.");
                        }
                    } while (applicantchoice != 5);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
        } while (choice!=3);
    }
    @Override
    public void launchInterface() {
        showMenu();
    }

}