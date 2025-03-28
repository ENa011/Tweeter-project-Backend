package com.cogent.tweet.app.project.repository;

import com.cogent.tweet.app.project.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByRegisterId(long id);
}
