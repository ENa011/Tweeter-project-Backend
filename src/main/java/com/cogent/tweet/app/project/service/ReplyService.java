package com.cogent.tweet.app.project.service;

import com.cogent.tweet.app.project.entity.Reply;

public interface ReplyService {
    Reply createReply(String username, long tweetId, Reply reply);
    Reply LikeTweet(String username, long tweetId);
}
