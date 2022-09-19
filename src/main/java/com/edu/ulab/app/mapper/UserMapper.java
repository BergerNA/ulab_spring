package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.web.request.create.UserRequest;
import com.edu.ulab.app.web.request.update.UserUpdateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userRequestToUserDto(UserRequest userRequest);

    UserRequest userDtoToUserRequest(UserDto userDto);

    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

    UserDto userUpdateRequestToUserDto(UserUpdateRequest userUpdateRequest);
}
