package com.mstftrgt.simplemirket.repository;

import com.mstftrgt.simplemirket.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}