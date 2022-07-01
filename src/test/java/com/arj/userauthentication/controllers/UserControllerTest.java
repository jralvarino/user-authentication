package com.arj.userauthentication.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.arj.userauthentication.dtos.PageResponse;
import com.arj.userauthentication.dtos.UserDTO;
import com.arj.userauthentication.dtos.UserResponse;
import com.arj.userauthentication.enums.ProfileTypeEnum;
import com.arj.userauthentication.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@AutoConfigureMockMvc
@EnableWebMvc
@SpringBootTest
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  //----------------- CREATE USER -----------------
  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_ADMIN" })
  public void should_return_201_when_call_createuser() throws Exception {
    //Arrange
    UserDTO userMock = new UserDTO("Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.USER.getName());
    when(userService.createUser(userMock)).thenReturn(any());
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = getMvcResult(ow.writeValueAsString(userMock));

    //Assert
    assertEquals(201, mvcResult.getResponse().getStatus());
    verify(userService, times(1)).createUser(userMock);
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_USER" })
  public void should_return_400_when_call_createuser_with_user_profile() throws Exception {
    //Arrange
    UserDTO userMock = new UserDTO("Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.USER.getName());
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = getMvcResult(ow.writeValueAsString(userMock));

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).createUser(userMock);
    assertEquals("{\"message\":\"Operation not allowed for this user level\",\"details\":[\"Access is denied\"]}", mvcResult.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_ADMIN" })
  public void should_return_400_when_call_createuser_without_name() throws Exception {
    //Arrange
    UserDTO userMock = new UserDTO("", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.ADMIN.getName());
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = getMvcResult(ow.writeValueAsString(userMock));

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).createUser(userMock);
    assertEquals("{\"message\":\"Data validation failed\",\"details\":[\"Name must to be informed\"]}", mvcResult.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_ADMIN" })
  public void should_return_400_when_call_createuser_without_email() throws Exception {
    //Arrange
    UserDTO userMock = new UserDTO("Test", null, "123", "Floripa", ProfileTypeEnum.ADMIN.getName());
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = getMvcResult(ow.writeValueAsString(userMock));

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).createUser(userMock);
    assertEquals("{\"message\":\"Data validation failed\",\"details\":[\"Email must to be informed\"]}", mvcResult.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_ADMIN" })
  public void should_return_400_when_call_createuser_without_password() throws Exception {
    //Arrange
    UserDTO userMock = new UserDTO("Test", "teste@gmail.com", "", "Floripa", ProfileTypeEnum.ADMIN.getName());
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = getMvcResult(ow.writeValueAsString(userMock));

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).createUser(userMock);
    assertEquals("{\"message\":\"Data validation failed\",\"details\":[\"Password must to be informed\"]}", mvcResult.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_ADMIN" })
  public void should_return_400_when_call_createuser_without_profile() throws Exception {
    //Arrange
    UserDTO userMock = new UserDTO("Test", "teste@gmail.com", "123456", "Floripa", null);
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = getMvcResult(ow.writeValueAsString(userMock));

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).createUser(userMock);
    assertEquals("{\"message\":\"Data validation failed\",\"details\":[\"Profile not found\"]}", mvcResult.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_ADMIN" })
  public void should_return_400_when_call_createuser_without_wrong_profile() throws Exception {
    //Arrange
    UserDTO userMock = new UserDTO("Test", "teste@gmail.com", "123456", "Floripa", "ABCD");
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = getMvcResult(ow.writeValueAsString(userMock));

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).createUser(userMock);
    assertEquals("{\"message\":\"Data validation failed\",\"details\":[\"Profile not found\"]}", mvcResult.getResponse().getContentAsString());
  }

  //----------------- UPDATE USER -----------------
  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_ADMIN" })
  public void should_return_200_when_call_updateuser() throws Exception {
    //Arrange
    UserDTO userMock = new UserDTO("Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.USER.getName());
    when(userService.updateUser(1l, userMock)).thenReturn(userMock);
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = mockMvc.perform(
            put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(userMock))
                .accept(MediaType.APPLICATION_JSON)
        )
        .andReturn();

    //Assert
    assertEquals(200, mvcResult.getResponse().getStatus());
    verify(userService, times(1)).updateUser(any(), any());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_USER" })
  public void should_return_400_when_call_updateuser_with_user_profile() throws Exception {
    //Arrange
    UserDTO userMock = new UserDTO("Xpto", "asdf@gmail.com", "123", "Floripa", ProfileTypeEnum.USER.getName());
    when(userService.updateUser(1l, userMock)).thenReturn(userMock);
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = mockMvc.perform(
            put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(userMock))
                .accept(MediaType.APPLICATION_JSON)
        )
        .andReturn();

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).updateUser(any(), any());
    assertEquals("{\"message\":\"Operation not allowed for this user level\",\"details\":[\"Access is denied\"]}", mvcResult.getResponse().getContentAsString());
  }

  //UpdateUser has the same validations as createUser, so is already tested at the user test section.

  //----------------- DELETE USER -----------------
  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_ADMIN" })
  public void should_return_200_when_call_deleteuser() throws Exception {
    //Arrange
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = mockMvc.perform(
            delete("/user/1"))
        .andReturn();

    //Assert
    assertEquals(200, mvcResult.getResponse().getStatus());
    verify(userService, times(1)).deleteUser(any());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_USER" })
  public void should_return_400_when_call_deleteuser_with_user_profile() throws Exception {
    //Arrange
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = mockMvc.perform(
            delete("/user/1"))
        .andReturn();

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).updateUser(any(), any());
    assertEquals("{\"message\":\"Operation not allowed for this user level\",\"details\":[\"Access is denied\"]}", mvcResult.getResponse().getContentAsString());
  }

  //----------------- RETRIEVE USERS -----------------
  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_USER" })
  public void should_return_200_when_call_retrieveusers_only_required_fields() throws Exception {
    //Arrange
    UserResponse userMockResponse = new UserResponse(1L, "Xpto", "asdf@gmail.com", "Floripa", ProfileTypeEnum.USER.getName());
    PageResponse pageResponse = new PageResponse(userMockResponse, 1, 1, 1);
    when(userService.retrieveUsersWithPagination(anyInt(), anyInt(), any(), any(), any(), any())).thenReturn(pageResponse);
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = mockMvc.perform(
            get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andReturn();

    //Assert
    assertEquals(200, mvcResult.getResponse().getStatus());
    verify(userService, times(1)).retrieveUsersWithPagination(anyInt(), anyInt(), any(), any(), any(), any());
    assertEquals("{\"content\":{\"id\":1,\"name\":\"Xpto\",\"email\":\"asdf@gmail.com\",\"address\":\"Floripa\",\"profile\":\"USER\"},\"currentPage\":1,\"totalItems\":1,\"totalPages\":1}", mvcResult.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_USER" })
  public void should_return_200_when_call_retrieveusers_all_fields() throws Exception {
    //Arrange
    UserResponse userMockResponse = new UserResponse(1L, "Xpto", "asdf@gmail.com", "Floripa", ProfileTypeEnum.USER.getName());
    PageResponse pageResponse = new PageResponse(userMockResponse, 1, 1, 1);
    when(userService.retrieveUsersWithPagination(anyInt(), anyInt(), any(), any(), any(), any())).thenReturn(pageResponse);
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = mockMvc.perform(
            get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "5")
                .param("size", "10")
                .param("sort", "email")
                .param("name", "xpto")
                .param("email", "xpto@gmail.com")
                .param("profile", "USER")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andReturn();

    //Assert
    assertEquals(200, mvcResult.getResponse().getStatus());
    verify(userService, times(1)).retrieveUsersWithPagination(5, 10, "email", "xpto", "xpto@gmail.com", "USER");
    assertEquals("{\"content\":{\"id\":1,\"name\":\"Xpto\",\"email\":\"asdf@gmail.com\",\"address\":\"Floripa\",\"profile\":\"USER\"},\"currentPage\":1,\"totalItems\":1,\"totalPages\":1}", mvcResult.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_USER" })
  public void should_return_400_when_call_retrieveusers_without_size() throws Exception {
    //Arrange
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = mockMvc.perform(
            get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("sort", "name")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andReturn();

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).retrieveUsersWithPagination(anyInt(), anyInt(), any(), any(), any(), any());
    assertEquals("{\"message\":\"Unable to complete request.\",\"details\":[\"Required request parameter 'size' for method parameter type int is not present\"]}", mvcResult.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_USER" })
  public void should_return_400_when_call_retrieveusers_without_page() throws Exception {
    //Arrange
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = mockMvc.perform(
            get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("size", "10")
                .param("sort", "name")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andReturn();

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).retrieveUsersWithPagination(anyInt(), anyInt(), any(), any(), any(), any());
    assertEquals("{\"message\":\"Unable to complete request.\",\"details\":[\"Required request parameter 'page' for method parameter type int is not present\"]}", mvcResult.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(username = "asdf@gmail.com", authorities = { "ROLE_USER" })
  public void should_return_400_when_call_retrieveusers_without_sort() throws Exception {
    //Arrange
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Act
    MvcResult mvcResult = mockMvc.perform(
            get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andReturn();

    //Assert
    assertEquals(400, mvcResult.getResponse().getStatus());
    verify(userService, times(0)).retrieveUsersWithPagination(anyInt(), anyInt(), any(), any(), any(), any());
    assertEquals("{\"message\":\"Unable to complete request.\",\"details\":[\"Required request parameter 'sort' for method parameter type String is not present\"]}", mvcResult.getResponse().getContentAsString());
  }

  private MvcResult getMvcResult(String content) throws Exception {
    return mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

}
