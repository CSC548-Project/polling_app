package com.example.pollingsystem.repository;

import com.example.pollingsystem.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {}
