package Main;

import Main.Personnel.User;
import Main.interfaces.IuserAuth;
import java.util.List;

public class BTOManagementSystem implements IuserAuth{
    private List<User> userList;
    
    public BTOManagementSystem(List<User> userList){
        this.userList = userList;
    }

    public List<User> getUsers(){
        return userList;
    }

    @Override
    public void login(String userID, String password){
        boolean loginSuccess= validateCredentials(userID, password);
    
        if (loginSuccess== true){
            System.out.println("Login successful for user: " + userID);
        }
        if(loginSuccess == false){
            System.out.println("UserID or password is incorrect.");
        }
    }

    @Override
    public void logout(String userID){
        for(User user : userList){
            if(user.getUserID().equals(userID)){
                user.setLoginStatus(false);
                System.out.println("Logout successful");
                return;
            }
        }
    }

    @Override
    public boolean validateCredentials(String userID, String password){
        for(User user : userList){
            if(user.getUserID().equals(userID)){
                if(user.checkPassword(password)){
                    user.setLoginStatus(true);
                    return true;
                }  
            }
        }
        return false;
    }

    @Override
    public boolean isLoggedIn(String userID){
        for(User user : userList){
            if(user.getUserID().equals(userID)){
                return user.getLoginStatus();
            }
        }
        return false;
    }
    
}