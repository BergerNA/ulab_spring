package com.edu.ulab.app.repository.impl.inmemory.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.repository.impl.inmemory.InMemoryRepository;
import com.edu.ulab.app.utils.CommonUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInMemoryRepository extends InMemoryRepository<User, Long> {

    private final InMemoryRepository<Book, Long> bookRepository;

    public UserInMemoryRepository(InMemoryRepository<Book, Long> bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        CommonUtils.requireNonNull(id, "Searching user id is null.");
        Optional<User> foundUser = Optional.ofNullable(entities.get(id));
        foundUser.ifPresent(user -> user.setBooks(
                bookRepository.getAll()
                        .stream()
                        .filter(book -> book.getUser().getId().equals(id))
                        .toList()));
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public <S extends User> S update(S entity) {
        CommonUtils.requireNonNull(entity, "Updated user is null");
        CommonUtils.requireNonNull(entity.getId(), "Updated user id is null");
        if (!entities.containsKey(entity.getId())) {
            throw new NotFoundException(String.format("Updated user with id %d isn't found.", entity.getId()));
        }
        entities.computeIfPresent(entity.getId(), (id, oldEntity) -> {
            setIfPresent(entity.getFullName(), oldEntity::setFullName);
            setIfPresent(entity.getTitle(), oldEntity::setTitle);
            setIfPresent(entity.getAge(), oldEntity::setAge);
            return oldEntity;
        });

        return (S) entities.get(entity.getId());
    }

    @Override
    public void delete(User userToDelete) {
        CommonUtils.requireNonNull(userToDelete, "Deleted user is null");
        CommonUtils.requireNonNull(userToDelete.getId(), "Deleted user id is null");

        Optional<User> deletedUser = Optional.ofNullable(entities.get(userToDelete.getId()));
        deletedUser.ifPresent(user -> user.getBooks().forEach(bookRepository::delete));
        super.delete(userToDelete);
    }
}
