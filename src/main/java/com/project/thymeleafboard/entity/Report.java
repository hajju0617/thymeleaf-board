package com.project.thymeleafboard.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

//@Entity
//@Getter
//public class Report {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(columnDefinition = "TEXT")
//    private String content;
//
//    private LocalDateTime reportDate;
//
//    @ManyToOne
//    private SiteUser reporter;
//
//    @ManyToOne
//    private Board targetBoard;
//
//    @ManyToOne
//    private Comment targetComment;
//
//    @Column(columnDefinition = "integer default 1")
//    private int status;
//}
