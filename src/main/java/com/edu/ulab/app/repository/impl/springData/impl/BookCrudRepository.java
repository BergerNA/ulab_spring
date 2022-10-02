package com.edu.ulab.app.repository.impl.springData.impl;

import com.edu.ulab.app.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookCrudRepository extends CrudRepository<Book, Long> {
}
