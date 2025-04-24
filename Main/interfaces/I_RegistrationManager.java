package Main.interfaces;

import Main.BTO.BTOProject;
import Main.Manager_control.Registration;
import Main.Personnel.HDBOfficer;
import java.util.List;

public interface I_RegistrationManager{
    void addRegistration(Registration registration);

    List<Registration> getRegistrationList();

    List<BTOProject> getAvailForRegistration(HDBOfficer officer);

    boolean validateOfficerEligibility(String officerUserID, String projectID);

    void createRegistration(BTOProject project, HDBOfficer officer);

    List<Registration> getRegistrationsForOfficer(String officerUserID);

    Registration getRegistrationByRegId(String registrationID);

    boolean checkApplicationPeriodClash(HDBOfficer officer, BTOProject newProject);

    boolean isPeriodClash(BTOProject existingProject, BTOProject newProject);

    boolean approveOfficerRegistration(String registrationId);

    boolean updateRegistrationStatus(String registrationId);

    void updateProjectHDBOfficers(String registrationId);
}