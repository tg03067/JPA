package com.green.greengram.feed;

import com.green.greengram.entity.FeedPics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedPicsRepository extends JpaRepository<FeedPics, Long> {

}
