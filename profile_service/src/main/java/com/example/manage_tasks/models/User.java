package com.example.manage_tasks.models;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usr_tbl")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String password;
    @Column(unique=true)
    private String email;
    @Column(updatable = false)
    private LocalDate createdAt;
    @LastModifiedDate()
    private LocalDate updatedAt;
    private String roles="NONE";

    public User(String username, String password, String email){
        this.name = username;
        this.password = password;
        this.email = email;
    }
    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDate.now();
    }
    @PrePersist
    public void onPersist(){
        this.updatedAt = LocalDate.now();
        this.createdAt=LocalDate.now();

    }
}
