package com.beyond.ticketLink.user.persistence.repository;

import com.beyond.ticketLink.user.application.domain.TicketLinkUserDetails;
import com.beyond.ticketLink.user.persistence.dto.UserCreateDto;

import java.util.Optional;

public interface UserRepository {

    Optional<TicketLinkUserDetails> findUserById(String id);

    void save(UserCreateDto user);

}
