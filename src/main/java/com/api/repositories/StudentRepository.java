package com.api.repositories;

import com.api.models.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentModel, Integer> {
    StudentModel save(StudentModel user);
    StudentModel findByEmail(String email);
    StudentModel findById(int id);
}
