package com.arj.userauthentication.repositories;

import com.arj.userauthentication.entities.UserEntity;
import com.arj.userauthentication.enums.ProfileTypeEnum;
import org.springframework.data.domain.Page;

public interface CustomUserInterface  {

  Page<UserEntity> findAllWithPagination(int page, int size, String sort, String name, String email, ProfileTypeEnum profile);

}
