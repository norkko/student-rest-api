package com.api.services;

import com.api.controllers.exceptions.BadRequestException;
import com.api.controllers.exceptions.NotFoundException;
import com.api.models.StudentModel;
import com.api.models.SubmissionModel;
import com.api.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public class BiddingService {

    @Autowired
    UserService userService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SubmissionService submissionService;

    /**
     * Method which allows requests to be reader for submissions
     *
     * @param authentication user authentication
     * @param submissionId the submission
     * @return ResponseEntity the response
     */
    public @ResponseBody ResponseEntity requestToReadSubmission(Authentication authentication, int submissionId) {
        try {
            if (submissionService.findById(submissionId) == null) throw new NotFoundException();
            SubmissionModel submission = submissionService.findById(submissionId);
            StudentModel student = studentRepository.findByEmail(authentication.getName());
            student.setRequestedSubmission(submission);
            submission.getRequestedReaders().add(student);
            userService.save(student);
            return new ResponseEntity<>("Request successfully sent", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new BadRequestException("Submission not found");
        } catch (Exception e) {
            throw new BadRequestException("Bad Request: " + e);
        }

    }

    /**
     * Method which allows confirmation on requests to be a reader on submissions
     *
     * @param studentId the student id
     * @param submissionId the submission id
     * @return ResponseEntity the response
     */
    public @ResponseBody ResponseEntity confirmToReadSubmission(int studentId, int submissionId) {
        try {
            if (studentRepository.findById(studentId) == null) throw new NotFoundException();
            if (submissionService.findById(submissionId) == null) throw new NotFoundException();
            StudentModel student = studentRepository.findById(studentId);
            SubmissionModel submission = submissionService.findById(submissionId);
            student.setConfirmedReaderSubmission(submission);
            student.setRequestedSubmission(null);
            submission.getConfirmedReaders().add(student);
            submission.getRequestedReaders().removeIf(studentObj -> studentObj.getId() == studentId);
            userService.save(student);
            return new ResponseEntity<>("Reader successfully confirmed", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("Student or submission not found");
        } catch (Exception e) {
            throw new BadRequestException("Exception: " + e);
        }

    }

    /**
     * Method which allows setting opponents on submissions
     *
     * @param studentId the student id
     * @param submissionId the submission id
     * @return ResponseEntity the response
     */
    public @ResponseBody ResponseEntity setOpponentOnSubmission(int studentId, int submissionId) {
        try {
            if (studentRepository.findById(studentId) == null || submissionService.findById(submissionId) == null) throw new NotFoundException();
            StudentModel student = studentRepository.findById(studentId);
            SubmissionModel submission = submissionService.findById(submissionId);
            student.setSetOpponentSubmission(submission);
            submission.getSetOpponents().add(student);
            userService.save(student);
            return new ResponseEntity<>("Opponent successfully set", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("User or submission not found");
        } catch (Exception e) {
            throw new BadRequestException("Exception: " + e);
        }

    }

    /**
     * Method which allows removing opponents from submissions
     * @param studentId the student id
     * @param submissionId the submission id
     * @return ResponseEntity the response
     */
    public @ResponseBody ResponseEntity removeOpponentFromSubmission(int studentId, int submissionId) {
        try {
            StudentModel student = studentRepository.findById(studentId);
            SubmissionModel submission = submissionService.findById(submissionId);
            student.setSetOpponentSubmission(null);
            submission.getSetOpponents().removeIf(studentObj -> studentObj.getId() == studentId);
            userService.save(student);
            return new ResponseEntity<>("Opponent successfully removed", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("User or submission not found");
        } catch (Exception e) {
            throw new BadRequestException("Exception: " + e);
        }

    }

}
