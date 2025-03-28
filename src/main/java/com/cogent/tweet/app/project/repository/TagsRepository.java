package com.cogent.tweet.app.project.repository;

import com.cogent.tweet.app.project.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<Tags, Long > {
    Boolean existsByTag(String tag);
    Tags findByTag(String tag);
}
