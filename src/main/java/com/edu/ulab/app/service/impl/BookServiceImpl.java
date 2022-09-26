package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.repository.CrudRepository;
import com.edu.ulab.app.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final CrudRepository<Book, Long> bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(BookDto bookDto) {
        CommonUtils.requireNonNull(bookDto, "Request to create book doesn't contain the book mete data");
        Book createdBook = bookRepository.save(bookMapper.bookDtoToBook(bookDto));
        log.info("Created book: {}", createdBook);
        return bookMapper.bookToBookDto(createdBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        CommonUtils.requireNonNull(bookDto, "Request to update book doesn't contain the book mete data");
        Book updatedBook = bookRepository.update(bookMapper.bookDtoToBook(bookDto));
        log.info("Updated book: {}", updatedBook);
        return bookMapper.bookToBookDto(updatedBook);
    }

    @Override
    public BookDto getBookById(Long bookId) {
        CommonUtils.requireNonNull(bookId, "Request to get book doesn't contain the book id");
        Optional<Book> foundBook = bookRepository.findById(bookId);
        foundBook.ifPresent(book -> log.info("Found book by id {}: {}", bookId, book));
        return bookMapper.bookToBookDto(foundBook.orElseThrow(
                () -> new NotFoundException(String.format("Book with id %d wasn't found.", bookId))));
    }

    @Override
    public void deleteBookById(Long bookId) {
        CommonUtils.requireNonNull(bookId, "Request to delete book doesn't contain the book id");
        log.info("Delete book by book id{}.", bookId);
        Optional<Book> deletedBook = bookRepository.findById(bookId);
        deletedBook.ifPresent(book -> log.info("User to delete by id {}: {}", bookId, book));
        bookRepository.remove(deletedBook.orElseThrow(
                () -> new NotFoundException(String.format("Book with id %d for delete wasn't found.", bookId))));
    }
}
