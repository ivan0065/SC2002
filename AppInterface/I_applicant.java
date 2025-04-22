package AppInterface;

import java.util.Scanner;

import Main.Enums.FlatType;
import Main.Personnel.Applicant;

public class I_applicant {
    private Applicant applicant;
    public I_applicant(Applicant applicant) {
        this.applicant = applicant;
    }
    public void show_menu(){
        int choice;
        do {
            System.out.println("Welcome to the Applicant Portal!");
            System.out.println("1.Apply for BTO");
            System.out.println("2.View Application Status");
            System.out.println("3.Make Enquiry");
            System.out.println("4.Delete Enquiry");
            System.out.println("5.View Open Projects");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter project ID and flat type to apply for BTO:");
                    String projectId = scanner.next(); // Get project ID from user input
                    String flatType = scanner.next(); // Get flat type from user input
                    FlatType flatType1 = FlatType.valueOf(flatType.toUpperCase());
                    applicant.applyBTO(projectId, flatType1); // Call applyBTO method with user input
                    break;
                case 2:
                    applicant.viewApplicationStatus(); // Call viewApplicationStatus method with user input
                    break;
                case 3:
                    System.out.println("Enter enquiry ID to make an enquiry:");
                    String enquiryId = scanner.next(); // Get enquiry ID from user input
                    applicant.createEnquiry(enquiryId); // Call makeEnquiry method with user input
                    break;
                case 4:
                    System.out.println("Enter enquiry ID to delete an enquiry:");
                    String enquiryIdToDelete = scanner.next(); // Get enquiry ID from user input
                    applicant.deleteEnquiry(enquiryIdToDelete); // Call deleteEnquiry method with user input
                    break;
                case 5:
                    System.out.println("Viewing open projects for the user group:");
                    applicant.viewOpenToUserGroup(); // Call viewOpenToUserGroup method with user input
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 5);
    }
}
