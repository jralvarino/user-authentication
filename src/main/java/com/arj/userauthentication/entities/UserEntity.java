package com.arj.userauthentication.entities;

import com.arj.userauthentication.enums.ProfileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserEntity {

  @Id
  private long id;
  private String name;
  private String email;
  private String password;
  private String address;
  private ProfileTypeEnum profile;

  public boolean isAdmin(){
    return profile == ProfileTypeEnum.ADMIN;
  }

}
