package com.api.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calendar")
public class CalendarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="event_id")
    private Integer id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime  createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime   expiresAt;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    public CalendarModel() {

    }

    public CalendarModel(LocalDateTime createdAt, LocalDateTime expiresAt, String title, String description) {
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.title = title;
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
