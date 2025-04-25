package AppInterface;

import Main.BTO.BTOProject;
import Main.BTO.ProjectDatabase;
import Main.Enums.FlatType;
import Main.Personnel.Applicant;
import Main.Personnel.HDBOfficer;
import java.util.List;
import java.util.Scanner;

public class I_applicant implements I_UserInterface{
    private Applicant applicant;
    
    public I_applicant(Applicant applicant) {
        this.applicant = applicant;
    }
    
    public I_applicant(HDBOfficer officer) {
        this.applicant = officer;
    }
    public void show_menu(){
        int choice=9;
        System.out.println("Welcome to the Applicant Portal!");
        do {
            System.out.println("1.Apply for BTO");
            System.out.println("2.Withdraw Application");
            System.out.println("3.View Application Status");
            System.out.println("4.Make Enquiry");
            System.out.println("5.Delete Enquiry");
            System.out.println("6.Edit Enquiry");
            System.out.println("7.View Enquirys");
            System.out.println("8.View Open Projects");
            System.out.println("9.Change Password");
            System.out.println("10.Exit");
            System.out.print("Please select an option: ");
            Scanner scanner = new Scanner(System.in);
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1:
                    System.out.println("Enter project Name to apply for BTO:");
                    String projectId = scanner.nextLine(); // Get project ID from user input

                    System.out.println("Enter flat type(2 or 3)");
                    int flatType = scanner.nextInt(); // Get flat type from user input
                    scanner.nextLine(); // Consume the leftover newline
                    FlatType flatType1 = null;
                    if (flatType == 2) {
                        flatType1 = FlatType.Two_Room; // Set flat type to TWO_ROOM
                    } else if (flatType == 3) {
                        flatType1 = FlatType.Three_Room; // Set flat type to THREE_ROOM
                    } else {
                        System.out.println("Invalid flat type. Please enter 2 or 3.");
                        continue; // Skip to the next iteration of the loop
                    }
                    System.out.println(projectId);
                    applicant.applyBTO(projectId, flatType1); // Call applyBTO method with user input
                    break;
                case 2:
                    System.out.println("Enter application ID to withdraw application:");
                    String applicationId = scanner.next(); // Get application ID from user input
                    applicant.withdrawApplication(applicationId); // Call withdrawApplication method with user input
                    break;
                case 3:
                    System.out.println(applicant.viewApplicationStatus()); // Call viewApplicationStatus method with user input
                    break;
                case 4:
                    System.out.println("Available projects to enquire about:");
                    ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
                    List<BTOProject> availableProjects = projectDatabase.getAllProjects();
                    
                    if (availableProjects.isEmpty()) {
                        System.out.println("No projects available.");
                        break;
                    }
                    
                    for (int i = 0; i < availableProjects.size(); i++) {
                        BTOProject project = availableProjects.get(i);
                        System.out.println((i+1) + ". " + project.getProjectName());
                    }
                    
                    System.out.print("Enter project name to make an enquiry: ");
                    String projectName = scanner.nextLine().trim();
                    
                    // Verify the project exists
                    BTOProject selectedProject = projectDatabase.getProjectByName(projectName);
                    if (selectedProject == null) {
                        System.out.println("Project not found: " + projectName);
                        break;
                    }
                    
                    System.out.print("Enter your question: ");
                    String question = scanner.nextLine();
                    
                    if (question.isEmpty()) {
                        System.out.println("Question cannot be empty.");
                        break;
                    }
                    
                    int enquiryId = applicant.addEnquiry(question, projectName);
                    
                    if (enquiryId > 0) {
                        System.out.println("Enquiry created successfully with ID: " + enquiryId);
                    } else {
                        System.out.println("Failed to create enquiry.");
                    }
                    break;
                case 5:
                    System.out.println("Enter project to make an enquiry:");
                    String project1 = scanner.next(); // Get enquiry ID from user input
                    System.out.println("Enter enquiry ID to delete an enquiry:");
                    Integer enquiryIdToDelete = scanner.nextInt(); // Get enquiry ID from user input
                    boolean deleted=applicant.removeEnquiry(enquiryIdToDelete,project1); // Call deleteEnquiry method with user input
                    if(deleted){
                        System.out.println("Enquiry deleted successfully.");
                    }else{
                        System.out.println("Failed to delete enquiry. Please check the ID and try again.");
                    }
                    break;
                case 6:
                    System.out.println("Enter project to edit an enquiry:");
                    String project2 = scanner.next(); // Get enquiry ID from user input
                    System.out.println("Enter enquiry ID to edit an enquiry:");
                    Integer enquiryIdToEdit = scanner.nextInt(); // Get enquiry ID from user input
                    System.out.println("Enter new question:");
                    scanner.nextLine(); // Consume any previous newline character
                    String newQuestion = scanner.nextLine(); // Get new question from user input
                    applicant.editEnquiry(enquiryIdToEdit, newQuestion,project2); // Call editEnquiry method with user input
                    break;
                case 7:
                    System.out.println("Viewing all enquiries for the user:");
                    applicant.viewEnquiry(); // Call viewEnquiry method with user input
                    break;
                case 8:
                    System.out.println("Viewing open projects for the user group:");
                    applicant.viewOpenToUserGroup(); // Call viewOpenToUserGroup method with user input
                    break;
                case 9:
                    System.out.println("Change Password");
                    System.out.print("Enter your current password: ");
                    String currentPassword = scanner.nextLine();
                    
                    // Verify current password
                    if (applicant.checkPassword(currentPassword)) {
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        System.out.print("Confirm new password: ");
                        String confirmPassword = scanner.nextLine();
                        
                        if (newPassword.equals(confirmPassword)) {
                            // Call the changePassword method from User superclass
                            applicant.changePassword(true, newPassword);
                            System.out.println("Password successfully changed.");
                        } else {
                            System.out.println("Passwords do not match. Password change failed.");
                        }
                    } else {
                        System.out.println("Incorrect current password. Password change failed.");
                    }
                    break;
                case 10:
                    System.out.println("Exiting the Applicant Portal. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 10);
    }
    
    @Override
    public void launchInterface() {
    	show_menu(); //delegate to existing method
    }
}
