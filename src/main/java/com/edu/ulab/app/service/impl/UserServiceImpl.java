package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.repository.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final CrudRepository<User, Long> userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(CrudRepository<User, Long> userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        userRepository.update(userMapper.userDtoToUser(userDto));
        return userDto;
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userMapper.userToUserDto(userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User with id %d wasn't found.", userId))));
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
