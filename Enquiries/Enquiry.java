package Enquiries;

import Personnel.User;
import java.time.LocalDateTime;

public class Enquiry{
    private String question;
    private String reply;
    private int enquiryID;
    private Boolean status=false;
    private User sender;
    //private Project project;
    private LocalDateTime timestamp;

    public Enquiry(String question, int enquiryID, User sender) {
        this.question = question;
        this.enquiryID = enquiryID;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
    }
    
    public void printEnquiry(){
        System.out.println("Enquiry ID: " + enquiryID);
        System.out.println("Sender: " + sender.getUserID());
        System.out.println("Question: " + question);
        System.out.println("Timestamp: " + timestamp);
        if(status==true){
            System.out.println("Reply: " + reply);
        }
    }

    public void addReply(String reply){
        this.reply=reply;
        this.status=true;
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
    public String getProject(){ //project not made yet
        return null;
    }

}