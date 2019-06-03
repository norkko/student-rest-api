package com.api.models;

import com.api.models.enums.Grade;
import com.api.models.enums.SubmissionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "submission")
public class SubmissionModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="fileType")
    private String fileType;

    @Column(name="fileName")
    private String fileName;

    @Lob
    @JsonIgnore
    private byte[] data;

    @Enumerated(EnumType.STRING)
    @Column(name="grade", nullable = false)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false)
    private SubmissionType type;

    @OneToMany(targetEntity = CommentModel.class, mappedBy = "submission", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"submission"})
    private List<CommentModel> comments;

    @ManyToOne
    @JsonIgnoreProperties({"supervisor", "requestedSupervisor", "comments", "requestedSubmission", "confirmedReaderSubmission", "setOpponentSubmission"})
    private UserModel user;

    @JsonIgnoreProperties({"requestedSubmission", "requestedSupervisor", "supervisor", "comments", "confirmedReaderSubmission", "setOpponentSubmission"})
    @OneToMany(targetEntity = StudentModel.class, mappedBy = "requestedSubmission", fetch = FetchType.LAZY)
    private List<StudentModel> requestedReaders;

    @JsonIgnoreProperties({"requestedSubmission", "requestedSupervisor", "supervisor", "comments", "confirmedReaderSubmission", "setOpponentSubmission"})
    @OneToMany(targetEntity = StudentModel.class, mappedBy = "confirmedReaderSubmission", fetch = FetchType.LAZY)
    private List<StudentModel> confirmedReaders;

    @JsonIgnoreProperties({"requestedSubmission", "requestedSupervisor", "supervisor", "comments", "confirmedReaderSubmission", "setOpponentSubmission"})
    @OneToMany(targetEntity = StudentModel.class, mappedBy = "setOpponentSubmission", fetch = FetchType.LAZY)
    private List<StudentModel> setOpponents;

    public SubmissionModel(String title, String description, String fileType, String fileName, byte[] data, Grade grade, SubmissionType type, List<CommentModel> comments, UserModel user, List<StudentModel> requestedReaders, List<StudentModel> confirmedReaders, List<StudentModel> setOpponents) {
        this.title = title;
        this.description = description;
        this.fileType = fileType;
        this.fileName = fileName;
        this.data = data;
        this.grade = grade;
        this.type = type;
        this.comments = comments;
        this.user = user;
        this.requestedReaders = requestedReaders;
        this.confirmedReaders = confirmedReaders;
        this.setOpponents = setOpponents;
    }

    public List<StudentModel> getConfirmedReaders() {
        return confirmedReaders;
    }

    public void setConfirmedReaders(List<StudentModel> confirmedReaders) {
        this.confirmedReaders = confirmedReaders;
    }

    public List<StudentModel> getRequestedReaders() {
        return requestedReaders;
    }

    public void setRequestedReaders(List<StudentModel> requestedReaders) {
        this.requestedReaders = requestedReaders;
    }

    public SubmissionModel(){}
    public List<CommentModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public SubmissionType getType() {
        return type;
    }

    public void setType(SubmissionType type) {
        this.type = type;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<StudentModel> getSetOpponents() {
        return setOpponents;
    }

    public void setSetOpponents(List<StudentModel> setOpponents) {
        this.setOpponents = setOpponents;
    }
}
