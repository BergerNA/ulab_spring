package com.edu.ulab.app.entity;

import lombok.Data;

@Data
public class Book implements BaseEntity<Long>{
    private Long id;
    private Long userId;
    private String title;
    private String author;
    private int pageCount;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
            this.id = id;
    }
}
