package com.api.controllers;

import com.api.controllers.exceptions.NotFoundException;
import com.api.models.SubmissionModel;
import com.api.services.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Controller
@RequestMapping("/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    /**
     * GET /submissions
     * Method which returns all submissions
     *
     * @return Iterable<SubmissionModel> collection of all submissions
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public @ResponseBody Iterable<SubmissionModel> getAllSubmissions() {
        return submissionService.findAll();
    }

    /**
     * GET /submissions/{id}
     * Method which returns a specific submission
     *
     * @return a specific submission
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public @ResponseBody SubmissionModel getSpecificSubmission(@PathVariable(value = "id") int id) {
        return submissionService.findById(id);
    }

    /**
     * GET /submissions/file/{id}
     * Method which finds a specific submission file
     *
     * @param id the submission id of the file
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("/file/{id}")
    public @ResponseBody ResponseEntity getFile(@PathVariable(value = "id") int id) {
        return submissionService.getFile(id);
    }

    /**
     * POST /submissions
     * Method which allows students to submit submissions to the system
     *
     * @param authentication user authentication
     * @param submission JSON body of submission
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @ApiIgnore
    @PostMapping(path = "/create")
    public ResponseEntity createSubmission(Authentication authentication, @ModelAttribute SubmissionModel submission, @RequestParam("file") MultipartFile file) {
        return submissionService.createSubmission(authentication, submission, file);
    }

    /**
     * DELETE /remove/{id}
     * Method which removes specific submissions
     *
     * @param authentication user authentication
     * @param id submission id
     * @return ResponseEntity
     * @throws NotFoundException
     */
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @DeleteMapping(path = "/remove/{id}")
    public ResponseEntity removeSubmission(Authentication authentication, @PathVariable(value = "id") int id) throws NotFoundException {
        return submissionService.removeSubmission(authentication, id);
    }

    /**
     * PUT /submissions/update/{id}
     * Method which allows students to update a specific submission
     *
     * @param authentication user authentication
     * @param submission JSON body of submission
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @ApiIgnore
    @PutMapping(path = "/update/{id}")
    public ResponseEntity updateSubmission(Authentication authentication, @PathVariable(value = "id") int id, @ModelAttribute SubmissionModel submission, @RequestParam("file") MultipartFile file) {
        return submissionService.updateSubmission(authentication, id, submission, file);
    }

    /**
     * PUT /submissions/grade/{id}
     * Method which allows grading on specific submissions
     *
     * @param id the submission id
     * @param submission the submission data containing new grade
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @ApiIgnore
    @PutMapping(path = "/grade/{id}")
    public ResponseEntity setGradeOnSubmission(@PathVariable(value = "id") int id, @ModelAttribute SubmissionModel submission) {
        return submissionService.gradeSubmission(id, submission);
    }

    /**
     * GET /submissions/view
     * Method which fetches all student submission,
     * or shows them as null if non-existent
     *
     * @return List<Object> list of students and each type of submission
     */
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/view")
    public @ResponseBody List<Object> fetchStatusOnStudentSubmissions() {
        return submissionService.fetchStatusOnStudentSubmissions();
    }
}
