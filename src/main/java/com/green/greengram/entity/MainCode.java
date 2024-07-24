package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MainCode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mainCodeId ;
    @Column(nullable = false, length = 20)
    private String cdName ;
    @Column(length = 30)
    private String description ;
}
