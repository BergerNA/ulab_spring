package com.edu.ulab.app.storage.repository.impl;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.storage.dao.DaoEntity;
import com.edu.ulab.app.storage.repository.CrudRepository;

import java.io.Serializable;
import java.util.Optional;

public class CrudRepositoryBaseEntity<T extends BaseEntity<ID>, ID extends Serializable> implements CrudRepository<T, ID> {
    private final DaoEntity<T, ID> daoEntity;

    public CrudRepositoryBaseEntity(final DaoEntity<T, ID> daoEntity) {
        this.daoEntity = daoEntity;
    }

    @Override
    public <S extends T> S save(S entity) {
        return daoEntity.save(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return daoEntity.findById(id);
    }

    @Override
    public void remove(T entity) {
        daoEntity.delete(entity);
    }

    @Override
    public <S extends T> S update(S entity) {
        return daoEntity.update(entity);
    }
}
