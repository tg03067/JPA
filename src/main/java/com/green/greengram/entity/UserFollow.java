package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"from_user_id", "to_user_id"}
                )
        }
)
public class UserFollow extends CreatedAt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userFollowId ;

    @ManyToOne // many 가 해당객체 ( UserFollow ) To One ( User )
    @JoinColumn(name = "from_user_id") // name = 컬럼 명
    private User fromUser ;
    @ManyToOne @JoinColumn(name = "to_user_id")
    private User toUser ;
}
