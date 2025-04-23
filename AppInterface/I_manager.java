package AppInterface;

import AppInterface.I_UserInterface;
import Main.BTO.BTOProject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

import Main.Personnel.HDBManager;
import Main.BTO.FlatList;
import Main.Enums.*;

public class I_manager implements I_UserInterface {
    private HDBManager manager;
    
    public I_manager(HDBManager manager){
        this.manager= manager;
    }

    public void showMenu() {
        int choice;
        String ID;
        int approved;
        do { 
            System.out.println("Welcome to the HDB Manager Interface!");
            System.out.println("Please select an option from the menu below:");
            System.out.println("1. ProjectManager Actions");
            System.out.println("2. RegistrationManager Actions");
            System.out.println("3. ApplicationManager Actions");
        } while ();
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
                    System.out.println("Creating a new BTO project...");
                    System.out.println("Enter project name:");
                    String projectName = scanner.nextLine();
                    System.out.println("Enter application opening date (YYYY-MM-DD):");
                    String openingDateStr = scanner.nextLine();
                    LocalDate openingDate = LocalDate.parse(openingDateStr);
                    System.out.println("Enter application closing date (YYYY-MM-DD):");
                    String closingDateStr = scanner.nextLine();
                    LocalDate closingDate = LocalDate.parse(closingDateStr);
                    System.out.println("Enter project status:");
                    String projectStatus = scanner.nextLine();
                    System.out.println("Enter project neighbourhood:");
                    String projectNeighbourhood = scanner.nextLine();
                    System.out.println("Enter flat types (comma-separated):");
                    String flatTypesStr = scanner.nextLine();
                    System.out.println("Enter project visibility (true/false):");
                    boolean isVisible = scanner.nextBoolean();
                    scanner.nextLine(); // consume the newline character
                    System.out.println("Enter project ID:");
                    String projectId = scanner.nextLine();
                    System.out.println("Enter flat list (comma-separated):");
                    String flatListStr = scanner.nextLine();
                    // Parse the flat types and flat list
                    String[] flatTypesArray = flatTypesStr.split(",");
                    String[] flatListArray = flatListStr.split(",");
                    // Convert flat types to FlatType enum
                    List<FlatType> flatTypes = new ArrayList<>();
                    for (String flatTypeStr : flatTypesArray) {
                        FlatType flatType = FlatType.valueOf(flatTypeStr.trim().toUpperCase());
                        flatTypes.add(flatType);
                    }
                    // Create FlatList object
                    FlatList flatList = new FlatList();
                    for (String flat : flatListArray) {
                        FlatType flatType = FlatType.valueOf(flat.trim().toUpperCase());
                        flatList.addFlat(flatType);
                    }
                    // Create the BTO project
                    manager.createBTOProject();
                    System.out.println("BTO project created successfully!");
                    break;
                case 5:
                    BTOProject proj;;
                    System.out.println("Enter the project name to edit:");
                    String projectName1 = scanner.nextLine();
                    // Display the fields that can be edited
                    // You can replace these with actual field names or enums if you have them defined
                    // by choices
                    if (manager.getManagedProject().isEmpty()) {
                        System.out.println("No projects available to edit.");
                        break;
                    }
                    else{
                        if (manager.getManagedProject().stream().anyMatch(project -> project.getProjectName().equals(projectName1))){
                            System.out.println("Project found: " + projectName1);
                            proj= manager.getManagedProject().stream().filter(project -> project.getProjectName().equals(projectName1)).findFirst().orElse(null);
                        }
                        else{
                            System.out.println("Project not found: " + projectName1);
                            break;
                        }
                    }
                    System.out.println("Choose the field you want to edit: ");
                    System.out.println("1. Project Name");
                    System.out.println("2. Application Opening Date");
                    System.out.println("3. Application Closing Date");
                    System.out.println("4. Project Status");
                    System.out.println("5. Project Neighbourhood");
                    System.out.println("6. Project Visibility");
                    System.out.println("7. Project ID");
                    
                    int edit_choice=0;
                    try {
                        edit_choice = scanner.nextInt();
                        scanner.nextLine(); // consume the newline character
                        if (edit_choice < 1 || edit_choice > 8) {
                            System.out.println("Invalid choice. Please try again.");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter a number between 1 and 8.");
                        scanner.nextLine(); // consume the invalid input
                        break;
                    }
                    
                    manager.editBTOProject(edit_choice, proj);
                    
                    break;
                case 6:
                    System.out.println("Enter name of Project:");
                    String Proj_name= scanner.nextLine();
                    manager.deleteBTOProject(Proj_name);
                    break;
                case 7:
                    System.out.println("Enter project ID to toggle visibility:");
                    String projectIdToToggle = scanner.nextLine();
                    BTOProject projectToToggle;
                    // Check if the project ID exists in the managed projects
                    if (manager.getManagedProject().stream().anyMatch(project -> project.getProjectId().equals(projectIdToToggle))) {
                        System.out.println("Project found: " + projectIdToToggle);
                        projectToToggle = manager.getManagedProject().stream().filter(project -> project.getProjectId().equals(projectIdToToggle)).findFirst().orElse(null);
                    } else {
                        System.out.println("Project not found: " + projectIdToToggle);
                        break;
                    }
                    // Toggle the visibility of the project
                    System.out.println("Toggling visibility for project ID: " + projectIdToToggle);
                    System.out.println("Enter new visibility (true/false):");
                    boolean newVisibility = scanner.nextBoolean();
                    manager.toggleProjectVisibility(projectToToggle,newVisibility);
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
    
    @Override
    public void launchInterface() {
    	showMenu();
    }
}
