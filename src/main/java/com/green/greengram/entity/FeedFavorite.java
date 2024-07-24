package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "feed_favorite",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"feed_id", "user_id"}
                )
        }
)
public class FeedFavorite extends CreatedAt{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedFavoriteId ;
    @ManyToOne @JoinColumn(name = "feed_id", nullable = false)
    private Feed feedId ;
    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User userId ;
}
