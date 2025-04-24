package AppInterface;

import Main.BTO.BTOProject;
import Main.BTO.Flat;
import Main.BTO.FlatList;
import Main.Enquiries.Enquiry;
import Main.Enums.*;
import Main.Manager_control.Registration;
import Main.Personnel.HDBManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class I_manager implements I_UserInterface {
    private HDBManager manager;
    
    public I_manager(HDBManager manager){
        this.manager= manager;
    }

    public void showMenu() {
        int choice;
        String ID;
        int approved;
        int projectChoice;
        int regChoice;
        int appChoice;
        do { 
            System.out.println("Welcome to the HDB Manager Interface!");
            System.out.println("Please select an option from the menu below:");
            System.out.println("1. ProjectManager Actions");
            System.out.println("2. RegistrationManager Actions");
            System.out.println("3. ApplicationManager Actions");
            System.out.println("4. EnquiryManager Actions");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character
            switch(choice){
                case 1:
                    do{
                        System.out.println("You have selected ProjectManager Actions.");
                        System.out.println("1. Create New BTO Project");
                        System.out.println("2. Edit Existing BTO Project");
                        System.out.println("3. Delete BTO Project");
                        System.out.println("4. Toggle Project Visibility");
                        System.out.println("5. View All Managed Projects");
                        System.out.println("6. View All Projects");
                        System.out.println("7. Back to Main Menu");
                        System.out.print("Enter your choice: ");
                        projectChoice = scanner.nextInt();
                        scanner.nextLine(); // consume the newline character
                        switch(projectChoice){
                            case 1:
                                System.out.println("Creating a new BTO project...");
                                System.out.println("Enter project name:");
                                String projectName = scanner.nextLine();
                                System.out.println("Enter application opening date (YYYY-MM-DD):");
                                String openingDateStr = scanner.nextLine();
                                LocalDate openingDate = null;
                                try {
                                    openingDate = LocalDate.parse(openingDateStr);
                                } catch (Exception e) {
                                    System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
                                    continue;
                                }
                                
                                System.out.println("Enter application closing date (YYYY-MM-DD):");
                                String closingDateStr = scanner.nextLine();
                                LocalDate closingDate = null;
                                try {
                                    closingDate = LocalDate.parse(closingDateStr);
                                    if (closingDate.isBefore(openingDate)) {
                                        System.out.println("Closing date cannot be before opening date.");
                                        continue;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
                                    continue; 
                                }
                                System.out.println("Enter project neighbourhood:");
                                String projectNeighbourhood = scanner.nextLine();
                                System.out.println("Enter flat types (comma-separated, e.g., Two_Room,Three_Room):");
                                String flatTypesStr = scanner.nextLine();
                                System.out.println("Enter project visibility (true/false):");
                                boolean isVisible = false;
                                try {
                                    isVisible = scanner.nextBoolean();
                                } catch (Exception e) {
                                    System.out.println("Invalid input. Please enter 'true' or 'false'.");
                                    scanner.nextLine(); // clear the invalid input
                                    continue; // go back to the project menu
                                }
                                scanner.nextLine(); // consume the newline character

                                // Display available flat types to help the user
                                System.out.println("Available flat types: " + java.util.Arrays.toString(FlatType.values()));
                                System.out.println("Enter flat list with quantities and price (e.g., Two_Room:50,500000,Three_Room:30,700000):");
                                String flatListStr = scanner.nextLine();
                                
                                String flatTypePriceStr = scanner.nextLine();
                                // Process flat types
                                String[] flatTypesArray = flatTypesStr.split(",");
                                List<FlatType> flatTypes = new ArrayList<>();
                                for (String flatTypeStr : flatTypesArray) {
                                    try {
                                        String trimmed = flatTypeStr.trim();
                                        FlatType flatType = null;
                                        
                                        if (trimmed.equalsIgnoreCase("Two_Room") || trimmed.equalsIgnoreCase("2") || trimmed.equalsIgnoreCase("2_Room")) {
                                            flatType = FlatType.Two_Room;
                                        } else if (trimmed.equalsIgnoreCase("Three_Room") || trimmed.equalsIgnoreCase("3") || trimmed.equalsIgnoreCase("3_Room")) {
                                            flatType = FlatType.Three_Room;
                                        } else {
                                            throw new IllegalArgumentException("Unknown flat type: " + trimmed);
                                        }
                                        
                                        flatTypes.add(flatType);
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid flat type: " + flatTypeStr + ". Skipping this entry.");
                                        System.out.println("Valid flat types are: " + java.util.Arrays.toString(FlatType.values()));
                                    }
                                }

                                // Process flat list with quantities
                                List<Flat> flatlist = new ArrayList<>();
                                String[] flatEntries = flatListStr.split(",");
                                for (String entry : flatEntries) {
                                    try {
                                        String[] parts = entry.trim().split(":");
                                        if (parts.length != 2) {
                                            System.out.println("Invalid format for entry: " + entry + ". Expected format: TYPE:QUANTITY");
                                            continue;
                                        }
                                        
                                        String flatTypeStr = parts[0].trim();
                                        FlatType flatType = null;
                                        
                                        if (flatTypeStr.equalsIgnoreCase("Two_Room") || flatTypeStr.equalsIgnoreCase("2") || flatTypeStr.equalsIgnoreCase("2_Room")) {
                                            flatType = FlatType.Two_Room;
                                        } else if (flatTypeStr.equalsIgnoreCase("Three_Room") || flatTypeStr.equalsIgnoreCase("3") || flatTypeStr.equalsIgnoreCase("3_Room")) {
                                            flatType = FlatType.Three_Room;
                                        } else {
                                            throw new IllegalArgumentException("Unknown flat type: " + flatTypeStr);
                                        }
                                        int quantity = Integer.parseInt(parts[1].trim());
                                        int price=Integer.parseInt(parts[2].trim());
                                        for (int i = 0; i < quantity; i++) {
                                            Flat flatObj = new Flat(flatType, price); // Assuming price is 0 for simplicity
                                            flatlist.add(flatObj);
                                        }
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid flat type or quantity in: " + entry);
                                        System.out.println("Valid flat types are: " + java.util.Arrays.toString(FlatType.values()));
                                    }
}
                                FlatList flatList = new FlatList(flatlist);
                                // Create the BTO project
                                if (flatlist.isEmpty()) {
                                    System.out.println("Flat list cannot be empty. Project creation failed.");
                                    break;
                                }
                                if (projectName.isEmpty() || projectNeighbourhood.isEmpty()) {
                                    System.out.println("Project name and neighbourhood cannot be empty. Project creation failed.");
                                    break;
                                }
                                manager.createBTOProject(projectName, openingDate, closingDate, projectNeighbourhood, flatTypes, isVisible, flatList);
                                break;
                            case 2:
                                BTOProject proj;
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
                            case 3:
                                System.out.println("Enter name of Project:");
                                String Proj_name= scanner.nextLine();
                                manager.deleteBTOProject(Proj_name);
                                break;
                            case 4:
                                System.out.println("Enter project name to toggle visibility:");
                                String projectIdToToggle = scanner.nextLine();
                                BTOProject projectToToggle;
                                // Check if the project ID exists in the managed projects
                                if (manager.getManagedProject().stream().anyMatch(project -> project.getProjectName().equals(projectIdToToggle))) {
                                    System.out.println("Project found: " + projectIdToToggle);
                                    projectToToggle = manager.getManagedProject().stream().filter(project -> project.getProjectName().equals(projectIdToToggle)).findFirst().orElse(null);
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
                            case 5:
                                manager.viewManagedProjects();
                                break;
                            case 6:
                                manager.viewALLprojects();
                                break;
                            case 7:
                                System.out.println("Returning to Main Menu...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    } while (projectChoice != 7); // Continue until the user chooses to exit
                    
                case 2:
                    do {
                        System.out.println("You have selected RegistrationManager Actions.");
                        System.out.println("1. View Pending Registrations");
                        System.out.println("2. Approve Registration");
                        System.out.println("3. Reject Registration");
                        System.out.println("4. Generate Report for Applications");
                        System.out.println("5. Back to Main Menu");
                        System.out.print("Enter your choice: ");
                        regChoice = scanner.nextInt();
                        scanner.nextLine(); // consume the newline character
                        switch(regChoice){
                            case 1:
                                List<Registration> pendingRegs = manager.getPendingRegistrations();
                                if (pendingRegs.isEmpty()) {
                                    System.out.println("No pending registrations found.");
                                } else {
                                    System.out.println("Pending Registrations:");
                                    for (Registration reg : pendingRegs) {
                                        System.out.println("Registration ID: " + reg.getRegistrationId());
                                        System.out.println("Officer: " + reg.getOfficer().getUserID());
                                        System.out.println("Project: " + reg.getProject().getProjectName());
                                        System.out.println("Date: " + reg.getRegistrationDate());
                                        System.out.println("Status: " + reg.getRegistrationStatus());
                                        System.out.println("---------------------------");
                                    }
                                }
                                break;
                            case 2:
                                System.out.println("Enter registration ID to approve:");
                                String approveId = scanner.nextLine();
                                manager.approveRegistration(approveId);
                                break;
                            case 3:
                                System.out.println("Enter registration ID to reject:");
                                String rejectId = scanner.nextLine();
                                manager.rejectRegistration(rejectId);
                                break;
            
                            case 4:
                                System.out.println("Returning to Main Menu...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    }while(regChoice!=4);
                case 3:
                    do {
                        System.out.println("You have selected ApplicationManager Actions.");
                        System.out.println("1. Approve BTO Application");
                        System.out.println("2. Approve BTO Withdrawal");
                        System.out.println("3. Generate Report for Applications");
                        System.out.println("4. Back to Main Menu");
                        System.out.print("Enter your choice: ");
                        appChoice = scanner.nextInt();
                        scanner.nextLine(); // consume the newline character
                        switch(appChoice){
                            case 1:
                                System.out.println("Enter application ID to approve or reject:");
                                String app_Id=scanner.nextLine();
                                System.out.println("Enter 1 to approve, 2 to reject");
                                int app_approved=scanner.nextInt();
                                if(app_approved==1){
                                    manager.approveBTOApplication(app_Id,"Approved");
                                }
                                else if(app_approved==2){
                                    manager.approveBTOApplication(app_Id,"Rejected");
                                }
                                break;
                            case 2:
                                System.out.println("Enter application_withdrawal ID to approve or reject:");
                                String app_withdrawal_Id=scanner.nextLine();
                                System.out.println("Enter 1 to approve, 2 to reject");
                                int app_withdrawal_approved=scanner.nextInt();
                                if(app_withdrawal_approved==1){
                                    manager.approveBTOWithdrawal(app_withdrawal_Id,"Approved");
                                }
                                else if(app_withdrawal_approved==2){
                                    manager.approveBTOWithdrawal(app_withdrawal_Id,"Rejected");
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
                                System.out.println("Returning to Main Menu...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    }while(appChoice!=4);
                case 4:
                    System.out.println("You have selected EnquiryManager Actions.");
                    System.out.println("1. View All Enquiries");
                    System.out.println("2. Reply to Enquiry");
                    System.out.println("3. Back to Main Menu");
                    System.out.print("Enter your choice: ");
                    int enquiryChoice = scanner.nextInt();
                    scanner.nextLine(); // consume the newline character
                    switch(enquiryChoice){
                        case 1:
                            System.out.println("Enquires:");
                            manager.ViewEnquiry();
                            break;
                        case 2:
                            System.out.print("Enter Enquiry ID to reply: ");
                            int enquiryId = scanner.nextInt();
                            scanner.nextLine(); // Consume newline character
                            Enquiry enquiry=manager.getEnquiryByID(enquiryId);
                            if (enquiry == null) {
                                System.out.println("Enquiry not found.");
                                break;
                            }
                            System.out.print("Enter Reply: ");
                            String reply = scanner.nextLine();
                            manager.replyEnquiry(enquiry, reply);
                            break;
                        case 3:
                            System.out.println("Returning to Main Menu...");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                    break;
                case 5:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            } // Close switch block
        } while (choice != 5); // Continue until the user chooses to exit
    } // Close showMenu method
    
    @Override
    public void launchInterface() {
        showMenu();
    }
}
