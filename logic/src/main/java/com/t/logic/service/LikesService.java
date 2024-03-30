package com.t.logic.service;

import com.t.logic.entity.Likes;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikesService extends MongoRepository<Likes, Integer> {

}
