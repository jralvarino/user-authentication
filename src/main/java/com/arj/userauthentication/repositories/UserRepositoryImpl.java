package com.arj.userauthentication.repositories;

import com.arj.userauthentication.entities.UserEntity;
import com.arj.userauthentication.enums.ProfileTypeEnum;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryImpl implements CustomUserInterface {

  private MongoOperations mongoOperations;

  public UserRepositoryImpl(MongoOperations mongoOperations){
    this.mongoOperations = mongoOperations;
  }

  @Override
  public Page<UserEntity> findAllWithPagination(int page, int size, String sort, String name, String email, ProfileTypeEnum profile) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
    var query = new Query().with(pageable);

    List<Criteria> criteria = new ArrayList<>();

    if(name != null && !name.isEmpty()){
      criteria.add(Criteria.where("name").regex(name, "i"));
    }
    if(email != null && !email.isEmpty()){
      criteria.add(Criteria.where("email").regex(email, "i"));
    }
    if(profile != null){
      criteria.add(Criteria.where("profile").is(profile.getName()));
    }
    if(!criteria.isEmpty()){
      query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
    }

    List<UserEntity> users = mongoOperations.find(query, UserEntity.class);

    long count = mongoOperations.count(query.skip(-1).limit(-1), UserEntity.class);

    return new PageImpl<>(users, pageable, count);
  }




}
