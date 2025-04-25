package Main.Manager_control;

import Main.BTO.BTOProject;
import Main.Enums.FilterCriteria;
import Main.Enums.FlatType;
import Main.Enums.MaritalStatus;
import Main.Personnel.Applicant;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator{

    public List<ApplicantReport> generateReport(List<BTOApplication> applications, FilterCriteria criteria, BTOProject project) {
        if (applications == null || applications.isEmpty()) {
            System.out.println("No applications available for report generation.");
            return new ArrayList<>();
        }
        List<ApplicantReport> report = new ArrayList<>();
        // Filter applications based on criteria
        List<Applicant> applicants = project.getApplicantList();
        
        for (BTOApplication app : applications) {
            // Check if the application matches the criteria

            if (matchesCriteria(app, criteria,project)) {
                // Find the corresponding applicant details
                String applicantId = app.getApplicantId();
                Applicant applicant = null;
                for (Applicant applicant1 : applicants) {
                    if (applicant1.getUserID().equals(applicantId)) {
                        applicant = applicant1;
                        break;
                    }
                }
                if (applicant != null) {
                    report.add(new ApplicantReport(
                            app.getApplicantId(),
                            applicant.getAge(),
                            applicant.getMaritalStatus(),
                            app.getFlatType(),
                            app.getProjectId()
                    ));
                } else {
                    System.out.println("Applicant details not found for application ID: " + app.getApplicantId());
                }
            }
        }
        return report;
    }

    private boolean matchesCriteria(BTOApplication app,FilterCriteria criteria,BTOProject project){
        List<Applicant> applicants = project.getApplicantList();
        String applicantId = app.getApplicantId();
        Applicant applicant = null;
        for (Applicant applicant1 : applicants) {
            if (applicant1.getUserID().equals(applicantId)) {
                applicant = applicant1;
                break;
            }
        }
        switch (criteria) {
            case MARRIED:
                return applicant.getMaritalStatus() == MaritalStatus.MARRIED;
            case SINGLE:
                return applicant.getMaritalStatus() == MaritalStatus.SINGLE;
            case Youths:
                return applicant.getAge() < 35;
            case Middle_aged:
                return applicant.getAge() >= 35 && applicant.getAge() < 55;
            case Elderly:
                return applicant.getAge() >= 55;
            case Flat_type_2room:
                return app.getFlatType() ==FlatType.Two_Room;
            case Flat_type_3room:
                return app.getFlatType() ==FlatType.Three_Room;
            case ALL:
            default:
                return true;
        }
    }

}