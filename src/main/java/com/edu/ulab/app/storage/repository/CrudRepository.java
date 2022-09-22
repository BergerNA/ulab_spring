package com.edu.ulab.app.storage.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> extends Repository<T, ID> {

    <S extends T> S save(S entity);

    Optional<T> findById(ID id);

    void remove(T entity);

    <S extends T> S update(S entity);
}
