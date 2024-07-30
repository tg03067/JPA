package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(
        name = "feed_pics"
)
public class FeedPics extends CreatedAt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedPicId ;
    @Column(nullable = false, length = 70)
    private String pic ;
    @ManyToOne @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed ;
}
