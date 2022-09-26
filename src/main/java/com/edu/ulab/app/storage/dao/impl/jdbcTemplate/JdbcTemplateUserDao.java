package com.edu.ulab.app.storage.dao.impl.jdbcTemplate;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.storage.dao.DaoEntity;
import com.edu.ulab.app.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Repository
public class JdbcTemplateUserDao implements DaoEntity<User, Long> {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcTemplateBookDao jdbcTemplateDaoBook;
    private static final BeanPropertyRowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);
    private static final BeanPropertyRowMapper<Book> bookRowMapper = new BeanPropertyRowMapper<>(Book.class);

    @Override
    public Optional<User> findById(Long id) {
        CommonUtils.requireNonNull(id, "Searching user id is null.");
        final String USER_SELECT_SQL = "SELECT ID, FULL_NAME, TITLE, AGE FROM PERSON WHERE ID=?";

        Optional<User> foundUser = Optional.of(jdbcTemplate.query(USER_SELECT_SQL, userRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundException(String.format("User with id %s is not found.", id))));
        foundUser.ifPresent(user -> log.info("Found user: {}", user));

        List<Book> userBooks = userBooks(id);
        userBooks.forEach(book -> log.info("Found related book ids: {}.", book));
        foundUser.ifPresent(user -> user.setBooks(userBooks));

        return foundUser;
    }

    @Override
    public <S extends User> S save(S user) {
        final String USER_INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    var ps = connection.prepareStatement(USER_INSERT_SQL, new String[]{"id"});
                    ps.setString(1, user.getFullName());
                    ps.setString(2, user.getTitle());
                    ps.setLong(3, user.getAge());
                    return ps;
                },
                keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Created user with id {}.", user.getId());
        return user;
    }

    @Override
    public <S extends User> S update(S user) {
        final String USER_UPDATE_SQL = "UPDATE PERSON SET FULL_NAME=?, TITLE=?, AGE=? WHERE ID=?";

        int updateCount = jdbcTemplate.update(USER_UPDATE_SQL,
                user.getFullName(),
                user.getTitle(),
                user.getAge(),
                user.getId());

        if (updateCount > 0) {
            log.info("Updated user with id {}.", user.getId());
            return user;
        } else {
            throw new NotFoundException(String.format("Updated user with id %s is not found.", user.getId()));
        }
    }

    @Override
    public void delete(User user) {
        final String USER_DELETE_SQL = "DELETE FROM PERSON WHERE ID=?";
        Objects.requireNonNull(user);

        final List<Book> userBook = userBooks(user.getId());

        userBook.forEach(jdbcTemplateDaoBook::delete);


        int updateCount = jdbcTemplate.update(USER_DELETE_SQL, user.getId());
        if (updateCount > 0) {
            log.info("Delete user with id {}.", user.getId());
        } else {
            throw new NotFoundException(String.format("Deleted user with id %s is not found.", user.getId()));
        }
    }

    private List<Book> userBooks(Long userId) {
        final String SELECT_BOOK_BY_USER_ID = "SELECT ID, TITLE, AUTHOR, PAGE_COUNT FROM BOOK WHERE USER_ID=?";
        return jdbcTemplate.query(SELECT_BOOK_BY_USER_ID, bookRowMapper, userId);
    }
}
