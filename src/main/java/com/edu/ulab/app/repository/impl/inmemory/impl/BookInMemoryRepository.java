package com.edu.ulab.app.repository.impl.inmemory.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.repository.impl.inmemory.InMemoryRepository;
import com.edu.ulab.app.utils.CommonUtils;
import org.springframework.stereotype.Component;

@Component
public class BookInMemoryRepository extends InMemoryRepository<Book, Long> {

    @Override
    public <S extends Book> S update(S entity) {
        CommonUtils.requireNonNull(entity, "Updated book is null");
        CommonUtils.requireNonNull(entity.getId(), "Updated book id is null");
        if (!entities.containsKey(entity.getId())) {
            throw new NotFoundException(String.format("Updated book with id %d isn't found.", entity.getId()));
        }
        entities.computeIfPresent(entity.getId(), (id, oldEntity) -> {
            setIfPresent(entity.getAuthor(), oldEntity::setAuthor);
            setIfPresent(entity.getTitle(), oldEntity::setTitle);
            setIfPresent(entity.getPageCount(), oldEntity::setPageCount);
            return oldEntity;
        });

        return (S) entities.get(entity.getId());
    }
}
