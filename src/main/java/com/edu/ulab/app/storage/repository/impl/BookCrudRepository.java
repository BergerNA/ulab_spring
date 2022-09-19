package com.edu.ulab.app.storage.repository.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.storage.dao.DaoEntity;
import com.edu.ulab.app.storage.repository.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class BookCrudRepository implements CrudRepository<Book, Long> {

    private final DaoEntity<Book, Long> bookDao;

    @Autowired
    public BookCrudRepository(DaoEntity<Book, Long> bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public <S extends Book> S save(S entity) {
        return bookDao.save(entity);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookDao.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        bookDao.delete(id);
    }

    @Override
    public <S extends Book> S update(S entity) {
        return bookDao.update(entity);
    }

    @Override
    public List<Book> getByCriteria(Predicate<Book> predicate) {
        return bookDao.getByCriteria(predicate);
    }
}
