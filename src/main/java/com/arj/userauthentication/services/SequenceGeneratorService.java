package com.arj.userauthentication.services;

import com.arj.userauthentication.entities.SequenceEntity;
import com.arj.userauthentication.enums.SequencesEnum;
import com.arj.userauthentication.exceptions.SequenceException;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class SequenceGeneratorService {

  private MongoOperations mongoOperations;

  public SequenceGeneratorService(MongoOperations mongoOperations){
    this.mongoOperations = mongoOperations;
  }

  public long nextSequence(SequencesEnum sequenceEnum) throws SequenceException {
    Query query = Query.query(Criteria.where("_id").is(sequenceEnum.getName()));

    Update update = new Update();
    update.inc("value", 1);

    FindAndModifyOptions options = new FindAndModifyOptions();
    options.returnNew(true).upsert(true);

    SequenceEntity sequence = mongoOperations.findAndModify(query, update, options, SequenceEntity.class);

    if (sequence == null) throw new SequenceException("Unable to get sequence id for key : " + sequenceEnum.getName());

    return sequence.getValue();
  }

}
