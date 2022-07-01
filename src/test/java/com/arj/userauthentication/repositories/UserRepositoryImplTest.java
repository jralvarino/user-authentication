package com.arj.userauthentication.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.arj.userauthentication.entities.UserEntity;
import com.arj.userauthentication.enums.ProfileTypeEnum;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootTest(classes = UserRepositoryImpl.class)
public class UserRepositoryImplTest {

  @Autowired
  private UserRepositoryImpl userRepository;

  @MockBean
  private MongoOperations mongoOperations;

  @Test
  void should_build_query_when_all_parameters_are_sent(){
    //Arrange
    Query queryMock = createQueryToCompare(1, 10, "name", "Xpto", "xpto@gmail.com", ProfileTypeEnum.ADMIN);
    when(mongoOperations.find(queryMock, UserEntity.class)).thenReturn(new ArrayList<>());

    //Act
    Page page = null;
    try{
      page = userRepository.findAllWithPagination(1, 10, "name", "Xpto", "xpto@gmail.com", ProfileTypeEnum.ADMIN);
    }catch (Exception e){
      fail("Exception not expected");
    }

    //Assert
    assertNotNull(page);
    Mockito.verify(mongoOperations, Mockito.times(1)).find(any(), any());
    Mockito.verify(mongoOperations, Mockito.times(1)).count(queryMock.skip(-1).limit(-1), UserEntity.class);
  }

  @Test
  void should_build_query_when_only_required_parameters_are_sent(){
    //Arrange
    Query queryMock = createQueryToCompare(1, 10, "name", null, null, null);
    when(mongoOperations.find(queryMock, UserEntity.class)).thenReturn(new ArrayList<>());

    //Act
    Page page = null;
    try{
      page = userRepository.findAllWithPagination(1, 10, "name", null, null, null);
    }catch (Exception e){
      fail("Exception not expected");
    }

    //Assert
    assertNotNull(page);
    Mockito.verify(mongoOperations, Mockito.times(1)).find(any(), any());
    Mockito.verify(mongoOperations, Mockito.times(1)).count(queryMock.skip(-1).limit(-1), UserEntity.class);
  }

  private Query createQueryToCompare(int page, int size, String sort, String name, String email, ProfileTypeEnum profile){
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

    return query;
  }

}
