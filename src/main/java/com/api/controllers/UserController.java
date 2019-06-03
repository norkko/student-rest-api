package com.api.controllers;

import com.api.controllers.exceptions.BadRequestException;
import com.api.controllers.exceptions.NotFoundException;
import com.api.models.StudentModel;
import com.api.models.SupervisorModel;
import com.api.models.UserModel;
import com.api.models.enums.RoleWrapper;
import com.api.repositories.UserRepository;
import com.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static com.api.config.security.SecurityConstants.SECRET;

@Controller
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    /**
     * GET /
     * Method which finds all users
     *
     * @return Iterable<UserModel> collection of all users
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public @ResponseBody List<UserModel> getAllUsers() {
        return userService.findAll();
    }

    /**
     * GET /user/{id}
     * Method which finds a user by ID
     *
     * @param id the id of the user
     * @return Map<String, Object> map with user info
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/user/{id}")
    public @ResponseBody Map<String, Object> getUserById(@PathVariable(value  = "id") int id) {
        return userService.getUserById(id);
    }

    /**
     * POST /signup
     * Method which creates a user in the system
     *
     * @param user JSON body
     * @return ResponseEntity the response
     * @throws BadRequestException
     */
    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity createUser(@RequestBody UserModel user) throws BadRequestException {
        return userService.createUser(user);
    }

    /**
     * DELETE /delete/{id}
     * Method which removes a user from the system
     *
     * @param id the user id
     * @return ResponseEntity the response
     * @throws NotFoundException
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/delete/{id}")
    public @ResponseBody ResponseEntity deleteUser(@PathVariable(value = "id") int id) throws NotFoundException {
        return userService.deleteUser(id);
    }

    /**
     * GET /supervisors
     * Method which return all registered supervisors
     *
     * @return Iterable<UserModel> collection of all supervisors
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/supervisors")
    public @ResponseBody Iterable<SupervisorModel> getAllSupervisors() {
        return userService.getAllSupervisors();
    }

    /**
     * GET /students
     * Method which return all registered students
     *
     * @return Iterable<UserModel> collection of all students
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/students")
    public @ResponseBody Iterable<StudentModel> getAllStudents() {
        return userService.getAllStudents();
    }

    /**
     * GET /user
     * Method which fetches callers account information
     *
     * @return Map<String, Object> map containing user information
     */
    @PostMapping(path = "/user")
    public @ResponseBody Map<String, Object> whoAmI(Authentication authentication) {
        return userService.getUserById(userService.getUserByEmail(authentication.getName()).getId());
    }

    /**
     * POST /roles/{id}
     * Method which sets roles for a user object and
     * upgrades the account if appropriate
     *
     * @param id the user id
     * @param roles the selected roles
     * @param header header deciding that accounts will only update roles
     * @return ResponseEntity the response
     */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/roles/{id}", headers = "action=none")
    public @ResponseBody ResponseEntity setUserRoles(@PathVariable(value  = "id") int id, @RequestBody RoleWrapper roles, @RequestHeader(value = "action") String header) throws NotFoundException {
        return userService.setUserRoles(id, roles.getRoles(), header);
    }

    /**
     * POST /roles/{id}
     * Method which sets roles for a user object and
     * upgrades the account if appropriate
     *
     * @param id the user id
     * @param roles the selected roles
     * @param header accounts will upgrade to student and update roles
     * @return ResponseEntity the response
     */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/roles/{id}", headers = "action=student")
    public @ResponseBody ResponseEntity upgradeToStudent(@PathVariable(value  = "id") int id, @RequestBody RoleWrapper roles, @RequestHeader(value = "action") String header) throws NotFoundException {
        return userService.setUserRoles(id, roles.getRoles(), header);
    }

    /**
     * POST /roles/{id}
     * Method which sets roles for a user object and
     * upgrades the account if appropriate
     *
     * @param id the user id
     * @param roles the selected roles
     * @param header accounts will upgrade to supervisor and update roles
     * @return ResponseEntity the response
     */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/roles/{id}", headers = "action=supervisor")
    public @ResponseBody ResponseEntity upgradeToSupervisor(@PathVariable(value  = "id") int id, @RequestBody RoleWrapper roles, @RequestHeader(value = "action") String header) throws NotFoundException {
        return userService.setUserRoles(id, roles.getRoles(), header);
    }

    /**
     * POST /roles/{id}
     * Method which sets roles for a user object and
     * upgrades the account if appropriate
     *
     * @param id the user id
     * @param roles the selected roles
     * @param header accounts will upgrade to coordinator and update roles
     * @return ResponseEntity the response
     */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/roles/{id}", headers = "action=coordinator")
    public @ResponseBody ResponseEntity upgradeToCoordinator(@PathVariable(value  = "id") int id, @RequestBody RoleWrapper roles, @RequestHeader(value = "action") String header) throws NotFoundException {
        return userService.setUserRoles(id, roles.getRoles(), header);
    }

    /**
     * POST /supervise/{student}/{supervisor}
     * Method which confirms supervision requested by a student
     *
     * @param studentId the id of the student
     * @param supervisorId the id of the supervisor
     * @return ResponseEntity the response
     */
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/supervise/confirm/{student}/{supervisor}")
    public @ResponseBody ResponseEntity setSupervisorOnStudent(@PathVariable(value = "student") int studentId, @PathVariable(value = "supervisor") int supervisorId) throws NotFoundException {
        return userService.setSupervisorOnStudent(studentId, supervisorId);
    }

    /**
     * POST /supervise/{student}/{supervisor}
     * Method which allows students to request supervision by a supervisor
     *
     * @param studentId the student id
     * @param supervisorId the supervisor id
     * @return ResponseEntity the response
     * @throws NotFoundException
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/supervise/{student}/{supervisor}")
    public @ResponseBody ResponseEntity requestSupervisor(@PathVariable(value = "student") int studentId, @PathVariable(value = "supervisor") int supervisorId) throws NotFoundException {
        return userService.requestSupervision(studentId, supervisorId);
    }

    /**
     * GET /coordinator/supervision
     * Method which returns all students and their respective supervisor
     * @return Iterable<SupervisorModel> the collection of all users
     */
    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_READER') or hasRole('ROLE_OPPONENT') or hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_COORDINATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/coordinator/supervision")
    public @ResponseBody Iterable<StudentModel> getAllStudentsWithRespectiveSupervisor() {
        return userRepository.findAllStudentsWithRespectiveSupervisor();
    }

}