package com.ing.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ing.hub.entity.Role;
import com.ing.hub.enums.UserRole;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(UserRole roleName);
    
}
