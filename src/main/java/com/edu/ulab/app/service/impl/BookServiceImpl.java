package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.repository.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final CrudRepository<Book, Long> bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(CrudRepository<Book, Long> bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        bookRepository.save(book);
        return bookMapper.bookToBookDto(book);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        bookRepository.update(bookMapper.bookDtoToBook(bookDto));
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long bookId) {
        return bookMapper.bookToBookDto(bookRepository.findById(bookId).orElseThrow(
                () -> new NotFoundException(String.format("User with id %d wasn't found.", bookId))));
    }

    @Override
    public void deleteBookById(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public List<BookDto> getBooksByUserId(Long userId) {
        return bookRepository.getByCriteria(x -> Objects.equals(x.getUserId(), userId))
                .stream()
                .map(bookMapper::bookToBookDto)
                .toList();
    }

    @Override
    public void deleteBooksByUserId(Long userId) {
        getBooksByUserId(userId)
                .stream()
                .map(BookDto::getId)
                .forEach(bookRepository::deleteById);
    }
}
