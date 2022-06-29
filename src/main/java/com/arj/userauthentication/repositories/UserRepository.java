package com.arj.userauthentication.repositories;

import com.arj.userauthentication.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {

}
