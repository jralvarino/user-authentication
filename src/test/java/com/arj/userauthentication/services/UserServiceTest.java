package com.arj.userauthentication.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.arj.userauthentication.dtos.PageResponse;
import com.arj.userauthentication.dtos.UserDTO;
import com.arj.userauthentication.dtos.UserResponse;
import com.arj.userauthentication.entities.UserEntity;
import com.arj.userauthentication.enums.ProfileTypeEnum;
import com.arj.userauthentication.enums.SequencesEnum;
import com.arj.userauthentication.exceptions.NotFoundException;
import com.arj.userauthentication.exceptions.SequenceException;
import com.arj.userauthentication.exceptions.UserAlreadyExistException;
import com.arj.userauthentication.repositories.CustomUserInterface;
import com.arj.userauthentication.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest(classes = {UserService.class, ModelMapper.class, BCryptPasswordEncoder.class})
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private ModelMapper modelMapper;

  @MockBean
  private SequenceGeneratorService sequenceGeneratorService;

  @MockBean
  private BCryptPasswordEncoder passwordEncoder;

  @MockBean
  private CustomUserInterface customUserInterface;

  @Test
  void should_create_a_user_successfully() throws SequenceException {
    //Arrange
    UserDTO userMock = new UserDTO("Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.ADMIN.getName());
    Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(convertDtoToEntity(userMock));
    Mockito.when(sequenceGeneratorService.nextSequence(SequencesEnum.SEQUENCE_USERS)).thenReturn(1L);

    //Act
    UserDTO user = null;
    try {
      user = userService.createUser(userMock);
    } catch (Exception e) {
      fail("Exception not expected");
    }

    //Assert
    assertNotNull(user);
    Mockito.verify(sequenceGeneratorService, Mockito.times(1)).nextSequence(SequencesEnum.SEQUENCE_USERS);
    Mockito.verify(passwordEncoder, Mockito.times(1)).encode(userMock.getPassword());
    Mockito.verify(userRepository, Mockito.times(1)).save(any(UserEntity.class));
  }

  @Test
  void should_throw_exception_when_email_already_exist() throws SequenceException {
    //Arrange
    UserDTO userMock = new UserDTO("Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.ADMIN.getName());
    Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(new UserEntity());

    //Act
    UserDTO user = null;
    Exception exception = null;
    try {
      user = userService.createUser(userMock);
    } catch (Exception e) {
      exception = e;
    }

    //Assert
    assertNull(user);
    assertNotNull(exception);
    assertTrue(exception instanceof UserAlreadyExistException);
    Mockito.verify(userRepository, Mockito.times(0)).save(any(UserEntity.class));
  }

  @Test
  void should_retrieve_users_with_pagination_successfully(){
    //Arrange
    Pageable pageable = PageRequest.of(2, 10, Sort.by("name"));
    List<UserEntity> users = new ArrayList<>();
    users.add(new UserEntity(1, "Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.ADMIN));
    users.add(new UserEntity(2, "Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.ADMIN));
    Page<UserEntity> pageUsers = new PageImpl<>(users, pageable, 5);

    Mockito.when(customUserInterface.findAllWithPagination(2, 10, "name", "", "", ProfileTypeEnum.ADMIN)).thenReturn(pageUsers);

    //Act
    PageResponse pageResponse = null;
    try {
      pageResponse = userService.retrieveUsersWithPagination(2, 10, "name", "", "", "ADMIN");
    } catch (Exception e) {
      fail("Exception not expected");
    }

    //Assert
    assertNotNull(pageResponse);
    List<UserResponse> userResponses = (List<UserResponse>) pageResponse.getContent();
    assertEquals(userResponses.size(), 2);
  }

  @Test
  void should_delete_user_when_id_is_valid(){
    //Arrange
    UserEntity user = new UserEntity(1, "Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.ADMIN);

    Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

    //Act
    try {
      userService.deleteUser(1L);
    } catch (Exception e) {
      fail("Exception not expected");
    }
  }

  @Test
  void should_throw_exception_when_try_delete_id_not_existent(){
    //Arrange
    given(userRepository.findById(1L)).willAnswer(invocation -> { throw new NotFoundException("User not found");});

    //Act
    Exception exception = null;
    try {
      userService.deleteUser(1L);
    } catch (Exception e) {
      exception = e;
    }

    //Assert
    assertTrue(exception instanceof NotFoundException);
  }

  @Test
  void should_update_user_when_try_update_user_existent() throws SequenceException {
    //Arrange
    UserDTO userMock = new UserDTO("Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.ADMIN.getName());
    Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity()));
    Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(convertDtoToEntity(userMock));

    //Act
    UserDTO user = null;
    try {
      user = userService.updateUser(1L, userMock);
    } catch (Exception e) {
      fail("Exception not expected");
    }

    //Assert
    assertNotNull(user);
    Mockito.verify(sequenceGeneratorService, Mockito.times(0)).nextSequence(SequencesEnum.SEQUENCE_USERS);
    Mockito.verify(passwordEncoder, Mockito.times(1)).encode(userMock.getPassword());
    Mockito.verify(userRepository, Mockito.times(1)).save(any(UserEntity.class));
  }

  @Test
  void should_throw_exception_when_try_update_user_not_existent(){
    //Arrange
    given(userRepository.findById(1L)).willAnswer(invocation -> { throw new NotFoundException("User not found");});

    //Act
    Exception exception = null;
    try {
      userService.updateUser(1L, any(UserDTO.class));
    } catch (Exception e) {
      exception = e;
    }

    //Assert
    assertTrue(exception instanceof NotFoundException);
  }

  @Test
  void should_throw_exception_when_updateuser_email_already_exist(){
    //Arrange
    UserDTO userMock = new UserDTO("Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.ADMIN.getName());
    Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity()));
    Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(new UserEntity());

    //Act
    Exception exception = null;
    try {
      userService.updateUser(1L, userMock);
    } catch (Exception e) {
      exception = e;
    }

    //Assert
    assertTrue(exception instanceof UserAlreadyExistException);
  }

  private UserEntity convertDtoToEntity(UserDTO userDTO) {
    modelMapper.getConfiguration().setAmbiguityIgnored(true);
    return modelMapper.map(userDTO, UserEntity.class);
  }

}
