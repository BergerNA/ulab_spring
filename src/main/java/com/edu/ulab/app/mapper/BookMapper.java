package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.web.request.create.BookRequest;
import com.edu.ulab.app.web.request.update.BookUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto bookRequestToBookDto(BookRequest bookRequest);

    BookRequest bookDtoToBookRequest(BookDto bookDto);

    Book bookDtoToBook(BookDto bookDto);

    BookDto bookToBookDto(Book book);

    BookDto bookUpdateRequestToBookDto(BookUpdateRequest bookUpdateRequest);

    @Mapping(target = "books", ignore = true)
    UserDto userToDto(User user);

    @Mapping(target = "books", ignore = true)
    User dtoToUser(UserDto userDto);
}
