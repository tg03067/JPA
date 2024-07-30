package com.green.greengram.entity;

import com.green.greengram.security.SignInProviderType;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@ToString
@Entity // 이 클래스는 entity ( 테이블 ) 이다.
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"provider_type", "uid"}
                )
        }
)
public class User extends UpdatedAt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId ;

    @Column(nullable = false) @ColumnDefault("4") // enum 타입
    private SignInProviderType providerType ;
    @Column(length = 50, nullable = false) // 문자열 길이, not null
    private String uid ;

    @Column(length = 100, nullable = false)
    private String upw ;
    @Column(length = 50, nullable = false)
    private String nm ;
    @Column(length = 200, nullable = true)
    private String pic ;
}
