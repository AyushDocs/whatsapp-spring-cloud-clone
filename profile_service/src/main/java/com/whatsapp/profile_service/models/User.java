package com.whatsapp.profile_service.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;

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
    private LocalDateTime lastLoggedInAt;
    
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
