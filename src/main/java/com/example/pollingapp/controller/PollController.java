package com.example.pollingapp.controller;

import com.example.pollingapp.model.Poll;
import com.example.pollingapp.model.Question;
import com.example.pollingapp.model.Option;
import com.example.pollingapp.repository.PollRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.example.pollingapp.dto.PollCreationDto;
import com.example.pollingapp.dto.QuestionCreationDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PollController {

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    private static final String INVALID_POLL_ID_MESSAGE = "Invalid poll Id:";
    private static final String INVALID_POLL_CODE_MESSAGE = "Invalid poll code: ";
    private static final String REDIRECT_POLL_URL = "redirect:/poll/";
    private static final String POLL_CREATION_DTO = "pollCreationDto";

    private final PollRepository pollRepository;

    public PollController(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @GetMapping("/polls")
    public String index(Model model) {
        model.addAttribute("polls", pollRepository.findAll());
        return "index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createPollForm(Model model) {
        model.addAttribute(POLL_CREATION_DTO, new PollCreationDto());
        return "createPoll";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createPoll(@ModelAttribute PollCreationDto pollCreationDto) {
        logger.debug("Received PollCreationDto: {}", pollCreationDto);
        
        Poll poll = convertDtoToPoll(pollCreationDto);
        logger.debug("Converted Poll: {}", poll);
        
        String uniqueCode = generateUniqueCode();
        poll.setCode(uniqueCode);
        poll.setPublished(false);
        
        // Set the creator of the poll
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        poll.setCreator(username);
        
        pollRepository.save(poll);
        
        logger.debug("Saved Poll: {}", poll);
        
        return REDIRECT_POLL_URL + uniqueCode;
    }

    @GetMapping("/poll/{code}")
    public String viewPoll(@PathVariable String code, Model model) {
        Poll poll = pollRepository.findByCode(code)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_POLL_CODE_MESSAGE + code));
        logger.debug("Retrieved Poll for viewing: {}", poll);
        model.addAttribute("poll", poll);
        return "pollManagement";
    }

    @PostMapping("/poll/{code}/publish")
    public String publishPoll(@PathVariable String code) {
        Poll poll = pollRepository.findByCode(code)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_POLL_CODE_MESSAGE + code));
        poll.setPublished(!poll.isPublished());
        pollRepository.save(poll);
        return REDIRECT_POLL_URL + code;
    }

    @GetMapping("/poll/{code}/edit")
    public String editPoll(@PathVariable String code, Model model) {
        Poll poll = pollRepository.findByCode(code)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_POLL_CODE_MESSAGE + code));
        PollCreationDto pollCreationDto = convertPollToDto(poll);
        model.addAttribute("poll", poll);
        model.addAttribute(POLL_CREATION_DTO, pollCreationDto);
        return "editPoll";
    }

@PostMapping("/poll/{code}/edit")
public String updatePoll(@PathVariable String code, 
                         @Valid @ModelAttribute("pollCreationDto") PollCreationDto pollCreationDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
        return "editPoll";
    }

    try {
        Poll existingPoll = pollRepository.findByCode(code)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_POLL_CODE_MESSAGE + code));
        
        // Update poll properties
        existingPoll.setTitle(pollCreationDto.getTitle());
        
        // Update questions and options
        List<Question> existingQuestions = existingPoll.getQuestions();
        List<Question> updatedQuestions = new ArrayList<>();

        for (QuestionCreationDto questionDto : pollCreationDto.getQuestions()) {
            Question question = existingQuestions.stream()
                .filter(q -> q.getId() != null && q.getId().equals(questionDto.getId()))
                .findFirst()
                .orElse(new Question());
            
            question.setText(questionDto.getText());
            
            List<Option> existingOptions = question.getOptions();
            List<Option> updatedOptions = new ArrayList<>();

            for (String optionText : questionDto.getOptions()) {
                Option option = existingOptions.stream()
                    .filter(o -> o.getText().equals(optionText))
                    .findFirst()
                    .orElse(new Option());
                
                option.setText(optionText);
                if (option.getVotes() == null) {
                    option.setVotes(0);
                }
                updatedOptions.add(option);
            }

            question.getOptions().clear();
            question.getOptions().addAll(updatedOptions);
            updatedQuestions.add(question);
        }

        existingPoll.getQuestions().clear();
        existingPoll.getQuestions().addAll(updatedQuestions);
        
        pollRepository.save(existingPoll);
        redirectAttributes.addFlashAttribute("message", "Poll updated successfully");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error updating poll: " + e.getMessage());
    }

    return REDIRECT_POLL_URL + code;
}

    @GetMapping("/poll/{code}/results")
    public String viewResults(@PathVariable String code, Model model) {
        Poll poll = pollRepository.findByCode(code)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_POLL_CODE_MESSAGE + code));
        model.addAttribute("poll", poll);
        return "voteResult";
    }

    @GetMapping("/poll")
    public String showPoll(@RequestParam String code, Model model) {
        Poll poll = pollRepository.findByCode(code)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_POLL_CODE_MESSAGE + code));
        model.addAttribute("poll", poll);
        return "pollDetails";
    }

    @PostMapping("/vote/{pollId}")
    public String vote(@PathVariable Long pollId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Poll poll = pollRepository.findById(pollId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_POLL_ID_MESSAGE + pollId));
        
        List<Question> questions = poll.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            String paramName = "question" + i;
            String optionIndex = request.getParameter(paramName);
            if (optionIndex != null) {
                int index = Integer.parseInt(optionIndex);
                Question question = questions.get(i);
                Option option = question.getOptions().get(index);
                option.setVotes(option.getVotes() + 1);
            }
        }
        
        pollRepository.save(poll);
        
        redirectAttributes.addFlashAttribute("pollId", pollId);
        return "redirect:/voteResult";
    }

    @GetMapping("/voteResult")
    public String voteResult(@ModelAttribute("pollId") Long pollId, Model model) {
        Poll poll = pollRepository.findById(pollId)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_POLL_ID_MESSAGE + pollId));
        model.addAttribute("poll", poll);
        return "userResults";
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private Poll convertDtoToPoll(PollCreationDto pollCreationDto) {
        Poll poll = new Poll();
        poll.setTitle(pollCreationDto.getTitle());
        
        List<Question> questions = new ArrayList<>();
        for (QuestionCreationDto questionDto : pollCreationDto.getQuestions()) {
            Question question = new Question();
            question.setText(questionDto.getText());
            
            List<Option> options = new ArrayList<>();
            for (String optionText : questionDto.getOptions()) {
                Option option = new Option();
                option.setText(optionText);
                option.setVotes(0);
                options.add(option);
            }
            question.setOptions(options);
            questions.add(question);
        }
        poll.setQuestions(questions);
        
        logger.debug("Converted PollCreationDto to Poll: {}", poll);
        return poll;
    }

    private PollCreationDto convertPollToDto(Poll poll) {
        PollCreationDto dto = new PollCreationDto();
        dto.setTitle(poll.getTitle());
        List<QuestionCreationDto> questionDtos = new ArrayList<>();
        for (Question question : poll.getQuestions()) {
            QuestionCreationDto questionDto = new QuestionCreationDto();
            questionDto.setText(question.getText());
            questionDto.setOptions(question.getOptions().stream().map(Option::getText).collect(Collectors.toList()));
            questionDtos.add(questionDto);
        }
        dto.setQuestions(questionDtos);
        return dto;
    }
    @GetMapping("/my-polls")
    public String showUserPolls(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Poll> userPolls = pollRepository.findByCreator(username);
        model.addAttribute("polls", userPolls);
        return "index";
    }
}