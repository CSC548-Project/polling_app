package com.example.pollingsystem.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    private List<Option> options;

    // Getters and Setters
}
