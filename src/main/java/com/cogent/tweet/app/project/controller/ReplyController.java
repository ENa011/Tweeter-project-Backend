package com.cogent.tweet.app.project.controller;

import com.cogent.tweet.app.project.entity.Reply;
import com.cogent.tweet.app.project.service.ReplyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/v1.0/tweets")
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @PostMapping("/{username}/reply/{tweetId}")
    public ResponseEntity<Reply> createReply(@PathVariable("username") String username,
                                             @PathVariable("tweetId") long tweetId,
                                             @Valid @RequestBody Reply reply){
        var data = replyService.createReply(username, tweetId, reply);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PutMapping("/{username}/like/{replyId}")
    public ResponseEntity<Reply> LikedReply(@PathVariable("username") String username,
                                            @PathVariable("replyId") long replyId){
        var data = replyService.LikeTweet(username, replyId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
