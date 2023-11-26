package com.example.linkedinprac;

public class User {
    private String username;
    private String email;
    private String gender;
    private String shortBio;
    private String skills;
    private String phoneNo;
    private String profilePicUrl;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String username, String email, String gender, String shortBio, String skills, String phoneNo) {
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.shortBio = shortBio;
        this.skills = skills;
        this.phoneNo = phoneNo;
        this.profilePicUrl = "";
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }
}
