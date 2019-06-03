package com.api.models;

import com.api.models.enums.CommentType;
import com.fasterxml.jackson.annotation.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="comments")
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentType type;

    @ManyToOne
    @JsonIgnore
    private SubmissionModel submission;

    @ManyToOne
    @JsonIgnoreProperties({"comments", "supervisor", "requestedSupervisor", "requestedSubmission", "surname", "name", "email", "confirmedReaderSubmission", "setOpponentSubmission"})
    private UserModel author;

    public CommentModel(String text, CommentType type, SubmissionModel submission, UserModel author) {
        this.text = text;
        this.type = type;
        this.submission = submission;
        this.author = author;
    }

    public CommentModel(){}

    public UserModel getAuthor() {
        return author;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CommentType getType() {
        return type;
    }

    public void setType(CommentType type) {
        this.type = type;
    }

    public SubmissionModel getSubmission() {
        return submission;
    }

    public void setSubmission(SubmissionModel submission) {
        this.submission = submission;
    }
}
