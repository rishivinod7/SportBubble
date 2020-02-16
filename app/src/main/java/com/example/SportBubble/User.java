package com.example.SportBubble;

public class User {

    private int id;
    private String name,username;
    private String email;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String usern) {
        this.username = usern;
    }


    public String toString() {
        return "Information{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username=" + username +
                '}';
    }



}
