package com.arj.userauthentication.enums;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

public enum ProfileTypeEnum {
  ADMIN("ADMIN"), USER("USER");

  @Getter
  private String name;

  ProfileTypeEnum(String name) {
    this.name = name;
  }

  public static Optional<ProfileTypeEnum> parse(String value) {
    return Arrays.stream(values()).filter(profile -> profile.name().equals(value)).findFirst();
  }

}
