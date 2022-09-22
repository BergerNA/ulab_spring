package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.repository.CrudRepository;
import com.edu.ulab.app.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final CrudRepository<User, Long> userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(final CrudRepository<User, Long> userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(final UserDto userDto) {
        CommonUtils.requireNonNull(userDto, "Request to create user doesn't contain the user meteData");
        User createdUser = userRepository.save(userMapper.userDtoToUser(userDto));
        log.info("Created user: {}", createdUser);
        return userMapper.userToUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(final UserDto userDto) {
        CommonUtils.requireNonNull(userDto, "Request to update user doesn't contain the user meteData");
        User updatedUser = userRepository.update(userMapper.userDtoToUser(userDto));
        log.info("Updated user: {}", updatedUser);
        return userMapper.userToUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(final Long userId) {
        CommonUtils.requireNonNull(userId, "Request to get user doesn't contain the user id");
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(foundUser -> log.info("Found user by id {}: {}", userId, foundUser));
        return userMapper.userToUserDto(user.orElseThrow(
                () -> new NotFoundException(String.format("User with id %d wasn't found.", userId))));
    }

    @Override
    public void deleteUserById(final Long userId) {
        CommonUtils.requireNonNull(userId, "Request to delete user doesn't contain the user id");
        Optional<User> deletedUser = userRepository.findById(userId);
        deletedUser.ifPresent(user -> log.info("User to delete by id {}: {}", userId, user));
        userRepository.remove(deletedUser.orElseThrow(
                () -> new NotFoundException(String.format("User with id %d for delete wasn't found.", userId))));
    }
}
