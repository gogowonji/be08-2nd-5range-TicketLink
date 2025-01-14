package com.beyond.ticketLink.user.persistence.repository;

import com.beyond.ticketLink.user.application.domain.UserRole;

import java.util.Optional;

public interface UserRoleRepository {
    Optional<UserRole> findByRoleName(String name);
}
