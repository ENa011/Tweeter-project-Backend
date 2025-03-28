package com.cogent.tweet.app.project.service;

import com.cogent.tweet.app.project.entity.Tweet;

import java.util.List;

public interface TweetService {
    List<Tweet> getAllTweet();
    List<Tweet> getAllTweetByUsername(String username);
    Tweet createTweet(String username,Tweet tweet);
    Tweet updateTweet(String username, long tweetId, Tweet tweet);
    void deleteTweet(String username, long tweetId);
}
