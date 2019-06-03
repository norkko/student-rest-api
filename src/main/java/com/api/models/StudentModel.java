package com.api.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIdentityInfo(scope=StudentModel.class, generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class StudentModel extends UserModel {

    @JsonIgnore
    @OneToMany(cascade=CascadeType.MERGE)
    @JoinTable(
            name="student_submission",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="SUBMISSION_ID", referencedColumnName="ID")})
    private List<SubmissionModel> submissions;

    @ManyToOne
    @JsonIgnoreProperties({"students", "comments", "studentsRequestingSupervision"})
    private SupervisorModel supervisor;

    @ManyToOne
    @JsonIgnoreProperties({"students", "comments", "studentsRequestingSupervision"})
    private SupervisorModel requestedSupervisor;

    @ManyToOne
    @JsonIgnoreProperties({"comments", "user", "requestedReaders","confirmedReaders", "setOpponents"})
    private SubmissionModel requestedSubmission;

    @ManyToOne
    @JsonIgnoreProperties({"comments", "user", "requestedReaders","confirmedReaders", "setOpponents"})
    private SubmissionModel confirmedReaderSubmission;

    @ManyToOne
    @JsonIgnoreProperties({"comments", "user", "requestedReaders","confirmedReaders", "setOpponents"})
    private SubmissionModel setOpponentSubmission;

    public StudentModel() {

    }

    public StudentModel(List<SubmissionModel> submissions, SupervisorModel supervisor, SupervisorModel requestedSupervisor, SubmissionModel requestedSubmission, SubmissionModel confirmedReaderSubmission) {
        this.submissions = submissions;
        this.supervisor = supervisor;
        this.requestedSupervisor = requestedSupervisor;
        this.requestedSubmission = requestedSubmission;
        this.confirmedReaderSubmission = confirmedReaderSubmission;
    }

    public StudentModel(String name, String surname, String email, String password, List<SubmissionModel> submissions, SupervisorModel supervisor, SupervisorModel requestedSupervisor, SubmissionModel requestedSubmission, SubmissionModel confirmedReaderSubmission) {
        super(name, surname, email, password);
        this.submissions = submissions;
        this.supervisor = supervisor;
        this.requestedSupervisor = requestedSupervisor;
        this.requestedSubmission = requestedSubmission;
        this.confirmedReaderSubmission = confirmedReaderSubmission;
    }


    public List<SubmissionModel> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<SubmissionModel> submissions) {
        this.submissions = submissions;
    }

    public SupervisorModel getRequestedSupervisor() {
        return requestedSupervisor;
    }

    public void setRequestedSupervisor(SupervisorModel requestedSupervisor) {
        this.requestedSupervisor = requestedSupervisor;
    }

    public SubmissionModel getConfirmedReaderSubmission() {
        return confirmedReaderSubmission;
    }

    public void setConfirmedReaderSubmission(SubmissionModel confirmedReaderSubmission) {
        this.confirmedReaderSubmission = confirmedReaderSubmission;
    }

    public SubmissionModel getRequestedSubmission() {
        return requestedSubmission;
    }

    public void setRequestedSubmission(SubmissionModel requestedSubmission) {
        this.requestedSubmission = requestedSubmission;
    }

    public SupervisorModel getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SupervisorModel supervisor) {
        this.supervisor = supervisor;
    }

    public SubmissionModel getSetOpponentSubmission() {
        return setOpponentSubmission;
    }

    public void setSetOpponentSubmission(SubmissionModel setOpponentSubmission) {
        this.setOpponentSubmission = setOpponentSubmission;
    }
}
