package com.arj.userauthentication.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

import com.arj.userauthentication.entities.UserEntity;
import com.arj.userauthentication.enums.ProfileTypeEnum;
import com.arj.userauthentication.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@SpringBootTest(classes = AuthenticationService.class)
public class AuthenticationServiceTest {

  @Autowired
  private AuthenticationService authenticationService;

  @MockBean
  private UserRepository userRepository;

  @Test
  void should_return_user_admin_when_authentication_is_called(){
    //Arrange
    String email = "asdf@gmail.com";
    UserEntity userMock = new UserEntity(1, "Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.ADMIN);

    Mockito.when(userRepository.findByEmail(email)).thenReturn(userMock);

    //Act
    UserDetails userDetails = authenticationService.loadUserByUsername(email);

    //Assert
    assertNotNull(userDetails);
    assertEquals(userDetails.getUsername(), email);
    assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
  }

  @Test
  void should_return_user_user_when_authentication_is_called(){
    //Arrange
    String email = "asdf@gmail.com";
    UserEntity userMock = new UserEntity(1, "Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.USER);

    Mockito.when(userRepository.findByEmail(email)).thenReturn(userMock);

    //Act
    UserDetails userDetails = authenticationService.loadUserByUsername(email);

    //Assert
    assertNotNull(userDetails);
    assertEquals(userDetails.getUsername(), email);
    assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
  }

  @Test
  void should_return_exception_when_user_not_exists(){
    //Arrange
    Mockito.when(userRepository.findByEmail(anyString())).thenReturn(null);

    //Act
    UserDetails userDetails = null;
    Exception exception = null;
    try {
      userDetails = authenticationService.loadUserByUsername("xpto@gmail.com");
    }catch (Exception e){
      exception = e;
    }

    //Assert
    assertNull(userDetails);
    assertTrue(exception instanceof UsernameNotFoundException);
  }

}
