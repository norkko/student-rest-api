package com.api.repositories;

import com.api.models.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentModel, Integer> {
    CommentModel save(CommentModel user);
    CommentModel findById(int id);
}
