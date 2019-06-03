package com.api.repositories;

import com.api.models.enums.Role;
import com.api.models.RoleModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleModel, Integer> {
    RoleModel findByRole(Role role);
}
