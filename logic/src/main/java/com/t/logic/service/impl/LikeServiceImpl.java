package com.t.logic.service.impl;

import com.mongodb.client.result.UpdateResult;
import com.t.logic.entity.Likes;
import com.t.logic.service.LikesService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl {
  @Autowired
  LikesService likesService;
  @Autowired
  MongoTemplate mongoTemplate;

  public Boolean isLike(Long userId, Long docId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("_id").is(userId).andOperator(Criteria.where("likes").elemMatch(Criteria.where("$eq").is(docId))));
    Likes one = mongoTemplate.findOne(query, Likes.class);
    if (one!=null)
        return true;
    return false;
  }
  public List<Long> getLikes(Long userId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("_id").is(userId));
    Likes one = mongoTemplate.findOne(query, Likes.class);
    if (one!=null) {
      return one.getDoc_Ids();
    }
    return null;
  }
  public Boolean addLike(Long userId, Long docId) {
    Query query = new Query();
//    query.addCriteria(Criteria.where("_id").is(1d).andOperator(Criteria.where("likes").elemMatch(Criteria.where("$eq").is(aDouble))));
    query.addCriteria(Criteria.where("_id").is(userId));
    Likes one = mongoTemplate.findOne(query, Likes.class);
    if (one!=null) {
      if (one.getDoc_Ids().contains(docId)) {
        return true;
      }
      Update update = new Update();
      update.push("likes",docId);
      UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Likes.class);
      if (updateResult.getMatchedCount()!=1) {
        System.out.println(false);
        return false;
      }
      if (updateResult.getModifiedCount() >= 1) {
        System.out.println(true);
        return true;
      }
      System.out.println(false);
      return false;
    }
    Likes insert = likesService.insert(new Likes().setUser_Id(userId).addDoc_Ids(docId));
    if (insert!=null)
      return true;
    return false;
  }
  public Boolean delLike(Long userId, Long docId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("_id").is(userId));
    Likes one = mongoTemplate.findOne(query, Likes.class);
    if (one==null)
      return false;
    if (!one.getDoc_Ids().contains(docId))
      return true;
    Update update = new Update();
    update.pull("likes",docId);
    UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Likes.class);
    if (updateResult.getMatchedCount()!=1) {
      System.out.println(false);
      return false;
    }
    if (updateResult.getModifiedCount() >= 1) {
      System.out.println(true);
      return true;
    }
    return false;
  }

}
