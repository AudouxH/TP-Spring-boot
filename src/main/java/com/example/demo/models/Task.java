package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Task")
@Table(name = "task")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean checked;
    private String libelle;

    public Task(boolean checked, String libelle) {
        super();
        this.checked = checked;
        this.libelle = libelle;
    }
}
