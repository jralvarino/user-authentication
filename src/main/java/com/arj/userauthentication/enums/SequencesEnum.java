package com.arj.userauthentication.enums;

import lombok.Getter;

public enum SequencesEnum {

  SEQUENCE_USERS("users_sequence");

  @Getter
  private String name;

  SequencesEnum(String name) {
    this.name = name;
  }
}
