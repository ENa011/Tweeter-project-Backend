package com.cogent.tweet.app.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "username can't be empty")
    @Column(nullable = false, unique = true)
    private String username;

    @NotEmpty(message = "Email can't be empty")
    @Column(nullable = false, unique = true)
    @Email(message = "not a right email")
    private String email;

    @NotEmpty(message = "password can't be empty")
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "contact number can't be empty")
    @Column(nullable = false)
    private String contactNumber;

    @NotEmpty(message = "first name can't be empty")
    @Column(nullable = false)
    private String firstName;

    @NotEmpty(message = "last name can't be empty")
    @Column(nullable = false)
    private String lastName;

    @OneToMany(
        mappedBy = "register",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Tweet> tweet = new ArrayList<>();

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "register_roles",
            joinColumns = @JoinColumn(
                    name = "register_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Roles> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
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

    public List<Tweet> getTweet() {
        return tweet;
    }

    public void setTweet(List<Tweet> tweet) {
        this.tweet = tweet;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }
}
