package com.api.controllers;

import com.api.services.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/bidding")
public class BiddingController {

    @Autowired
    BiddingService biddingService;

    /**
     * POST /bidding/request/reader/{id}
     * Method which allows requests to be reader for submissions
     *
     * @param authentication user authentication
     * @param submissionId the submission
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/request/reader/{id}")
    public @ResponseBody ResponseEntity requestToReadSubmission(Authentication authentication,@PathVariable(value = "id") int submissionId) {
        return biddingService.requestToReadSubmission(authentication, submissionId);
    }

    /**
     * POST /bidding/confirm/reader/{studentId}/{submissionId}
     * Method which allows confirmation on requests to be a reader on submissions
     *
     * @param studentId the student id
     * @param submissionId the submission id
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/confirm/reader/{studentId}/{submissionId}")
    public @ResponseBody ResponseEntity confirmToReadSubmission(@PathVariable(value = "studentId") int studentId, @PathVariable(value = "submissionId") int submissionId) {
        return biddingService.confirmToReadSubmission(studentId, submissionId);
    }

    /**
     * POST /bidding/set/opponent/{studentId}/{submissionId}
     * Method which allows setting opponents on submissions
     *
     * @param studentId the student id
     * @param submissionId the submission id
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/set/opponent/{studentId}/{submissionId}")
    public @ResponseBody ResponseEntity setOpponentOnSubmission(@PathVariable(value = "studentId") int studentId, @PathVariable(value = "submissionId") int submissionId) {
        return biddingService.setOpponentOnSubmission(studentId, submissionId);
    }

    /**
     * PUT /bidding/remove/opponent/{studentId}/{submissionId}
     * Method which allows removing opponents from submissions
     * @param studentId the student id
     * @param submissionId the submission id
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/remove/opponent/{studentId}/{submissionId}")
    public @ResponseBody ResponseEntity removeOpponentFromSubmission(@PathVariable(value = "studentId") int studentId, @PathVariable(value = "submissionId") int submissionId) {
        return biddingService.removeOpponentFromSubmission(studentId, submissionId);
    }

}
