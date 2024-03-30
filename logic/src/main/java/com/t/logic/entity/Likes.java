package com.t.logic.entity;

import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "userlike")
@Data
@Accessors(chain = true)
public class Likes {
  @Field("_id")
  public Long user_Id;
  @Field("likes")
  public List<Long>  doc_Ids;

  public Likes addDoc_Ids(Long doc_Id){
    if (doc_Ids!=null)
      doc_Ids.add(doc_Id);
    else doc_Ids= Arrays.asList(doc_Id);
    return this;
  }
}
