package AppInterface;

import Main.BTO.BTOProject;

import Main.BTO.ProjectDatabase;
import Main.Enquiries.Enquiry;
import Main.Enums.*;
import Main.Manager_control.RegistrationManager;
import Main.Personnel.*;

import java.util.List;
import java.util.Scanner;

public class I_officer implements I_UserInterface {
    private HDBOfficer officer;
    private I_applicant applicantUI;
    private RegistrationManager regManager = new RegistrationManager();
    private Scanner scanner = new Scanner(System.in);

    public I_officer(HDBOfficer officer) {
        this.officer = officer;
        this.applicantUI = new I_applicant(officer);
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
                        System.out.println("2. View Projects Available to Join as an Officer");
                        System.out.println("3. Join Project");
                        System.out.println("4. View Registration Status");
                        System.out.println("5. View Enquires");
                        System.out.println("6. Reply to Enquiry");
                        System.out.println("7. Exit Officer Menu");
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
                                viewAvailableProjectsToJoin();
                                break;
                            case 3:
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
                            case 4:
                                System.out.println("Registration Status: " + officer.getHDBOfficerRegistrationStatus());
                                break;
                            case 5:
                                System.out.println("Enquiries:");
                                officer.ViewEnquiry();
                                break;
                            case 6:
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
                            case 7:
                                return;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    } while (officerChoice != 7);
                    break;
                case 2:
                    applicantUI.show_menu();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
        } while (choice!=3);
    }
    
    private void viewAvailableProjectsToJoin() {
        List<BTOProject> joinableProjects = regManager.getAvailForRegistration(officer);

        if (joinableProjects.isEmpty()) {
            System.out.println("No available projects to register for at the moment.");
            return;
        }

        System.out.println("\nAvailable Projects to Join:");
        for (int i = 0; i < joinableProjects.size(); i++) {
            BTOProject project = joinableProjects.get(i);
            System.out.println((i + 1) + ". " + project.getProjectName() + 
                " | Remaining Officer Slots: " + project.getRemainingOfficerSlots());
        }

        System.out.print("Enter the number of the project to join (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= joinableProjects.size()) {
            BTOProject selectedProject = joinableProjects.get(choice - 1);
            officer.joinProject(selectedProject);
            System.out.println("Successfully joined project: " + selectedProject.getProjectName());
        } else {
            System.out.println("Cancelled or invalid choice.");
        }
    }


	@Override
    public void launchInterface() {
        showMenu();
    }

}
