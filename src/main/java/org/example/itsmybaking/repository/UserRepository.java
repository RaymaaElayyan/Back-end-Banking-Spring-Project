package org.example.itsmybaking.repository;

import org.example.itsmybaking.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<user, Long> {
    Boolean existsByEmail(String email);
    user findByUsername(String username);

    boolean existsByAccountNumber(String accountNumber);
    boolean existsById(Long id);
    user findByAccountNumber(String accountNumber);
}
