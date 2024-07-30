package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "feed_comment"
)
public class FeedComment extends UpdatedAt{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedCommentId ;
    @ManyToOne @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;
    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(length = 1000, nullable = false)
    private String comment ;
}
