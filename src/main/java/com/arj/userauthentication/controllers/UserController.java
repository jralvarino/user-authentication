package com.arj.userauthentication.controllers;

import com.arj.userauthentication.dtos.PageResponse;
import com.arj.userauthentication.dtos.UserDTO;
import com.arj.userauthentication.exceptions.NotFoundException;
import com.arj.userauthentication.exceptions.SequenceException;
import com.arj.userauthentication.exceptions.UserAlreadyExistException;
import com.arj.userauthentication.services.UserService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

  private UserService userService;

  public UserController(UserService userService){
    this.userService = userService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) throws SequenceException, UserAlreadyExistException {
    return userService.createUser(userDTO);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserDTO updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody UserDTO userDTO) throws NotFoundException, UserAlreadyExistException {
    return userService.updateUser(userId, userDTO);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteUser(@PathVariable(value = "id") Long userId) throws NotFoundException {
    userService.deleteUser(userId);
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public PageResponse retrieveUsers(@RequestParam int page,
                                    @RequestParam int size,
                                    @RequestParam String sort,
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "email", required = false) String email,
                                    @RequestParam(value = "profile", required = false) String profile) {
    return userService.retrieveUsersWithPagination(page, size, sort, name, email, profile);
  }

}
