package interfaces;

public interface IuserAuth{
    void login(String username, String password);
    void logout(String username);
    boolean isLoggedIn(String username);
    boolean validateCredentials(String username, String password);
}