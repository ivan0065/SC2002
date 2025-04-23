package Main.Personnel;
import java.util.List;
import java.util.Scanner;

import Main.BTO.BTOProject;
import Main.BTO.ProjectDatabase;
import Main.Enquiries.ApplicantEnquiryManager;
import Main.Enquiries.EnquiryList;
import Main.Enums.FlatType;
import Main.Manager_control.BTOApplication;
import Main.Personnel.Applicant;

public class ApplicantInterface 
{
    public class I_applicant 
    {
        private Applicant applicant;
        private ProjectDatabase projectDatabase;
        private EnquiryList enquiryList;
        private ApplicantEnquiryManager enquiryManager;
        private Scanner scanner;
    
        public I_applicant(Applicant applicant) 
        {
            this.applicant = applicant;
            this.projectDatabase = new ProjectDatabase();
            this.enquiryList = new EnquiryList();
            this.enquiryManager = new ApplicantEnquiryManager(applicant);
            this.scanner = new Scanner(System.in);
        }

        public void showMenu() 
        {
            int choice;
            System.out.println("\n----- Applicant Menu -----");
            System.out.println("1. View Available Projects");
            System.out.println("2. Apply for BTO");
            System.out.println("3. View Application Status");
            System.out.println("4. Request Withdrawal");
            System.out.println("5. Manage Enquiries");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
                
            choice = scanner.nextInt();
            scanner.nextLine(); 
            while (choice != 0)
            {
                switch (choice) 
                {
                        case 1: viewProjects(); break;
                        case 2: applyForBTO(); break;
                        case 3: viewApplicationStatus(); break;
                        case 4: requestWithdrawal(); break;
                        case 5: manageEnquiries(); break;
                        case 0: System.out.println("Logging out..."); break;
                        default: System.out.println("Invalid choice. Please try again.");
                    }
                } 
            }

        private void viewProjects() 
        {
            System.out.println("\n----- Available BTO Projects -----");
            List<BTOProject> projects = projectDatabase.getAllProjects();
            
            if (projects.isEmpty()) 
            {
                System.out.println("No projects available.");
                return;
            }

            boolean isMarried = applicant.getMaritalStatus().toString().equals("MARRIED");
            int age = applicant.getAge();
            boolean foundEligible = false;
            
            for (BTOProject project : projects) 
            {
                if (project.getVisibilitySetting() && project.isApplicableFor(age, isMarried)) 
                {
                    project.displayProject();
                    foundEligible = true;
                }
            }
        }

        
    }
}
