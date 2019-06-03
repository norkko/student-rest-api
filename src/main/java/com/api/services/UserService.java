package com.api.services;

import com.api.controllers.exceptions.BadRequestException;
import com.api.controllers.exceptions.InternalErrorException;
import com.api.controllers.exceptions.NotFoundException;
import com.api.models.*;
import com.api.models.enums.Role;
import com.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.ConstraintViolationException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SupervisorRepository supervisorRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Method which finds all users
     *
     * @return Iterable<UserModel> collection of all users
     */
    public List<UserModel> findAll() {
        return userRepository.findAllUsers();
    }

    /**
     * Method which finds a user by ID
     *
     * @param id the id of the user
     * @return UserModel the user object
     */
    public UserModel findById(int id) {
        return userRepository.findById(id);
    }

    /**
     * Method which deletes user by id
     *
     * @param id the id of user to delete
     */
    public void deleteById(int id) throws NotFoundException {
        try {
            if (userRepository.findById(id) == null) {
                throw new NotFoundException();
            }
            userRepository.deleteById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which saves a user in the system
     *
     * @param user the user object to save
     * @return UserModel the saved user object
     */
    public UserModel save(UserModel user) throws BadRequestException {
        try {
            return userRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new BadRequestException("Missing fields");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which creates a user in the system
     *
     * @param user JSON body
     * @return ResponseEntity the response
     */
    public @ResponseBody ResponseEntity createUser(@RequestBody UserModel user) throws BadRequestException {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new ResponseEntity<>("User successfully created", HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            throw new BadRequestException("Missing fields");
        }  catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which removes a user from the system
     *
     * @param id the user id
     * @return ResponseEntity the response
     * throws NotFoundException
     */
    public @ResponseBody ResponseEntity deleteUser(int id) throws NotFoundException {
        try {
            if (userRepository.findById(id) == null) {
                throw new NotFoundException();
            }
            userRepository.deleteById(id);
            return new ResponseEntity<>("User successfully removed", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which fetches user by email
     *
     * @param email the user object email
     * @return UserModel the user object
     */
    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     *
     * Method which helps authentication
     * @param email the email to authenticate
     * @return UserDetails spring security object
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel userModel = getUserByEmail(email);
        return new User(userModel.getEmail(), userModel.getPassword(), getAuthorities(userModel));
    }

    /**
     * Method which helps the authentication process
     * by fetching the authorities for a user
     *
     * @param user the user in question
     * @return Collection<? extends GrantedAuthority> the generic collection with all user roles
     */
    private static Collection<? extends GrantedAuthority> getAuthorities(UserModel user) {
        String[] userRoles = user.getRoles().stream().map((role) -> role.getRole().name()).toArray(String[]::new);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return authorities;
    }

    /**
     * Method which returns all users with supervisor role
     *
     * @return Iterable<UserModel> collection of all supervisors
     */
    public @ResponseBody Iterable<SupervisorModel> getAllSupervisors() {
        return userRepository.findAllSupervisors();
    }

    /**
     * Method which returns all users with student role
     *
     * @return Iterable<UserModel> collection of all students
     */
    public @ResponseBody Iterable<StudentModel> getAllStudents() {
        return userRepository.findAllStudents();
    }

    /**
     * Method which constructs a map containing
     * information about a user object
     *
     * @return Map<String> map containing user object info
     */
    public @ResponseBody Map<String, Object> getUserById(int id) throws NotFoundException {
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            List<String> roleList = new ArrayList<>();
            UserModel user = userRepository.findById(id);
            for (int i = 0; i < user.getRoles().size(); i++) {
                roleList.add(user.getRoles().get(i).getRole().name());
            }
            map.put("user", user);
            map.put("roles", roleList);
            return map;
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which clears previous roles for a user
     * and collects the newly selected ones and returns them
     *
     * @param user the user
     * @param roles the selected roles
     * @return List<RoleModel> list of roles
     */
    private List<RoleModel> fetchRoles(UserModel user, ArrayList<Role> roles) {
        List<RoleModel> list =  user.getRoles();
        list.clear();
        roles.forEach(e -> {
            if (roleRepository.findByRole(e) == null) {
                RoleModel role = new RoleModel();
                role.setRole(e);
                list.add(role);
            } else {
                list.add(roleRepository.findByRole(e));
            }
        });
        return list;
    }

    /**
     * Method which sets roles for a user object and
     * upgrades the account if appropriate
     *
     * @param id the user id
     * @param roles the selected roles
     * @param header deciding whether an account should be upgraded
     * @return ResponseEntity the response
     */
    @SuppressWarnings("Duplicates")
    public @ResponseBody ResponseEntity setUserRoles(int id, @RequestBody ArrayList<Role> roles, String header) throws NotFoundException {
        try {
            UserModel user = userRepository.findById(id);
            List<RoleModel> list = fetchRoles(user, roles);
            // Upgrading user to student account
            if (header.equals("student")) {
                StudentModel studentModel = new StudentModel();
                studentModel.setName(user.getName());
                studentModel.setPassword(user.getPassword());
                studentModel.setEmail(user.getEmail());
                studentModel.setSurname(user.getSurname());
                studentModel.setRoles(list);
                userRepository.deleteById(user.getId());
                userRepository.save(studentModel);
            }
            // Upgrading user to coordinator account
            else if (header.equals("coordinator")) {
                CoordinatorModel coordinatorModel = new CoordinatorModel();
                coordinatorModel.setName(user.getName());
                coordinatorModel.setPassword(user.getPassword());
                coordinatorModel.setEmail(user.getEmail());
                coordinatorModel.setSurname(user.getSurname());
                coordinatorModel.setRoles(list);
                userRepository.deleteById(user.getId());
                userRepository.save(coordinatorModel);
            }
            // Upgrading user to supervisor account
            else if (header.equals("supervisor")) {
                SupervisorModel supervisorModel = new SupervisorModel();
                supervisorModel.setName(user.getName());
                supervisorModel.setPassword(user.getPassword());
                supervisorModel.setEmail(user.getEmail());
                supervisorModel.setSurname(user.getSurname());
                supervisorModel.setRoles(list);
                userRepository.deleteById(user.getId());
                userRepository.save(supervisorModel);
            }
            // Updating roles
            else if (header.equals("none")) {
                user.setRoles(list);
                userRepository.save(user);
            }
            return new ResponseEntity<>("Roles successfully updated", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found" + e);
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    /**
     * Method which saves a request of supervision by student
     *
     * @param studentId the id of the student
     * @param supervisorId the id of the supervisor
     * @return ResponseEntity the response
     */
    public @ResponseBody ResponseEntity setSupervisorOnStudent(int studentId, int supervisorId) throws NotFoundException {
        try {
            StudentModel student = studentRepository.findById(studentId);
            SupervisorModel supervisor = supervisorRepository.findById(supervisorId);
            supervisor.getStudentsRequestingSupervision().removeIf(studentObj -> studentObj.getId() == studentId);
            student.setRequestedSupervisor(null);
            student.setSupervisor(supervisor);
            supervisor.getStudents().add(student);
            userRepository.save(student);
            return new ResponseEntity<>("Supervision confirmed", HttpStatus.OK);
        } catch (NotFoundException e) {
           throw new NotFoundException("User not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

    public @ResponseBody ResponseEntity requestSupervision(int studentId, int supervisorId) throws NotFoundException {
        try {
            StudentModel student = studentRepository.findById(studentId);
            SupervisorModel supervisor = supervisorRepository.findById(supervisorId);
            if (student.getSupervisor() != null) {
                return new ResponseEntity<>("Already have a supervisor", HttpStatus.OK);
            } else if (student.getRequestedSupervisor() != null) {
                return new ResponseEntity<>("Already have requested a supervisor", HttpStatus.OK);
            }
            student.setRequestedSupervisor(supervisor);
            supervisor.getStudentsRequestingSupervision().add(student);
            userRepository.save(student);
            return new ResponseEntity<>("Requesting supervision successfully", HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("User not found");
        } catch (Exception e) {
            throw new InternalErrorException("An error occurred " + e);
        }
    }

}