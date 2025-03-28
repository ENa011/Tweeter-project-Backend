package com.cogent.tweet.app.project.controller;

import com.cogent.tweet.app.project.entity.Tweet;
import com.cogent.tweet.app.project.service.TweetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/v1.0/tweets")
public class TweetController {

    @Autowired
    TweetService tweetService;

    @GetMapping("/all")
    public ResponseEntity<List<Tweet>> getAllTweet() {
        var data = tweetService.getAllTweet();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Tweet>> getAllTweetByUserName(@PathVariable("username") String username){
        var data = tweetService.getAllTweetByUsername(username);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/{username}/add")
    public ResponseEntity<Tweet> createTweet(@PathVariable("username")String username,
                                             @Valid @RequestBody Tweet tweet){
        var data = tweetService.createTweet(username, tweet);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PutMapping("/{username}/update/{tweetId}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable("username") String username,
                                             @PathVariable("tweetId") long tweetId,
                                             @Valid @RequestBody Tweet tweet){
        var data = tweetService.updateTweet(username, tweetId, tweet);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/{username}/delete/{tweetId}")
    public ResponseEntity<String> deleteTweet(@PathVariable("username") String username,
                                              @PathVariable("tweetId") long tweetId){
        tweetService.deleteTweet(username, tweetId);
        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }
}
