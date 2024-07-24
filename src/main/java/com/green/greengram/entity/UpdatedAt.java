package com.green.greengram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass // 부모클래스랑 맵핑 가능하게 ( 상속으로 사용가능하게 )
@EntityListeners(AuditingEntityListener.class) // auditing 이벤트 바인딩
public class UpdatedAt extends CreatedAt {
    @LastModifiedDate // JPA 가 insert, update 때 현재일시 값을 주입.
    @Column(nullable = false)
    private LocalDateTime updatedAt ;
}
