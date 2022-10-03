package com.edu.ulab.app.repository.impl.springData.impl;

import com.edu.ulab.app.config.SystemJpaTest;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты репозитория {@link UserSpringDataRepository}.
 */
@SystemJpaTest
class UserSpringDataRepositoryTest {

    @Autowired
    UserSpringDataRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить пользователя. Должно пройти успешно.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given
        User person = new User();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        //When
        User result = userRepository.save(person);

        //Then
        assertEquals(111, result.getAge());
        assertEquals("Test Test", result.getFullName());
        assertEquals("reader", result.getTitle());
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получить пользователя по id. Должно пройти успешно.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getUserById_thenAssertDmlCount() {
        //Given
        final Long USER_ID = 1001L;

        //When
        Optional<User> result = userRepository.findById(USER_ID);

        //Then
        assertTrue(result.isPresent());
        User user = result.get();
        assertSelectCount(1);
        assertEquals(USER_ID, user.getId());
        assertEquals(55, user.getAge());
        assertEquals("default user", user.getFullName());
        assertEquals("reader", user.getTitle());
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить пользователя. Должно пройти успешно.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql"
    })
    void updateUserById_thenAssertDmlCount() {
        //Given
        final long USER_ID = 1001L;
        final int UPDATED_USER_AGE = 30;
        final String UPDATED_USER_TITLE = "updated title";
        final String UPDATED_USER_FULL_NAME = "updated full name";

        Optional<User> user = userRepository.findById(USER_ID);

        assertTrue(user.isPresent());

        User userToUpdate = user.get();

        userToUpdate.setAge(UPDATED_USER_AGE);
        userToUpdate.setTitle(UPDATED_USER_TITLE);
        userToUpdate.setFullName(UPDATED_USER_FULL_NAME);

        //When
        User updatedUser = userRepository.save(userToUpdate);

        //Then
        assertSelectCount(1);
        assertEquals(USER_ID, updatedUser.getId());
        assertEquals(UPDATED_USER_AGE, updatedUser.getAge());
        assertEquals(UPDATED_USER_TITLE, updatedUser.getTitle());
        assertEquals(UPDATED_USER_FULL_NAME, updatedUser.getFullName());
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить пользователя по id. Должно пройти успешно.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteUserById_thenAssertDmlCount() {
        //Given
        final Long USER_ID = 1001L;

        Optional<User> userToDelete = userRepository.findById(USER_ID);

        assertTrue(userToDelete.isPresent());
        assertEquals(USER_ID, userToDelete.get().getId());

        //When
        userRepository.delete(userToDelete.get());

        Optional<User> deletedUser = userRepository.findById(USER_ID);

        //Then
        assertTrue(deletedUser.isEmpty());
        assertSelectCount(3);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Сохранить не консистентного пользователя. Должно вернуть ошибку нарушения целостности данных.")
    @Test
    @Rollback
    @Sql("classpath:sql/1_clear_schema.sql")
    void saveInvalidUser_thenAssertDmlCount() {
        //Given
        User user = new User();
        user.setFullName(null);
        user.setTitle("User title");
        user.setAge(20);

        //When
        ThrowableAssert.ThrowingCallable saveOperation = () -> userRepository.save(user);

        //Then
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(saveOperation);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
}