package com.edu.ulab.app.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@ToString
public class User extends BaseEntity<Long> {
    private Long id;
    private int age;
    private String title;
    private String fullName;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}

