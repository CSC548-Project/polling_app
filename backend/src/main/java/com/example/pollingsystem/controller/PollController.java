package com.example.pollingsystem.controller;

import com.example.pollingsystem.entity.Option;
import com.example.pollingsystem.entity.Poll;
import com.example.pollingsystem.entity.Vote;
import com.example.pollingsystem.repository.OptionRepository;
import com.example.pollingsystem.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polls")
public class PollController {
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private OptionRepository optionRepository;

    @PostMapping("/create")
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
        Poll savedPoll = pollRepository.save(poll);
        return ResponseEntity.ok(savedPoll);
    }

    @PostMapping("/{pollId}/vote")
    public ResponseEntity<Option> voteOnPoll(@PathVariable Long pollId, @RequestBody Vote vote) {
        Option option = optionRepository.findById(vote.getOption().getId()).orElseThrow();
        option.setVotes(option.getVotes() + 1);
        optionRepository.save(option);
        return ResponseEntity.ok(option);
    }

    @GetMapping("/{pollId}/results")
    public ResponseEntity<List<Option>> getPollResults(@PathVariable Long pollId) {
        List<Option> options = optionRepository.findByPollId(pollId);
        return ResponseEntity.ok(options);
    }
}
