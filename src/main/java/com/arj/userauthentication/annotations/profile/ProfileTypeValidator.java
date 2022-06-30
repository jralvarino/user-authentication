package com.arj.userauthentication.annotations.profile;

import com.arj.userauthentication.enums.ProfileTypeEnum;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProfileTypeValidator implements ConstraintValidator<ProfileType, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !ProfileTypeEnum.parse(value).isEmpty();
  }
}
