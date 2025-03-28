package com.cogent.tweet.app.project.service.Impl;

import com.cogent.tweet.app.project.entity.Register;
import com.cogent.tweet.app.project.entity.Reply;
import com.cogent.tweet.app.project.entity.Tags;
import com.cogent.tweet.app.project.entity.Tweet;
import com.cogent.tweet.app.project.exceptions.MissMatchIdException;
import com.cogent.tweet.app.project.exceptions.ResourceNotFoundException;
import com.cogent.tweet.app.project.repository.RegisterRepository;
import com.cogent.tweet.app.project.repository.ReplyRepository;
import com.cogent.tweet.app.project.repository.TagsRepository;
import com.cogent.tweet.app.project.repository.TweetRepository;
import com.cogent.tweet.app.project.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    RegisterRepository registerRepository;

    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    TagsRepository tagsRepository;

    @Override
    public Reply createReply(String username, long tweetId, Reply reply) {
        Register register = registerRepository.findByUsername(username);
        if(register.getUsername().isEmpty()) {
            throw new ResourceNotFoundException("username", "username", username);
        }
        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(()->
                new ResourceNotFoundException("tweet", "tweet id", tweetId));
        if(!tweet.getRegister().getUsername().equals(register.getUsername())){
            throw new MissMatchIdException("this tweet is not belong to this User");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            reply.setReplyUsername(((UserDetails) principal).getUsername());
        } else throw new MissMatchIdException("authentication info not found");

        Set<Tags> setOfTags = new HashSet<>();
        for(Tags tags: reply.getTags()){
            if(tagsRepository.existsByTag(tags.getTag())){
                setOfTags.add(tagsRepository.findByTag(tags.getTag()));
            } else setOfTags.add(tags);
        }
        reply.setTags(setOfTags);
        reply.setTimeStamp(new Date());
        reply.setTweet(tweet);
        reply.setLiked(0);
        return replyRepository.save(reply);
    }

    @Override
    public Reply LikeTweet(String username, long replyId) {
        Register register = registerRepository.findByUsername(username);
        if(register.getUsername().isEmpty()) {
            throw new ResourceNotFoundException("username", "username", username);
        }
        Reply reply = replyRepository.findById(replyId).orElseThrow(()->
                new ResourceNotFoundException("reply", "reply id", replyId));
        if(!reply.getTweet().getRegister().getUsername().equals(register.getUsername())){
            throw new MissMatchIdException("this tweet is not belong to this User");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if(!reply.getReplyUsername().equals(((UserDetails) principal).getUsername())) {
                throw new MissMatchIdException("like reply doesn't belong to this user");
            }
        } else throw new MissMatchIdException("authentication info not found");

        if(reply.getLiked() == 0) {
            reply.setLiked(1);
        } else reply.setLiked(0);
        return replyRepository.save(reply);
    }
}
