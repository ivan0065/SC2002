package AppInterface;

import Main.BTO.BTOProject;
import Main.BTO.ProjectDatabase;
import Main.Enquiries.Enquiry;
import Main.Personnel.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class I_officer implements I_UserInterface {
    private HDBOfficer officer;
    private I_applicant applicantUI;
    private Scanner scanner = new Scanner(System.in);

    public I_officer(HDBOfficer officer) {
        this.officer = officer;
        this.applicantUI = new I_applicant(officer);
    }

    public void showMenu(){
        int choice;
        int officerChoice;
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
                        System.out.println("2. View details of Assigned Project");
                        System.out.println("3. View Projects Available to Join as an Officer");
                        System.out.println("4. Join Project");
                        System.out.println("5. View Registration Status");
                        System.out.println("6. View Enquires");
                        System.out.println("7. Reply to Enquiry");
                        System.out.println("8. Change Password");
                        System.out.println("9. Exit Officer Menu");
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
                                String projectName1 = scanner.nextLine();
                                officer.viewAssignedProjectDetails(projectName1);
                                break;
                            case 3:
                                officer.viewAvailableProjectsToJoin();
                                break;
                            case 4:
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
                                break;
                            case 5:
                                System.out.println("Enter project name to check registration status: ");
                                String projectName2 = scanner.nextLine();
                                ProjectDatabase projectDatabase1 = ProjectDatabase.getInstance();
                                BTOProject existingProject1 = projectDatabase1.getProjectByName(projectName2);
                                if (existingProject1 == null) {
                                    System.out.println("Project not found.");
                                    break;
                                }
                                System.out.println("Registration Status: " + officer.getRegistrationStatusForProject(existingProject1));
                                break;
                            case 6:
                                System.out.println("Enquiries:");
                                officer.ViewEnquiry();
                                break;
                            case 7:
                                // First, view all enquiries to see what's available
                                System.out.println("Viewing all enquiries for your assigned projects:");
                                officer.ViewEnquiry();
                                
                                System.out.print("\nEnter Enquiry ID to reply to: ");
                                try {
                                    int enquiryId = scanner.nextInt();
                                    scanner.nextLine(); // Consume newline character
                                    
                                    Enquiry enquiry = officer.getEnquiryByID(enquiryId);
                                    
                                    if (enquiry != null) {
                                        System.out.println("\nSelected Enquiry:");
                                        enquiry.printEnquiry();
                                        
                                        System.out.print("\nEnter your reply: ");
                                        String reply = scanner.nextLine();
                                        
                                        if (reply.trim().isEmpty()) {
                                            System.out.println("Reply cannot be empty. Operation cancelled.");
                                        } else {
                                            officer.replyEnquiry(enquiry, reply);
                                            System.out.println("Reply submitted successfully.");
                                            
                                            // Show the updated enquiry
                                            System.out.println("\nUpdated Enquiry:");
                                            enquiry.printEnquiry();
                                        }
                                    } else {
                                        System.out.println("No enquiry found with ID " + enquiryId + ".");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid input. Please enter a number for the enquiry ID.");
                                    scanner.nextLine(); // Clear the invalid input
                                } catch (Exception e) {
                                    System.out.println("An error occurred: " + e.getMessage());
                                }
                                break;
                                
                            case 8:
                                System.out.println("Change Password");
                                System.out.print("Enter your current password: ");
                                String currentPassword = scanner.nextLine();
                                
                                // Verify current password
                                if (officer.checkPassword(currentPassword)) {
                                    System.out.print("Enter new password: ");
                                    String newPassword = scanner.nextLine();
                                    System.out.print("Confirm new password: ");
                                    String confirmPassword = scanner.nextLine();
                                    
                                    if (newPassword.equals(confirmPassword)) {
                                        // Call the changePassword method from User superclass
                                        officer.changePassword(true, newPassword);
                                        System.out.println("Password successfully changed.");
                                    } else {
                                        System.out.println("Passwords do not match. Password change failed.");
                                    }
                                } else {
                                    System.out.println("Incorrect current password. Password change failed.");
                                }
                                break;
                            case 9:
                                return;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    } while (officerChoice != 9);
                    break;
                case 2:
                    applicantUI.show_menu();
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
