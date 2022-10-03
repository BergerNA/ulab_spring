package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
class BookServiceImplTest {

    @Mock
    IRepository<Book, Long> bookRepository;
    @Mock
    BookMapper bookMapper;
    @InjectMocks
    BookServiceImpl bookService;

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void createBook_book_successful() {
        //given
        final long USER_ID = 1L;
        final long BOOK_ID = 1L;

        User user = getUser(USER_ID);

        Book book = getBook();
        book.setUser(user);

        BookDto bookDto = bookToBookDto(book);

        Book savedBook = getBook();
        savedBook.setId(BOOK_ID);
        savedBook.setUser(user);

        BookDto result = bookToBookDto(savedBook);

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertNotNull(bookDtoResult);
        assertEquals(BOOK_ID, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Получить книгу по id. Должно пройти успешно.")
    void getBookById_existingBook_foundBook() {
        final long BOOK_ID = 1L;
        final Book book = getBook();
        book.setId(BOOK_ID);

        BookDto expectedResult = bookToBookDto(book);

        doReturn(Optional.of(book))
                .when(bookRepository).findById(BOOK_ID);

        doReturn(expectedResult).when(bookMapper).bookToBookDto(book);

        var actualResult = bookService.getBookById(1L);

        assertNotNull(actualResult);
        assertEquals(BOOK_ID, actualResult.getId());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Получить книгу по несуществуещему id. Должно вернуть ошибку отсутствия книги.")
    void getBookById_nonExistingBook_throwNotFoundException() {
        final long NON_EXIST_BOOK_ID = 1L;

        when(bookRepository.findById(NON_EXIST_BOOK_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.deleteBookById(NON_EXIST_BOOK_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Book with id %d for delete wasn't found.", NON_EXIST_BOOK_ID));
    }

    @Test
    @DisplayName("Обновить книгу. Должно пройти успешно.")
    void updateBook_existingBook_successful() {
        final String BOOK_AUTHOR_UPDATED_VALUE = "Test updated author book";
        final long BOOK_ID = 1L;
        final long USER_ID = 1L;

        User user = getUser(USER_ID);

        Book book = getBook();
        book.setAuthor(BOOK_AUTHOR_UPDATED_VALUE);
        book.setId(BOOK_ID);
        book.setUser(user);

        BookDto bookDto = bookToBookDto(book);

        Book updatedBook = getBook();
        updatedBook.setAuthor(BOOK_AUTHOR_UPDATED_VALUE);
        updatedBook.setId(BOOK_ID);
        updatedBook.setUser(user);

        BookDto result = bookToBookDto(updatedBook);


        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.update(book)).thenReturn(updatedBook);
        when(bookMapper.bookToBookDto(any())).thenReturn(result);

        BookDto bookDtoResult = bookService.updateBook(bookDto);

        assertNotNull(bookDtoResult);
        assertEquals(BOOK_AUTHOR_UPDATED_VALUE, bookDtoResult.getAuthor());
        assertEquals(result, bookDtoResult);
    }

    @Test
    @DisplayName("Удалить книгу по id. Должно пройти успешно.")
    void deleteBookById_existingBook_successful() {
        final long BOOK_ID = 1L;

        Book book = getBook();
        book.setId(BOOK_ID);

        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        doNothing().when(bookRepository).delete(book);

        assertDoesNotThrow(() -> bookService.deleteBookById(BOOK_ID));
    }

    @Test
    @DisplayName("Удалить книгу по id. Должно вернуть ошибку отсутствия книги.")
    void deleteBookById_nonExistingBook_throwNotFoundException() {
        final long NON_EXIST_BOOK_ID = 1L;

        when(bookRepository.findById(NON_EXIST_BOOK_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.deleteBookById(NON_EXIST_BOOK_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("Book with id %d for delete wasn't found.", NON_EXIST_BOOK_ID));
    }

    private Book getBook() {
        return Book.builder()
                .author("Test author")
                .pageCount(1000)
                .title("Test title")
                .build();
    }

    private User getUser(long userId) {
        return User.builder()
                .id(userId)
                .age(30)
                .title("Test user title")
                .fullName("Test user fullname")
                .build();
    }

    private BookDto bookToBookDto(Book book) {
        BookDto bookDto = BookDto.builder()
                .id(book.getId())
                .author(book.getAuthor())
                .pageCount(book.getPageCount())
                .title(book.getTitle())
                .build();
        if (book.getUser() != null) {
            bookDto.setUser(userToUserDto(book.getUser()));
        }
        return bookDto;
    }

    private UserDto userToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .age(user.getAge())
                .title(user.getTitle())
                .fullName(user.getFullName())
                .build();
    }
}