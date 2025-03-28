package com.cogent.tweet.app.project.repository;

import com.cogent.tweet.app.project.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
