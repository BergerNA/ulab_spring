package com.edu.ulab.app.repository.impl.springData.impl;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertDeleteCount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты репозитория {@link BookSpringDataRepository}.
 */
@SystemJpaTest
class BookSpringDataRepositoryTest {

    @Autowired
    BookSpringDataRepository bookRepository;
    @Autowired
    UserSpringDataRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу и автора.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void findAllBadges_thenAssertDmlCount() {
        //Given
        User user = new User();
        user.setAge(111);
        user.setTitle("reader");
        user.setFullName("Test Test");

        User savedUser = userRepository.save(user);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setUser(savedUser);

        //When
        Book result = bookRepository.save(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получить книгу.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBook_thenAssertDmlCount() {
        //Given
        final long BOOK_ID = 3003L;

        //When
        Optional<Book> result = bookRepository.findById(BOOK_ID);

        //Then
        assertTrue(result.isPresent());
        assertEquals(6655, result.get().getPageCount());
        assertEquals("on more author", result.get().getAuthor());
        assertEquals("more default book", result.get().getTitle());
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить книгу.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
        //Given
        final String UPDATED_BOOK_AUTHOR = "updated author";
        final String UPDATED_BOOK_TITLE = "updated title";
        final int UPDATED_BOOK_PAGE_COUNT = 222;

        User user = new User();
        user.setAge(111);
        user.setTitle("reader");
        user.setFullName("Test Test");

        User savedUser = userRepository.save(user);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setUser(savedUser);

        //When
        Book savedBook = bookRepository.save(book);
        savedBook.setTitle(UPDATED_BOOK_TITLE);
        savedBook.setAuthor(UPDATED_BOOK_AUTHOR);
        savedBook.setPageCount(UPDATED_BOOK_PAGE_COUNT);

        Book updatedBook = bookRepository.save(savedBook);

        //Then
        assertEquals(UPDATED_BOOK_TITLE, updatedBook.getTitle());
        assertEquals(UPDATED_BOOK_TITLE, updatedBook.getTitle());
        assertEquals(UPDATED_BOOK_AUTHOR, updatedBook.getAuthor());
        assertEquals(UPDATED_BOOK_PAGE_COUNT, updatedBook.getPageCount());
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удаление книги по id.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBookById_thenAssertDmlCount() {
        //Given
        final long BOOK_ID_TO_DELETE = 2002L;

        Optional<Book> bookToDelete = bookRepository.findById(BOOK_ID_TO_DELETE);

        assertTrue(bookToDelete.isPresent());

        //When
        bookRepository.delete(bookToDelete.get());

        Optional<Book> deletedBook = bookRepository.findById(BOOK_ID_TO_DELETE);

        //Then
        assertTrue(deletedBook.isEmpty());

        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Сохранить не консистентную книгу пользователя. Должно вернуть ошибку нарушения целостности данных.")
    @Test
    @Rollback
    @Sql("classpath:sql/1_clear_schema.sql")
    void saveInvalidUserBook_thenAssertDmlCount() {
        //Given
        User user = new User();
        user.setFullName("User full name");
        user.setTitle("User title");
        user.setAge(20);

        User savedUser = userRepository.save(user);

        Book book = new Book();
        book.setAuthor(null);
        book.setTitle("test");
        book.setPageCount(1000);
        book.setUser(savedUser);

        //When
        ThrowableAssert.ThrowingCallable saveOperation = () -> bookRepository.save(book);

        //Then
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(saveOperation);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
}