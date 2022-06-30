package com.arj.userauthentication.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

import com.arj.userauthentication.entities.SequenceEntity;
import com.arj.userauthentication.enums.SequencesEnum;
import com.arj.userauthentication.exceptions.SequenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootTest(classes = SequenceGeneratorService.class)
class SequenceGeneratorServiceTest {

  @Autowired
  private SequenceGeneratorService sequenceGeneratorService;

  @MockBean
  private MongoOperations mongoOperations;

  @Test
  void should_return_value_when_sequence_is_not_null(){
    //Arrange
    Query query = Query.query(Criteria.where("_id").is(SequencesEnum.SEQUENCE_USERS.getName()));

    Update update = new Update();
    update.inc("value", 1);

    SequenceEntity sequenceMock = new SequenceEntity(SequencesEnum.SEQUENCE_USERS.getName(), 2);
    Mockito.when(mongoOperations.findAndModify(eq(query), eq(update), any(), eq(SequenceEntity.class))).thenReturn(sequenceMock);

    //Act
    long sequence = 0;
    try {
      sequence = sequenceGeneratorService.nextSequence(SequencesEnum.SEQUENCE_USERS);
    } catch (SequenceException e) {
      Assertions.fail("Exception not expected.");
    }

    //Assert
    assertEquals(sequence, sequenceMock.getValue());
  }

  @Test
  void should_throw_exception_when_sequence_is_not_found(){
    //Arrange
    Mockito.when(mongoOperations.findAndModify(any(), any(), any())).thenReturn(null);

    //Act
    Exception exceptionExpected = null;
    try {
      sequenceGeneratorService.nextSequence(SequencesEnum.SEQUENCE_USERS);
    } catch (SequenceException e) {
      exceptionExpected = e;

    }

    //Assert
    assertNotNull(exceptionExpected);
  }

}
