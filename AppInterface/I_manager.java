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
        int choice = -1;
        Scanner scanner = new Scanner(System.in);
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

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume the newline character
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume the invalid input
                continue;
            }

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
                        System.out.println("7. Change Password");
                        System.out.println("8. Back to Main Menu");
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
                                System.out.println("Enter flat types (comma-separated):");
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
                                System.out.println("Enter flat list (comma-separated):");
                                String flatListStr = scanner.nextLine();
                                // Process flat types
                                String[] flatTypesArray = flatTypesStr.split(",");
                                String[] flatListArray = flatListStr.split(",");
                                // Convert flat types to FlatType enum
                                List<FlatType> flatTypes = new ArrayList<>();
                                for (String flatTypeStr : flatTypesArray) {
                                    try {
                                        FlatType flatType = FlatType.valueOf(flatTypeStr.trim());
                                        flatTypes.add(flatType);
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid flat type: " + flatTypeStr + ". Skipping this entry.");
                                        System.out.println("Valid flat types are: " + java.util.Arrays.toString(FlatType.values()));
                                    }
                                }
                                // Create FlatList object
                                List<Flat> flatlist = new ArrayList<>();
                                String[] flatEntries = flatListStr.split(",");
                                for (int i = 0; i < flatEntries.length - 1; i += 2) {
                                    try {
                                        String typeQuantityEntry = flatEntries[i].trim();
                                        String priceEntry = flatEntries[i+1].trim();
                                        
                                        // Extract flat type and quantity
                                        String[] typeAndQuantity = typeQuantityEntry.split(":");
                                        if (typeAndQuantity.length != 2) {
                                            System.out.println("Invalid format for entry: " + typeQuantityEntry + ". Expected format: TYPE:QUANTITY");
                                            continue;
                                        }
                                        
                                        String flatTypeOnly = typeAndQuantity[0].trim();
                                        int quantity = Integer.parseInt(typeAndQuantity[1].trim());
                                        int price = Integer.parseInt(priceEntry);
                                        
                                        FlatType flatType = null;
                                        if (flatTypeOnly.equalsIgnoreCase("Two_Room") || flatTypeOnly.equalsIgnoreCase("2") || 
                                            flatTypeOnly.equalsIgnoreCase("2_Room")) {
                                            flatType = FlatType.Two_Room;
                                        } else if (flatTypeOnly.equalsIgnoreCase("Three_Room") || flatTypeOnly.equalsIgnoreCase("3") || 
                                                  flatTypeOnly.equalsIgnoreCase("3_Room")) {
                                            flatType = FlatType.Three_Room;
                                        } else {
                                            throw new IllegalArgumentException("Unknown flat type: " + flatTypeOnly);
                                        }
                                        
                                        for (int j = 0; j < quantity; j++) {
                                            Flat flatObj = new Flat(flatType, price);
                                            flatlist.add(flatObj);
                                        }
                                    } catch (IndexOutOfBoundsException e) {
                                        System.out.println("Incomplete flat entry at index " + i + ". Make sure each flat type has both quantity and price.");
                                    } catch (IllegalArgumentException e) {
                                        System.out.println("Invalid flat type, quantity, or price: " + e.getMessage());
                                    } catch (Exception e) {
                                        System.out.println("Error processing flat entry: " + e.getMessage());
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
                                System.out.println("8. Return to Main Menu");
                                System.out.print("Enter your choice: ");
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
                                String projectNameToToggle = scanner.nextLine();
                                BTOProject projectToToggle;
                                // Check if the project name exists in the managed projects
                                if (manager.getManagedProject().stream().anyMatch(project -> project.getProjectName().equals(projectNameToToggle))) {
                                    System.out.println("Project found: " + projectNameToToggle);
                                    projectToToggle = manager.getManagedProject().stream().filter(project -> project.getProjectName().equals(projectNameToToggle)).findFirst().orElse(null);
                                } else {
                                    System.out.println("Project not found: " + projectNameToToggle);
                                    break;
                                }
                                // Toggle the visibility of the project
                                System.out.println("Toggling visibility for project name: " + projectNameToToggle);
                                System.out.println("Enter new visibility (true/false):");
                                boolean newVisibility = scanner.nextBoolean();
                                manager.toggleProjectVisibility(projectToToggle,newVisibility);
                                System.out.println("Project visibility toggled successfully.");
                                break;
                            case 5:
                                System.out.println("Managed Projects:");
                                List<BTOProject> managedProjects = manager.getManagedProject();
                                if (managedProjects.isEmpty()) {
                                    System.out.println("No managed projects found.");
                                } else {
                                    for (BTOProject project : managedProjects) {
                                        System.out.println("Project Name: " + project.getProjectName());
                                        System.out.println("Opening Date: " + project.getApplicationOpeningDate());
                                        System.out.println("Closing Date: " + project.getApplicationClosingDate());
                                        System.out.println("Neighbourhood: " + project.getProjectNeighbourhood());
                                        System.out.println("---------------------------");
                                    }
                                }
                                break;
                            case 6:
                                manager.viewALLprojects();
                                break;
                            case 7:
                                System.out.println("Change Password");
                                System.out.print("Enter your current password: ");
                                String currentPassword = scanner.nextLine();
                                
                                // Verify current password
                                if (manager.checkPassword(currentPassword)) {
                                    System.out.print("Enter new password: ");
                                    String newPassword = scanner.nextLine();
                                    System.out.print("Confirm new password: ");
                                    String confirmPassword = scanner.nextLine();
                                    
                                    if (newPassword.equals(confirmPassword)) {
                                        // Call the changePassword method from User superclass
                                        manager.changePassword(true, newPassword);
                                        System.out.println("Password successfully changed.");
                                    } else {
                                        System.out.println("Passwords do not match. Password change failed.");
                                    }
                                } else {
                                    System.out.println("Incorrect current password. Password change failed.");
                                }
                                break;
                            case 8:
                                System.out.println("Returning to Main Menu...");
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    } while (projectChoice != 8); // Continue until the user chooses to exit
                    break;
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
                        switch(regChoice)
                        {
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
                                System.out.println("Which project would you like to generate a report for?");
                                
                                // Get list of managed projects
                                List<BTOProject> managedProjects = manager.getManagedProject();
                                if (managedProjects == null) {
                                    System.out.println("You don't have any managed projects.");
                                    break;
                                }
                                
                                // Display managed projects
                                for (int i = 0; i < managedProjects.size(); i++) {
                                    System.out.println((i + 1) + ". " + managedProjects.get(i).getProjectName());
                                }
                                
                                System.out.print("Enter project number: ");
                                int projectNumber;
                                try {
                                    projectNumber = scanner.nextInt();
                                    scanner.nextLine();
                                    
                                    if (projectNumber < 1 || projectNumber > managedProjects.size()) {
                                        System.out.println("Invalid project number.");
                                        break;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid input. Please enter a number.");
                                    scanner.nextLine();
                                    break;
                                }
                                
                                BTOProject selectedProject = managedProjects.get(projectNumber - 1);
                                
                                // Get registrations for the selected project
                                List<Registration> projectRegistrations = new ArrayList<>();
                                for (Registration reg : manager.getRegistrationList()) {
                                    if (reg.getProject().getProjectName().equals(selectedProject.getProjectName())) {
                                        projectRegistrations.add(reg);
                                    }
                                }
                                
                                if (projectRegistrations.isEmpty()) {
                                    System.out.println("No registrations found for project: " + selectedProject.getProjectName());
                                    break;
                                }
                            
                                System.out.println("Select report type:");
                                System.out.println("1. All Registrations");
                                System.out.println("2. Pending Registrations");
                                System.out.println("3. Approved Registrations");
                                System.out.println("4. Rejected Registrations");
                                System.out.print("Enter your choice: ");
                                
                                int reportType;
                                try {
                                    reportType = scanner.nextInt();
                                    scanner.nextLine();
                                } catch (Exception e) {
                                    System.out.println("Invalid input. Please enter a number.");
                                    scanner.nextLine();
                                    break;
                                }
                                
                                // Filter registrations based on status
                                List<Registration> filteredRegistrations = new ArrayList<>();
                                String statusFilter;
                                
                                switch (reportType) {
                                    case 1: // All
                                        filteredRegistrations = projectRegistrations;
                                        statusFilter = "All";
                                        break;
                                    case 2: // Pending
                                        for (Registration reg : projectRegistrations) {
                                            if (reg.getRegistrationStatus().equals("PENDING")) {
                                                filteredRegistrations.add(reg);
                                            }
                                        }
                                        statusFilter = "Pending";
                                        break;
                                    case 3: // Approved
                                        for (Registration reg : projectRegistrations) {
                                            if (reg.getRegistrationStatus().equals("APPROVED")) {
                                                filteredRegistrations.add(reg);
                                            }
                                        }
                                        statusFilter = "Approved";
                                        break;
                                    case 4: // Rejected
                                        for (Registration reg : projectRegistrations) {
                                            if (reg.getRegistrationStatus().equals("REJECTED")) {
                                                filteredRegistrations.add(reg);
                                            }
                                        }
                                        statusFilter = "Rejected";
                                        break;
                                    default:
                                        filteredRegistrations = projectRegistrations;
                                        statusFilter = "All";
                                        break;
                            }
                            
                                // Display report
                                System.out.println("\n=============== Registration Report ===============");
                                System.out.println("Project: " + selectedProject.getProjectName());
                                System.out.println("Status Filter: " + statusFilter);
                                System.out.println("Total Registrations: " + filteredRegistrations.size());
                                System.out.println("=================================================");
                                
                                if (filteredRegistrations.isEmpty()) {
                                    System.out.println("No registrations match the selected criteria.");
                                } else {
                                    System.out.printf("%-15s | %-15s | %-12s | %-10s\n",
                                            "Registration ID", "Officer NRIC", "Date", "Status");
                                    System.out.println("-------------------------------------------------");
                                    
                                    for (Registration reg : filteredRegistrations) {
                                        System.out.printf("%-15s | %-15s | %-12s | %-10s\n",
                                                reg.getRegistrationId(),
                                                reg.getOfficer().getUserID(),
                                                reg.getRegistrationDate(),
                                                reg.getRegistrationStatus());
                                    }
                                }
                                
                                System.out.println("=================================================");
                                    break;

                                case 5:
                                    System.out.println("Returning to Main Menu...");
                                    break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    }while(regChoice!=5);
                break;
                case 3:
                    do {
                        System.out.println("You have selected ApplicationManager Actions.");
                        System.out.println("1. Approve BTO Application");
                        System.out.println("2. Approve BTO Withdrawal");
                        System.out.println("3. Generate Report for Applications");
                        System.out.println("4. Back to Main Menu");
                        System.out.print("Enter your choice: ");
                        appChoice = scanner.nextInt();
                        scanner.nextLine(); 

                        switch(appChoice){
                            case 1:
                                System.out.println("Enter application ID to approve or reject:");
                                String app_Id = scanner.nextLine();
                                
                                if (app_Id == null || app_Id.trim().isEmpty()) {
                                    System.out.println("Application ID cannot be empty. Please try again.");
                                    break;
                                }
                                
                                System.out.println("Enter 1 to approve, 2 to reject");
                                int app_approved;
                                try {
                                    app_approved = scanner.nextInt();
                                    scanner.nextLine(); // consume the newline character
                                    
                                    if (app_approved == 1) {
                                        manager.approveBTOApplication(app_Id, "Approved");
                                        System.out.println("Application approved successfully.");
                                    } else if (app_approved == 2) {
                                        manager.approveBTOApplication(app_Id, "Rejected");
                                        System.out.println("Application rejected successfully.");
                                    } else {
                                        System.out.println("Invalid choice. Please enter 1 to approve or 2 to reject.");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid input. Please enter a valid number (1 or 2).");
                                    scanner.nextLine();
                                }
                                break;

                            case 2:
                                System.out.println("Enter application_withdrawal ID to approve or reject:");
                                String app_withdrawal_Id=scanner.nextLine();
                                System.out.println("Enter 1 to approve, 2 to reject");
                                int app_withdrawal_approved;
                                try {
                                    app_withdrawal_approved = scanner.nextInt();
                                    scanner.nextLine(); // consume the newline character
                                    
                                    if (app_withdrawal_approved == 1) {
                                        manager.approveBTOWithdrawal(app_withdrawal_Id, "Approved");
                                        System.out.println("Withdrawal approved successfully.");
                                    } else if (app_withdrawal_approved == 2) {
                                        manager.approveBTOWithdrawal(app_withdrawal_Id, "Rejected");
                                        System.out.println("Withdrawal rejected successfully.");
                                    } else {
                                        System.out.println("Invalid choice. Please enter 1 to approve or 2 to reject.");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid input. Please enter a valid number (1 or 2).");
                                    scanner.nextLine();
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
                                try {
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
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Error: Invalid filter criteria. Please try again.");
                                } catch (Exception e) {
                                    System.out.println("An unexpected error occurred: " + e.getMessage());
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
                break;
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
                            int enquiryId = -1;
                            try {
                                enquiryId = scanner.nextInt();
                                scanner.nextLine();
                            } catch (Exception e) {
                                System.out.println("Invalid input. Please enter a valid Enquiry ID.");
                                scanner.nextLine();
                            }

                            Enquiry enquiry = manager.getEnquiryByID(enquiryId);
                            if (enquiry == null) {
                                System.out.println("Enquiry not found. Please check the Enquiry ID and try again.");
                                break;
                            }

                            System.out.print("Enter Reply: ");
                            String reply = scanner.nextLine();
                            if (reply.trim().isEmpty()) {
                                System.out.println("Reply cannot be empty. Please enter a valid reply.");
                                break;
                            }

                            try {
                                manager.replyEnquiry(enquiry, reply);
                                System.out.println("Reply sent successfully.");
                            } catch (Exception e) {
                                System.out.println("An error occurred while sending the reply: " + e.getMessage());
                            }
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
            }
        } while (choice != 5); // Continue until the user chooses to exit
    } // Close showMenu method
    
    @Override
    public void launchInterface() {
        showMenu();
    }
}
