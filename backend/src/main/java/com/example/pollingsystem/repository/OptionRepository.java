package com.example.pollingsystem.repository;

import com.example.pollingsystem.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByPollId(Long pollId);
}
