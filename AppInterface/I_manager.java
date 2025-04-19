package AppInterface;
import java.util.List;
import java.util.Scanner;

import Main.Personnel.HDBManager;
import Main.Enums.*;
public class I_manager{
    private HDBManager manager;
    
    public I_manager(HDBManager manager){
        this.manager= manager;
    }

    public void showMenu() {
        int choice;
        String ID;
        int approved;
        do {
            System.out.println("\n--- HDB Manager Interface ---");
            System.out.println("1. Approve BTO Application");
            System.out.println("2. Approve BTO Withdrawal");
            System.out.println("3. Generate Report for Applications");
            System.out.println("4. Create New BTO Project");
            System.out.println("5. Edit Existing BTO Project");
            System.out.println("6. Delete BTO Project");
            System.out.println("7. Toggle Project Visibility");
            System.out.println("8. View All Managed Projects");
            System.out.println("9. View All Projects");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            // Perform action based on user input
            switch (choice) {
                case 1:
                    System.out.println("Enter application ID to approve or reject:");
                    ID=scanner.nextLine();
                    System.out.println("Enter 1 to approve, 2 to reject");
                    approved=scanner.nextInt();
                    if(approved==1){
                        manager.approveBTOApplication(ID,"Approved");
                    }
                    else if(approved==2){
                        manager.approveBTOApplication(ID,"Rejected");
                    }
                    break;
                case 2:
                    System.out.println("Enter application_withdrawal ID to approve or reject:");
                    ID=scanner.nextLine();
                    System.out.println("Enter 1 to approve, 2 to reject");
                    approved=scanner.nextInt();
                    if(approved==1){
                        manager.approveBTOWithdrawal(ID,"Approved");
                    }
                    else if(approved==2){
                        manager.approveBTOWithdrawal(ID,"Rejected");
                    }
                    break;
                case 3:
                    System.out.println("Choose criteria to filter: ");
                    System.out.println("1. All");
                    System.out.println("2. Married");
                    System.out.println("3. Single");
                    System.out.println("4. Youth");
                    System.out.println("5. Middle_aged");
                    System.out.println("6. Elderly");
                    System.out.println("7. 2_room");
                    System.out.println("8. 3_room");
                    int criteria_choice=scanner.nextInt();
                    scanner.nextLine();
                    switch (criteria_choice) {
                        case 1:
                            System.out.println("Generating report for all applicants...");
                            manager.generateReport(FilterCriteria.ALL);
                            break;
                        case 2:
                            System.out.println("Generating report for married applicants...");
                            manager.generateReport(FilterCriteria.MARRIED);
                            break;
                        case 3:
                            System.out.println("Generating report for single applicants...");
                            manager.generateReport(FilterCriteria.SINGLE);
                            break;
                        case 4:
                            System.out.println("Generating report for youth applicants...");
                            manager.generateReport(FilterCriteria.Youths);
                            break;
                        case 5:
                            System.out.println("Generating report for middle-aged applicants...");
                            manager.generateReport(FilterCriteria.Middle_aged);
                            break;
                        case 6:
                            System.out.println("Generating report for elderly applicants...");
                            manager.generateReport(FilterCriteria.Elderly);
                            break;
                        case 7:
                            System.out.println("Generating report for applicants interested in 2-room flats...");
                            manager.generateReport(FilterCriteria.Flat_type_2room);
                            break;
                        case 8:
                            System.out.println("Generating report for applicants interested in 3-room flats...");
                            manager.generateReport(FilterCriteria.Flat_type_3room);
                            break;
                        default:
                            System.out.println("Invalid choice. Generating report for all applicants by default.");
                            manager.generateReport(FilterCriteria.ALL);
                            break;
                    }
                    break;
                case 4:
                    manager.createBTOProject();
                    break;
                case 5:
                    // by choices
                    manager.editBTOProject();
                    break;
                case 6:
                    System.out.println("Enter name of Project:");
                    String Proj_name= scanner.nextLine();
                    manager.deleteBTOProject(Proj_name);
                    break;
                case 7:
                    manager.toggleProjectVisibility();
                    break;
                case 8:
                    manager.viewManagedProjects();
                    break;
                case 9:
                    manager.viewALLprojects();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }

        } while (choice != 0); // Continue until the user chooses to exit
    }
}