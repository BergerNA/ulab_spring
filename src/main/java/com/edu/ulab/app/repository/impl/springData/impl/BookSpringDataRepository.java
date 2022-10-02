package com.edu.ulab.app.repository.impl.springData.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.repository.impl.springData.SpringDataRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BookSpringDataRepository extends SpringDataRepository<Book, Long> {

    public BookSpringDataRepository(CrudRepository<Book, Long> bookLongCrudRepository) {
        super(bookLongCrudRepository);
    }
}
