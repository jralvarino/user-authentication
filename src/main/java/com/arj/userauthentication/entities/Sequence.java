package com.arj.userauthentication.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "sequence")
public class Sequence {

  @Id
  private String id;
  private long value;

}
