package com.green.greengram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass // 부모클래스랑 맵핑 가능하게
@EntityListeners(AuditingEntityListener.class) // auditing 이벤트 바인딩
public class CreatedAt {
    @Column(nullable = false)
    @CreatedDate // JPA 가 insert 때 현재일시 값을 주입 ( default current_timestamp() 속성을 주입한거와 같음 )
    private LocalDateTime createdAt ;
}
