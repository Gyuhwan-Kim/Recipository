package com.example.recipository.model.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "tbl_memo")
@Entity
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MySQL의 AUTO_INCREMENT를 사용
    private Long id;
    @Column(length = 200, nullable = false)
    private String memoText;
}