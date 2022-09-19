package com.edu.ulab.app.web.request.update;

import lombok.Data;

@Data
public class BookUpdateRequest {
    private Long id;
    private String title;
    private String author;
    private long pageCount;
}
