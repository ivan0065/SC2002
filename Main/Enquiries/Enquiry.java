package Main.Enquiries;

import Main.BTO.BTOProject;
import Main.Personnel.User;
import java.time.LocalDateTime;

public class Enquiry{
    private String question;
    private String reply=null;
    private int enquiryID;
    private Boolean status=false;
    private User sender;
    private LocalDateTime timestamp;
    private BTOProject project;

    public Enquiry(String question, int enquiryID, User sender,BTOProject project) {
        // check if timestamp is before proj opendate or after proj enddate
        this.project=project;
        this.question = question;
        this.enquiryID = enquiryID;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
    }
    // Constructor with project reference and timestamp
    public Enquiry(String question, int enquiryID, User sender, BTOProject project, LocalDateTime timestamp) {
        this.question = question;
        this.enquiryID = enquiryID;
        this.sender = sender;
        this.project = project;
        this.timestamp = timestamp;
    }
    // Default constructor that uses current time
    public Enquiry(String question, int enquiryID, User sender) {
        this.question = question;
        this.enquiryID = enquiryID;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
    }
    public void printEnquiry() {
        System.out.println("Enquiry ID: " + enquiryID);
        System.out.println("Sender: " + sender.getUserID() + " (" + sender.getName() + ")");
        System.out.println("Project: " + (project != null ? project.getProjectName() : "Unknown"));
        System.out.println("Question: " + question);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Status: " + (status ? "Replied" : "Pending"));
        if (status) {
            System.out.println("Reply: " + reply);
        }
    }

    public void addReply(String reply){
        this.reply=reply;
        this.status=true;
    }
    public String getReply(){
        return reply;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public User getSender(){
        return sender;
    }

    public int getEnquiryID(){
        return enquiryID;
    }
    public String getProject(){
        return project.getProjectName();
    }

}