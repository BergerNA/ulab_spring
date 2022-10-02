package com.edu.ulab.app.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@ToString
@Builder
@Entity(name = "person")
public class User extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "age",
            nullable = false)
    private int age;

    @Column(name = "title",
            nullable = false)
    private String title;

    @Column(name = "full_name",
            nullable = false)
    private String fullName;

    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();
}

