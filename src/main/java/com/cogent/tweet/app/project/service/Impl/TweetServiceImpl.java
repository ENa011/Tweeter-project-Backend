package com.cogent.tweet.app.project.service.Impl;

import com.cogent.tweet.app.project.entity.Register;
import com.cogent.tweet.app.project.entity.Roles;
import com.cogent.tweet.app.project.entity.Tags;
import com.cogent.tweet.app.project.entity.Tweet;
import com.cogent.tweet.app.project.exceptions.MissMatchIdException;
import com.cogent.tweet.app.project.exceptions.ResourceNotFoundException;
import com.cogent.tweet.app.project.repository.RegisterRepository;
import com.cogent.tweet.app.project.repository.TagsRepository;
import com.cogent.tweet.app.project.repository.TweetRepository;
import com.cogent.tweet.app.project.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TweetServiceImpl implements TweetService {

    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    RegisterRepository registerRepository;

    @Autowired
    TagsRepository tagsRepository;

    @Override
    public List<Tweet> getAllTweet() {
        return tweetRepository.findAll();
    }

    @Override
    public List<Tweet> getAllTweetByUsername(String username) {
        Register register = registerRepository.findByUsername(username);
        if(register.getUsername().isEmpty()) {
            throw new ResourceNotFoundException("username", "username", username);
        }
        return tweetRepository.findByRegisterId(register.getId());
    }

    @Override
    public Tweet createTweet(String username, Tweet tweet) {
        Register register = registerRepository.findByUsername(username);
        if(register.getUsername().isEmpty()) {
            throw new ResourceNotFoundException("username", "username", username);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if(username.equals(((UserDetails) principal).getUsername())){
            tweet.setTweetUsername(((UserDetails) principal).getUsername());
            } else throw new MissMatchIdException("this is not your username to create tweet");
        } else throw new MissMatchIdException("authentication info not found");

        Set<Tags> setOfTags = new HashSet<>();

        for(Tags tags: tweet.getTags()){
            if(tagsRepository.existsByTag(tags.getTag())){
                setOfTags.add(tagsRepository.findByTag(tags.getTag()));
            } else setOfTags.add(tags);
        }
        tweet.setTags(setOfTags);
        tweet.setRegister(register);
        tweet.setTimeStamp(new Date());
        return tweetRepository.save(tweet);
    }

    @Override
    public Tweet updateTweet(String username, long tweetId, Tweet tweet) {
        Register register = registerRepository.findByUsername(username);
        if(register.getUsername().isEmpty()) {
            throw new ResourceNotFoundException("username", "username", username);
        }
        Tweet newTweet = tweetRepository.findById(tweetId).orElseThrow(()->
                new ResourceNotFoundException("tweet", "tweet id", tweetId));
        if(!newTweet.getRegister().getUsername().equals(register.getUsername())) {
            throw new MissMatchIdException("tweet does not belong to this user");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if(newTweet.getTweetUsername().equals(((UserDetails) principal).getUsername())){
                newTweet.setContent(tweet.getContent());
                return tweetRepository.save(newTweet);
            } else throw new MissMatchIdException("Not a User's tweet to update, Unauthorized");
        } else throw new MissMatchIdException("authentication info not found");

    }

    @Override
    public void deleteTweet(String username, long tweetId) {
        Register register = registerRepository.findByUsername(username);
        if(register.getUsername().isEmpty()) {
            throw new ResourceNotFoundException("username", "username", username);
        }
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(()->
                new ResourceNotFoundException("tweet", "tweet id", tweetId));
        if(!tweet.getRegister().getUsername().equals(register.getUsername())) {
            throw new MissMatchIdException("tweet does not belong to this user");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if(tweet.getTweetUsername().equals(((UserDetails) principal).getUsername())){
                tweet.setContent(tweet.getContent());
                tweetRepository.deleteById(tweetId);
            } else throw new MissMatchIdException("Not a User's tweet to update, Unauthorized");
        } else throw new MissMatchIdException("authentication info not found");

    }
}
