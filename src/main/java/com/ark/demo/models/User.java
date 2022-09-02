package com.ark.demo.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
public class User extends AbstractEntity{
    private Date dateCreated;
    @NotNull
    private String username;

    @NotNull
    private String pwHash;

    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    private UserDetails userDetails;

    @OneToMany(mappedBy = "user")
    private List<Request> requests;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @ManyToOne(mappedBy = "threadUsers")
    private List<Thread> userThreads;

    public User(String username, String password){
        this.username = username;
        this.pwHash = encoder.encode(password);
        this.dateCreated = new Date();
    }

    public User(){
    }

    public String getUsername() {
        return username;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setPwHash(String password){
        this.pwHash = encoder.encode(password);
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public boolean isMatchingPassword(String password){
        return encoder.matches(password,pwHash);
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void addRequest(Request request) {
        this.requests.add(request);
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Thread> getUserThreads() {
        return userThreads;
    }

    public void addUserThread(Thread thread) {
        this.userThreads.add(thread);
    }
}
