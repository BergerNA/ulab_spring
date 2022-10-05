package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.IRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
class UserServiceImplTest {

    @Mock
    IRepository<User, Long> userRepository;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void createUser_user_successful() {
        //given
        final long USER_ID = 1L;

        User user = getUser();

        UserDto userDto = userToUserDto(user);

        User savedUser = getUser();
        savedUser.setId(USER_ID);

        UserDto result = userToUserDto(savedUser);

        //when
        when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.userToUserDto(savedUser)).thenReturn(result);

        //then
        UserDto userDtoResult = userService.createUser(userDto);
        assertNotNull(userDtoResult);
        assertEquals(USER_ID, userDtoResult.getId());
    }

    @Test
    @DisplayName("Получение пользователя по id. Должно пройти успешно.")
    void getUserById_existingBUser_foundUser() {
        final long USER_ID = 1L;
        final User user = getUser();
        user.setId(USER_ID);

        UserDto expectedResult = userToUserDto(user);

        doReturn(Optional.of(user))
                .when(userRepository).findById(USER_ID);

        doReturn(expectedResult).when(userMapper).userToUserDto(user);

        var actualResult = userService.getUserById(USER_ID);

        assertNotNull(actualResult);
        assertEquals(USER_ID, actualResult.getId());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Получение пользователя по несуществующему id. Должно вернуть ошибку отсутствия пользователя.")
    void getUserById_nonExistingUser_throwNotFoundException() {
        final long NON_EXIST_USER_ID = 1L;

        when(userRepository.findById(NON_EXIST_USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUserById(NON_EXIST_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("User with id %d for delete wasn't found.", NON_EXIST_USER_ID));
    }

    @Test
    @DisplayName("Обновить пользователя. Должно пройти успешно.")
    void updateUser_existingUser_successful() {
        final String USER_FULL_NAME_UPDATED_VALUE = "Test updated user full name";
        final long USER_ID = 1L;

        User user = getUser();
        user.setId(USER_ID);
        user.setFullName(USER_FULL_NAME_UPDATED_VALUE);

        UserDto userDto = userToUserDto(user);

        User updatedUser = getUser();
        updatedUser.setId(USER_ID);
        updatedUser.setFullName(USER_FULL_NAME_UPDATED_VALUE);

        UserDto result = userToUserDto(updatedUser);

        when(userMapper.userDtoToUser(userDto)).thenReturn(user);
        when(userRepository.update(user)).thenReturn(updatedUser);
        when(userMapper.userToUserDto(updatedUser)).thenReturn(result);

        UserDto userDtoResult = userService.updateUser(userDto);

        assertNotNull(userDtoResult);
        assertEquals(USER_FULL_NAME_UPDATED_VALUE, userDtoResult.getFullName());
        assertEquals(result, userDtoResult);
    }

    @Test
    @DisplayName("Удалить пользователя по id. Должно пройти успешно.")
    void deleteUserById_existingUser_successful() {
        final long USER_ID = 1L;

        User user = getUser();
        user.setId(USER_ID);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(new User()));
        doNothing().when(userRepository).delete(user);

        assertDoesNotThrow(() -> userService.deleteUserById(USER_ID));
    }

    @Test
    @DisplayName("Удалить пользователя по несуществующему id. Должно вернуть ошибку отсутствия пользователя.")
    void deleteUserById_nonExistingUser_throwNotFoundException() {
        final long NON_EXIST_USER_ID = 1L;

        when(userRepository.findById(NON_EXIST_USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUserById(NON_EXIST_USER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("User with id %d for delete wasn't found.", NON_EXIST_USER_ID));
    }

    private User getUser() {
        return User.builder()
                .age(30)
                .title("Test user title")
                .fullName("Test user fullname")
                .build();
    }

    private UserDto userToUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .age(user.getAge())
                .title(user.getTitle())
                .fullName(user.getFullName())
                .build();

        if (user.getId() != null) {
            userDto.setId(user.getId());
        }
        return userDto;
    }
}