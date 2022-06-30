package com.arj.userauthentication.repositories;

import com.arj.userauthentication.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, Long> {

}
