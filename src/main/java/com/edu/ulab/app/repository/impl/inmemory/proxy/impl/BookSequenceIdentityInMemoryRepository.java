package com.edu.ulab.app.repository.impl.inmemory.proxy.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.repository.impl.inmemory.impl.BookInMemoryRepository;
import com.edu.ulab.app.repository.impl.inmemory.proxy.impl.identity.impl.SequenceLongIdentityProvider;
import com.edu.ulab.app.repository.impl.inmemory.proxy.InMemoryRepositoryPrePersistProxy;
import org.springframework.stereotype.Component;

@Component
public class BookSequenceIdentityInMemoryRepository extends InMemoryRepositoryPrePersistProxy<Book, Long> {

    public BookSequenceIdentityInMemoryRepository(BookInMemoryRepository bookInMemoryRepository,
                                                  SequenceLongIdentityProvider identityProvider) {
        super(bookInMemoryRepository, identityProvider);
    }
}
