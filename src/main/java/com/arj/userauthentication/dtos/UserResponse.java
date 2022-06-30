package com.arj.userauthentication.dtos;

import lombok.Data;

@Data
public class UserResponse {

  private long id;
  private String name;
  private String email;
  private String address;
  private String profile;
}
