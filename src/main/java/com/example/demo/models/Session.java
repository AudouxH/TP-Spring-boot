package com.example.demo.models;

import jakarta.persistence.*;

import java.util.List;

@Entity(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long session_id;
    private String session_name;
    private String session_desc;
    //private Integer session_lenght;

    @ManyToMany
    @JoinTable(
            name = "session_speakers", // table name
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "speaker_id"))
    private List<Speaker> speakers;

    public Session() {
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public Long getSession_id() {
        return session_id;
    }

    public String getSession_name() {
        return session_name;
    }

    public String getSession_desc() {
        return session_desc;
    }
}
