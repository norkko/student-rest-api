package com.api.repositories;

import com.api.models.SupervisorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupervisorRepository extends JpaRepository<SupervisorModel, Integer> {
    SupervisorModel findById(int id);
}
