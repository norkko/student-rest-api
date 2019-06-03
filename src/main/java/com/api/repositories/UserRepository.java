package com.api.repositories;

import com.api.models.StudentModel;
import com.api.models.SupervisorModel;
import com.api.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {
    UserModel findById(int id);
    @Query("SELECT NEW map(email as email, surname as surname, id as id, name as name) FROM UserModel")
    List<UserModel> findAllUsers();
    @Query("SELECT t FROM SupervisorModel t")
    List<SupervisorModel> findAllSupervisors();
    @Query("SELECT NEW map(email as email, surname as surname, id as id, name as name) FROM StudentModel")
    List<StudentModel> findAllStudents();
    @Query("SELECT t FROM StudentModel t")
    List<StudentModel> findAllStudentsWithRespectiveSupervisor();
    List<UserModel> findAll();
    void deleteById(int id);
    UserModel save(UserModel user);
    UserModel findByEmail(String email);
}
