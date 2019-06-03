package com.api.services;

import com.api.controllers.exceptions.InternalErrorException;
import com.api.controllers.exceptions.MethodNotAllowedException;
import com.api.controllers.exceptions.NotFoundException;
import com.api.models.CommentModel;
import com.api.models.SubmissionModel;
import com.api.models.UserModel;
import com.api.repositories.CommentRepository;
import com.api.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    /**
     * Method which allows fetching specific comments
     *
     * @param id the comment id
     * @return ResponseEntity the response
     */
    public @ResponseBody CommentModel getComment(int id) {
        try {
            if (commentRepository.findById(id) == null) {
                throw new NotFoundException();
            }
            return commentRepository.findById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Comment not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which allows removing specific comments
     *
     * @param id the comment id
     * @return ResponseEntity the response
     */
    public ResponseEntity deleteComment(int id) {
        try {
            if (commentRepository.findById(id) == null) {
                throw new NotFoundException();
            }
            commentRepository.deleteById(id);
            return new ResponseEntity<>("Comment deleted", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("Comment not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which allows posting different types of comment on submissions
     *
     * @param authentication user authentication
     * @param id the submission id
     * @param comment the comment model
     * @return ResponseEntity the response
     */
    public ResponseEntity postCommentOnSubmission(Authentication authentication,int id, CommentModel comment) {
        try {
            if (submissionRepository.findById(id) == null) {
                throw new NotFoundException();
            }
            SubmissionModel submission = submissionRepository.findById(id);
            UserModel user = userService.getUserByEmail(authentication.getName());
            submission.getComments().add(comment);
            comment.setSubmission(submission);
            comment.setAuthor(user);
            commentRepository.save(comment);
            return new ResponseEntity<>("Created comment of type " + comment.getType(), HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("Submission not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which allows updating specific comments
     *
     * @param authentication user authentication
     * @param id the submission id
     * @param oldComment the comment model
     * @return ResponseEntity the response
     */
    public ResponseEntity updateComment(Authentication authentication, int id, CommentModel oldComment) {
        try {
            UserModel userModel = userService.getUserByEmail(authentication.getName());
            List<CommentModel> comments = userModel.getComments();
            if (!comments.stream().filter(e -> e.getId() == id).findFirst().isPresent()) {
                throw new MethodNotAllowedException("Not your comment");
            }
            CommentModel comment = commentRepository.findById(id);
            comment.setText(oldComment.getText());
            comment.setType(oldComment.getType());
            commentRepository.save(comment);
            return new ResponseEntity<>("Comment updated", HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

}
