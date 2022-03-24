package com.example.manage_tasks.models;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User{
    @Id
    private Long id;
    private String userName;
    private String password;
    private String email;
    private LocalDate createdAt;
    private String roles="NONE";

    public User(String username, String password, String email){
        this.userName = username;
        this.password = password;
        this.email = email;
    }
}
