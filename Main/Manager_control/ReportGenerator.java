package Main.Manager_control;

import java.util.ArrayList;
import java.util.List;
import Main.Enums.FilterCriteria;
import Main.Enums.FlatType;

public class ReportGenerator{

    public List<ApplicantReport> generateReport(List<BTOApplication> applications, FilterCriteria criteria){
        List<ApplicantReport> report = new ArrayList<>();

        for (BTOApplication app : applications) {
            if (matchesCriteria(app, criteria)) {
                report.add(new ApplicantReport(
                        app.getApplicantId(),
                        app.getAge(),
                        app.getMaritalStatus(),
                        app.getFlatType(),
                        app.getProjectName()
                ));
            }
        }
        return report;
    }

    private boolean matchesCriteria(BTOApplication app,FilterCriteria criteria){
        switch (criteria) {
            case MARRIED:
                return app.getMaritalStatus().equalsIgnoreCase("Married");
            case SINGLE:
                return app.getMaritalStatus().equalsIgnoreCase("Single");
            case Flat_type_2room:
                return app.getFlatType().equalsIgnoreCase(FlatType.Two_Room);
            case Flat_type_3room:
                return app.getFlatType().equalsIgnoreCase(FlatType.Three_Room);
            case ALL:
            default:
                return true;
        }
    }

}