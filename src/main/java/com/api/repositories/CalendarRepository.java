package com.api.repositories;

import com.api.models.CalendarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarModel, Integer> {
    CalendarModel findById(int id);
}
