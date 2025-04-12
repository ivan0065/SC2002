package Personnel;
import Enums.UserRole;

public class User{
    private String userID;
    private String password="password";
    private int age;
    private String maritalStatus;
    public Boolean loginStatus;
    private String nirc;
    private UserRole role;

    public User(String userID, String password, int age, String maritalStatus, String nirc) {
        this.userID = userID;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.nirc = nirc;
        this.loginStatus = false;
        this.role = UserRole.Applicant; // Default role
    }

    public void setAge(int age){
        this.age=age;
    }

    public int getAge(){
        return age;
    }

    public String getMaritalStatus(){
        return maritalStatus;
    }

    public String getUserID(){
        return userID;
    }

    public void setMaritalStatus(String maritalStatus){
        this.maritalStatus=maritalStatus;
    }

    public boolean checkPassword(String inputPassword){
        return this.password.equals(inputPassword);
    }

    public void changePassword(Boolean loginStatus, String password){
        if(loginStatus==true){
            this.password=password;
        }
    }

    public void viewProjects(){
        // useBTOProjectDatabase class once created
    }
}