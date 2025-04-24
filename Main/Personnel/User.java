package Main.Personnel;
import AppInterface.I_UserInterface;
import Main.Enums.MaritalStatus;
import Main.Enums.UserRole; 

public abstract class User
{
    private String name;
    private String userID; // User ID is NRIC (S/T, 7 digits, ending letter)
    private String password = "password";
    private int age;
    private boolean loginStatus = false; // Default login status is false
    private MaritalStatus maritalStatus;
    private UserRole userRole;

    // Constructor
    public User(String name, String userID, String password, int age, MaritalStatus maritalStatus, UserRole userRole) 
    {
        this.name = name;
        this.userID = userID;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.userRole = userRole; // Default role
    }

    // Getter methods
    public String getUserID()
    {
        return userID;
    }

    public int getAge()
    {
        return age;
    }

    public MaritalStatus getMaritalStatus()
    {
        return maritalStatus;
    }

    public UserRole getRole()
    {
        return userRole;
    }

    // Setter methods
    public void setAge(int age)
    {
        this.age = age;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus)
    {
        this.maritalStatus = maritalStatus;
    }

    public void setUserRole(UserRole userRole)
    {
        this.userRole = userRole;
    }

    public void changePassword(Boolean loginStatus, String password)
    {
        if(loginStatus==true)
        {
            this.password=password;
        }
    }

    public void setLoginStatus(boolean loginStatus)
    {
        this.loginStatus = loginStatus;
    }
    public boolean getLoginStatus()
    {
        return loginStatus;
    }
    // Utility methods
    public boolean checkPassword(String inputPassword)
    {
        return this.password.equals(inputPassword);
    }

    public void viewProjects()
    {
        // useBTOProjectDatabase class once created
    }

    public abstract I_UserInterface getUserInterface();
}
