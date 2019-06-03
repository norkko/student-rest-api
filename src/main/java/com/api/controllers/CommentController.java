package com.api.controllers;

import com.api.models.CommentModel;
import com.api.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    /**
     * POST /comment/add/{id}
     * Method which allows posting different types of comment on submissions
     *
     * @param authentication user authentication
     * @param id the submission id
     * @param comment the comment model
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/add/{id}")
    public ResponseEntity postCommentOnSubmission(Authentication authentication, @PathVariable(value = "id") int id, @RequestBody CommentModel comment) {
        return commentService.postCommentOnSubmission(authentication,id, comment);
    }

    /**
     * GET /comment/{id}
     * Method which allows fetching specific comments
     *
     * @param id the comment id
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/{id}")
    public  @ResponseBody CommentModel getComment(@PathVariable(value = "id") int id) {
        return commentService.getComment(id);
    }

    /**
     * DELETE /comment/delete/{id}
     * Method which allows removing specific comments
     *
     * @param id the comment id
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity deleteComment(@PathVariable(value = "id") int id) {
        return commentService.deleteComment(id);
    }

    /**
     * PUT /comment/update/{id}
     * Method which allows updating specific comments
     * 
     * @param authentication user authentication
     * @param id the submission id
     * @param comment the comment model
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/update/{id}")
    public ResponseEntity updateComment(Authentication authentication, @PathVariable(value = "id") int id, @RequestBody CommentModel comment) {
        return commentService.updateComment(authentication,id, comment);
    }

}
