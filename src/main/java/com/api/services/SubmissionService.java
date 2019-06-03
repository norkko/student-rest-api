package com.api.services;

import com.api.controllers.exceptions.*;
import com.api.models.StudentModel;
import com.api.models.SubmissionModel;
import com.api.repositories.StudentRepository;
import com.api.repositories.SubmissionRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.*;
import springfox.documentation.annotations.ApiIgnore;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.api.models.enums.Grade.F;
import static com.api.models.enums.Grade.PENDING;
import static com.api.models.enums.SubmissionType.*;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Method which finds a file by ID
     *
     * @param id the id of the submission
     * @return SubmissionModel the submission object
     */
    public SubmissionModel findById(int id) {
        return submissionRepository.findById(id);
    }

    /**
     * Method which finds all submissions
     *
     * @return List<SubmissionModel> list of all submissions
     */
    public List<SubmissionModel> findAll() {
       return submissionRepository.findAll();
    }

    /**
     * Method which saves a submission
     *
     * @param submission the submission object to save
     * @return SubmissionModel the saved submission object
     */
    public SubmissionModel save(SubmissionModel submission) {
        return submissionRepository.save(submission);
    }

    /**
     * Method which finds a specific submission file
     *
     * @param id the submission id of the file
     * @return ResponseEntity the response
     */
    public ResponseEntity getFile(int id) throws NotFoundException {
        try {
            SubmissionModel submission = submissionRepository.findById(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String filename = submission.getFileName();
            headers.add("Content-Disposition", "inline; filename=" + submission.getFileName());
            headers.setContentDispositionFormData(filename, filename); // Comment out to view file inside browser
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(submission.getData(), headers, HttpStatus.OK);
            return response;
        } catch (Exception e) {
            throw new NotFoundException("File not found");
        }
    }

    /**
     * Method which allows students to submit to the database
     *
     * @param authentication user authentication
     * @param submission JSON body of submission
     * @param file MultipartFile file to save
     * @return ResponseEntity the response
     */
    @ApiIgnore
    @SuppressWarnings("Duplicates")
    public ResponseEntity createSubmission(Authentication authentication, @ModelAttribute SubmissionModel submission, MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            StudentModel student = studentRepository.findByEmail(authentication.getName());
            if (submission.getType() == PLAN ) {
                if (!student.getSubmissions().stream().filter(e -> e.getType().equals(DESCRIPTION)).findFirst().isPresent()) {
                    throw new BadRequestException("Missing description submission");
                }
                student.getSubmissions().forEach(e -> {
                    if(e.getType() ==  DESCRIPTION) {
                        if(e.getGrade() == PENDING || e.getGrade() == F) {
                            throw new BadRequestException( e.getGrade() + " grade on " + e.getType());
                        }
                    }
                });
            }

            if (submission.getType() == REPORT ) {
                if (!student.getSubmissions().stream().filter(e -> e.getType().equals(PLAN)).findFirst().isPresent() ) {
                    throw new BadRequestException("Missing plan submission");
                }
                student.getSubmissions().forEach(e -> {
                    if (e.getType() ==  PLAN) {
                        if (e.getGrade() == PENDING || e.getGrade() == F) {
                            throw new BadRequestException( e.getGrade() + " grade on " + e.getType());
                        }
                    }
                });
            }
            if (submission.getType() == REPORT_FINAL) {
                if (!student.getSubmissions().stream().filter(e -> e.getType().equals(REPORT)).findFirst().isPresent() ) {
                    throw new BadRequestException("Missing report submission");
                }
            }


            if (fileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence " + fileName);
            }
            submission.setFileName(fileName);
            submission.setFileType(file.getContentType());
            submission.setData(file.getBytes());
            List<SubmissionModel> submissions = student.getSubmissions();
            if (submissions.stream().filter(e -> e.getType().equals(submission.getType())).findFirst().isPresent()) {
                return new ResponseEntity<>( submission.getType() + " already exists", HttpStatus.NOT_ACCEPTABLE);
            }
            submission.setUser(student);
            submissions.add(submission);
            student.setSubmissions(submissions);
            userService.save(student);
            return new ResponseEntity<>("Submission " + submission.getType() + " successfully saved", HttpStatus.OK);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName, ex);
        }
    }

    /**
     * Method which removes a specific submission
     *
     * @param authentication user authentication
     * @param id submission id
     * @return ResponseEntity
     * @throws NotFoundException
     */
    public ResponseEntity removeSubmission(Authentication authentication, int id) throws NotFoundException {
        try {
            if (submissionRepository.findById(id) == null) {
                throw new NotFoundException();
            }
            StudentModel student = studentRepository.findByEmail(authentication.getName());
            List<SubmissionModel> submissions = student.getSubmissions();
            student.getSubmissions().removeIf(submissionObj -> submissionObj.getId() == id);
            student.setSubmissions(submissions);
            userService.save(student);
            submissionRepository.deleteById(id);
            return new ResponseEntity<>("Submission successfully removed", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("Submission not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }
    /**
     * Method which updates a specific submission
     *
     * @param authentication user authentication
     * @param id submission id
     * @return ResponseEntity
     */
    public ResponseEntity updateSubmission(Authentication authentication, int id, @ModelAttribute SubmissionModel submission, @RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {

            if (submissionRepository.findById(id) == null) {
                throw new NotFoundException();
            }

            if (fileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence " + fileName);
            }

           StudentModel student = studentRepository.findByEmail(authentication.getName());
                    //userService.getUserByEmail(authentication.getName());
            List<SubmissionModel> submissions = student.getSubmissions();
            if (!submissions.stream().filter(e -> e.getId() == id).findFirst().isPresent()) {
                throw new MethodNotAllowedException();
            }

            SubmissionModel submissionModel = submissionRepository.findById(id);
            submissionModel.setFileName(fileName);
            submissionModel.setFileType(file.getContentType());
            submissionModel.setData(file.getBytes());
            submissionModel.setTitle(submission.getTitle());
            submissionModel.setDescription(submission.getDescription());
            submissionRepository.save(submissionModel);

            return new ResponseEntity<>("Submission " + submission.getType() + " successfully updated", HttpStatus.OK);
        } catch (IOException ex) {
            throw new FileStorageException("Storing file failed " + fileName, ex);
        } catch (MethodNotAllowedException e) {
            throw new MethodNotAllowedException("Not Allowed");
        } catch (NotFoundException e) {
            throw new NotFoundException("Submission not found");
        }  catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which allows grading on specific submissions
     *
     * @param id the submission id
     * @param submission the submission data containing new grade
     * @return ResponseEntity the response
     */
    public ResponseEntity gradeSubmission(int id, SubmissionModel submission) {
        try {
            if (submissionRepository.findById(id) == null) {
                throw new NotFoundException();
            }
            SubmissionModel submissionModel = submissionRepository.findById(id);
            submissionModel.setGrade(submission.getGrade());
            submissionRepository.save(submissionModel);
            return new ResponseEntity<>("Submission successfully graded", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("Submission not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which fetches all student submission,
     * or shows them as null if non-existent
     *
     * @return List<Object> list of students and each type of submission
     */
    public List<Object> fetchStatusOnStudentSubmissions() {
        try {
            List<SubmissionModel> submissions = submissionRepository.fetchStatusOnStudentSubmissions();
            List<Object> list = new ArrayList<>();
            studentRepository.findAll().forEach(x -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("student", x.getId());
                map.put("DESCRIPTION", null);
                map.put("PLAN", null);
                map.put("REPORT", null);
                map.put("REPORT_FINAL", null);
                submissions.forEach(e -> {
                    if (e.getUser().getId().equals(x.getId())) {
                        if (e.getType().toString().equals("DESCRIPTION")) {
                            map.put("DESCRIPTION", submissionRepository.findById(e.getId()));
                        } else if (e.getType().toString().equals("PLAN")) {
                            map.put("PLAN", submissionRepository.findById(e.getId()));
                        } else if (e.getType().toString().equals("REPORT")) {
                            map.put("REPORT", submissionRepository.findById(e.getId()));
                        } else if (e.getType().toString().equals("REPORT_FINAL")) {
                            map.put("REPORT_FINAL", submissionRepository.findById(e.getId()));
                        }
                    }
                });
                list.add(map);
            });
            return list;
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

}
