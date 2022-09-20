package com.example.recipository.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserT {
    @Id
    private int num;
    private String email;
    private String name;
    private String password;
    private String copy_contents;
    private String author;
}
