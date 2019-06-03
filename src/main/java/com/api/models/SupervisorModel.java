package com.api.models;

import com.fasterxml.jackson.annotation.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Access(AccessType.PROPERTY)
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class SupervisorModel extends UserModel {

    @JsonIgnoreProperties({"supervisor", "requestedSupervisor", "requestedSubmission", "confirmedReaderSubmission", "setOpponentSubmission"})
    private List<StudentModel> students;

    @JsonIgnoreProperties({"supervisor","requestedSupervisor" })
    private List<StudentModel> studentsRequestingSupervision;

    public SupervisorModel() {

    }

    public SupervisorModel(List<StudentModel> students, List<StudentModel> studentsRequestingSupervision) {
        this.students = students;
        this.studentsRequestingSupervision = studentsRequestingSupervision;
    }

    public SupervisorModel(String name, String surname, String email, String password, List<StudentModel> students, List<StudentModel> studentsRequestingSupervision) {
        super(name, surname, email, password);
        this.students = students;
        this.studentsRequestingSupervision = studentsRequestingSupervision;
    }

    @OneToMany(targetEntity = StudentModel.class, mappedBy = "supervisor",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    public List<StudentModel> getStudents() {
        return students;
    }

    public void setStudents(List<StudentModel> students) {
        this.students = students;
    }

    @OneToMany(targetEntity = StudentModel.class, mappedBy = "requestedSupervisor",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    public List<StudentModel> getStudentsRequestingSupervision() {
        return studentsRequestingSupervision;
    }

    public void setStudentsRequestingSupervision(List<StudentModel> studentsRequestingSupervision) {
        this.studentsRequestingSupervision = studentsRequestingSupervision;
    }
}
