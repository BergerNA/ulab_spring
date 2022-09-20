package com.edu.ulab.app.storage.dao;

import com.edu.ulab.app.entity.BaseEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface DaoEntity<T extends BaseEntity<ID>, ID> {
    Optional<T> findById(ID id);

    <S extends T> S save(S entity);

    <S extends T> S update(S entity);

    void delete(ID id);

    List<T> getByCriteria(Predicate<T> predicate);
}
