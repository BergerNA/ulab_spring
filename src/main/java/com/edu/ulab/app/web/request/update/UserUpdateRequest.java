package com.edu.ulab.app.web.request.update;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long id;
    private String fullName;
    private String title;
    private int age;
}
