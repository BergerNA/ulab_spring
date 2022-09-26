package com.edu.ulab.app.storage.dao;

import com.edu.ulab.app.entity.BaseEntity;

import java.io.Serializable;
import java.util.Optional;

public interface DaoEntity<T extends BaseEntity<ID>, ID extends Serializable> {
    Optional<T> findById(ID id);

    <S extends T> S save(S entity);

    <S extends T> S update(S entity);

    void delete(T entity);
}
