package AppInterface;

import Main.Enums.FlatType;
import Main.Personnel.Applicant;
import java.util.Scanner;

public class I_applicant implements I_UserInterface{
    private Applicant applicant;
    
    public I_applicant(Applicant applicant) {
        this.applicant = applicant;
    }
    
    public void show_menu(){
        int choice;
        do {
            System.out.println("Welcome to the Applicant Portal!");
            System.out.println("1.Apply for BTO");
            System.out.println("2.Withdraw Application");
            System.out.println("3.View Application Status");
            System.out.println("4.Make Enquiry");
            System.out.println("5.Delete Enquiry");
            System.out.println("6.View Open Projects");
            System.out.println("7.Exit");
            System.out.print("Please select an option: ");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter project Name to apply for BTO:");
                    scanner.nextLine(); // Consume any previous newline character
                    String projectId = scanner.nextLine(); // Get project ID from user input
                    
                    System.out.println("Enter flat type(2 or 3)");
                    int flatType;
                    try {
                        flatType = Integer.parseInt(scanner.nextLine()); // Use nextLine and parse to int
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        continue;
                    }
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
                    applicant.viewApplicationStatus(); // Call viewApplicationStatus method with user input
                    break;
                case 4:
                    System.out.println("Enter enquiry ID to make an enquiry:");
                    String enquiryId = scanner.next(); // Get enquiry ID from user input
                    applicant.createEnquiry(enquiryId); // Call makeEnquiry method with user input
                    break;
                case 5:
                    System.out.println("Enter enquiry ID to delete an enquiry:");
                    String enquiryIdToDelete = scanner.next(); // Get enquiry ID from user input
                    applicant.deleteEnquiry(enquiryIdToDelete); // Call deleteEnquiry method with user input
                    break;
                case 6:
                    System.out.println("Viewing open projects for the user group:");
                    applicant.viewOpenToUserGroup(); // Call viewOpenToUserGroup method with user input
                    break;
                case 7:
                    System.out.println("Exiting the Applicant Portal. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 7);
    }
    
    @Override
    public void launchInterface() {
    	show_menu(); //delegate to existing method
    }
}
