package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.BadRequestException;
import com.edu.ulab.app.exception.NullPointerException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.utils.CommonUtils;
import com.edu.ulab.app.web.request.create.UserBookRequest;
import com.edu.ulab.app.web.request.update.UserBookUpdateRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        CommonUtils.requireNonNull(userBookRequest, "Create request is null.");
        CommonUtils.requireNonNull(userBookRequest.getUserRequest(), "The request for creating a user is null.");

        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);
        List<Long> bookIdList = Optional.ofNullable(userBookRequest.getBookRequests())
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();

        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(UserBookUpdateRequest userBookUpdateRequest) {
        if (userBookUpdateRequest == null) {
            throw new NullPointerException("The update request is null.");
        }
        if (userBookUpdateRequest.getUserUpdateRequest().getId() == null) {
            throw new BadRequestException("Update request hasn't user identity.");
        }

        List<Long> updatedBooks = Optional.ofNullable(userBookUpdateRequest.getBookUpdateRequests())
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .peek(updatedBook -> CommonUtils.requireNonNull(updatedBook.getId(), "Update request hasn't book identity."))
                .map(bookMapper::bookUpdateRequestToBookDto)
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::updateBook)
                .peek(updatedBook -> log.info("Update book: {}", updatedBook))
                .map(BookDto::getId)
                .toList();

        UserDto userDto = userService.updateUser(
                userMapper.userUpdateRequestToUserDto(userBookUpdateRequest.getUserUpdateRequest()));
        log.info("Updated user: {}", userDto);

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(updatedBooks)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        CommonUtils.requireNonNull(userId, "UserId is null.");
        UserDto userDto = userService.getUserById(userId);
        log.info("User by id {}: {}", userId, userDto);
        List<Long> userBooksDto = bookService.getBooksByUserId(userId).stream()
                .map(BookDto::getId)
                .toList();
        log.info("User books: {}", userBooksDto);

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(userBooksDto)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        CommonUtils.requireNonNull(userId, "UserId is null.");
        bookService.deleteBooksByUserId(userId);
        userService.deleteUserById(userId);
    }
}
