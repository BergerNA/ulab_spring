package com.edu.ulab.app.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;
    private String fullName;
    private String title;
    private int age;
    private List<BookDto> books;
}
