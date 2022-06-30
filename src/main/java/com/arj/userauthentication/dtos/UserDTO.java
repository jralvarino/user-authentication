package com.arj.userauthentication.dtos;

import com.arj.userauthentication.annotations.profile.ProfileType;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDTO {

  @NotEmpty(message = "Name must to be informed")
  private String name;

  @NotEmpty(message = "Email must to be informed")
  private String email;

  @NotEmpty(message = "Password must to be informed")
  private String password;

  private String address;

  @ProfileType(message = "Profile not found")
  private String profile;

}
