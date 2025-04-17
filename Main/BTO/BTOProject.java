package Main.BTO;


import java.util.List;
import Main.Enums.FlatType;
import java.time.LocalDate;
import Main.Personnel.*;
public class BTOProject {
    private String projectStatus;

    private String projectName;

    private String projectNeighbourhood;

    private List<FlatType> flatTypes;

    private LocalDate applicationOpeningDate;

    private LocalDate applicationClosingDate;

    private String projectID;

    private List<Application> applicationList;

    private List<Applicant> applicatantList;

    private FlatList flatList;

    private int availableOfficerSlots;

    public BTOProject(){

    }

    //isVisible

    //managerincharge

    //officerlist

    public String getProjectName(){
        return this.projectName;
    }

    public FlatList getflatList(){
        return flatList;
    }
}
