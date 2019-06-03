package com.api.repositories;

import com.api.models.SubmissionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<SubmissionModel, Integer> {
    List<SubmissionModel> findAll();
    SubmissionModel save(SubmissionModel user);
    SubmissionModel findById(int id);
    void deleteById(int id);
    @Query("SELECT t FROM SubmissionModel t")
    List<SubmissionModel> fetchStatusOnStudentSubmissions();
}
