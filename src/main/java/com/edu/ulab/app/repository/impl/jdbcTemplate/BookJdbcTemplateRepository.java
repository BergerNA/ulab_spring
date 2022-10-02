package com.edu.ulab.app.repository.impl.jdbcTemplate;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.repository.IRepository;
import com.edu.ulab.app.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Repository
public class BookJdbcTemplateRepository implements IRepository<Book, Long> {

    private final JdbcTemplate jdbcTemplate;
    private static final BeanPropertyRowMapper<Book> bookRowMapper = new BeanPropertyRowMapper<>(Book.class);

    @Override
    public Optional<Book> findById(Long id) {
        CommonUtils.requireNonNull(id, "Searching user id is null.");
        final String BOOK_SELECT_SQL = "SELECT TITLE, AUTHOR, PAGE_COUNT, USER_ID, ID FROM BOOK WHERE ID=?";

        Book book = jdbcTemplate.query(BOOK_SELECT_SQL, bookRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Book with id %s is not found", id)));

        log.info("Found book: {}", book);
        return Optional.ofNullable(book);
    }

    @Override
    public <S extends Book> S save(S book) {
        CommonUtils.requireNonNull(book, "Created book is null");
        final String BOOK_INSERT_SQL = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    var ps = con.prepareStatement(BOOK_INSERT_SQL, new String[]{"id"});
                    ps.setString(1, book.getTitle());
                    ps.setString(2, book.getAuthor());
                    ps.setLong(3, book.getPageCount());
                    ps.setLong(4, book.getUser().getId());
                    return ps;
                },
                keyHolder);

        book.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Created book with id {}", book.getId());
        return book;
    }

    @Override
    public <S extends Book> S update(S book) {
        CommonUtils.requireNonNull(book, "Updated book is null");
        CommonUtils.requireNonNull(book.getId(), "Updated book id is null");
        final String BOOK_UPDATE_SQL = "UPDATE BOOK SET TITLE=?, AUTHOR=?, PAGE_COUNT=? WHERE ID=?";

        int updateCount = jdbcTemplate.update(BOOK_UPDATE_SQL,
                book.getTitle(),
                book.getAuthor(),
                book.getPageCount(),
                book.getId());

        if (updateCount > 0) {
            log.info("Updated book with id {}", book.getId());
            return book;
        } else {
            throw new NotFoundException(String.format("Updated book with id %s is not found.", book.getId()));
        }
    }

    @Override
    public void delete(Book book) {
        CommonUtils.requireNonNull(book, "Deleted book is null");
        CommonUtils.requireNonNull(book.getId(), "Deleted book id is null");
        final String BOOK_DELETE_SQL = "DELETE FROM BOOK WHERE ID=?";

        int deleteCount = jdbcTemplate.update(BOOK_DELETE_SQL, book.getId());

        if (deleteCount > 0) {
            log.info("Delete book with id {}.", book.getId());
        } else {
            throw new NotFoundException(String.format("Deleted book with id %s is not found.", book.getId()));
        }
    }
}
