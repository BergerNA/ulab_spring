package com.edu.ulab.app.storage.spring;

import com.edu.ulab.app.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


//@Repository
public interface BookJpaRepository extends CrudRepository<Book, Long> {

}
