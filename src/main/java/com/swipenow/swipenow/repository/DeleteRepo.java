package com.swipenow.swipenow.repository;

import com.swipenow.swipenow.entity.DeletedUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteRepo extends JpaRepository<DeletedUsers, Long> {
}
