package com.green.greengram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(
        name = "sub_code" ,
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"main_code_id", "val"}
                )
        }
)
public class SubCode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCodeId ;

    @ManyToOne @JoinColumn(name = "main_code_id", nullable = false)
    private MainCode mainCodeId ;

    @Column(nullable = false, length = 30)
    private String val ;

    @Column(length = 30)
    private String description ;
}
