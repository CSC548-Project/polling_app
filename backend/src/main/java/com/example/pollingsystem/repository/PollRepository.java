package com.example.pollingsystem.repository;

import com.example.pollingsystem.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {}
