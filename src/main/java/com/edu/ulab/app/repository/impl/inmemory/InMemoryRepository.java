package com.edu.ulab.app.repository.impl.inmemory;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.repository.IRepository;
import com.edu.ulab.app.utils.CommonUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class InMemoryRepository<T extends BaseEntity<ID>, ID extends Serializable> implements IRepository<T, ID> {

    protected HashMap<ID, T> entities = new HashMap<>();

    @Override
    public Optional<T> findById(ID id) {
        CommonUtils.requireNonNull(id, "Id the searching entity is null.");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public <S extends T> S save(S entity) {
        CommonUtils.requireNonNull(entity, "Saved entity is null.");
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(T entity) {
        CommonUtils.requireNonNull(entity, "Deleted entity is null.");
        CommonUtils.requireNonNull(entity.getId(), "Deleted id is null.");
        entities.remove(entity.getId());
    }

    public Collection<T> getAll() {
        return entities.values();
    }

    protected <T> void setIfPresent(T obj, Consumer<T> consumer) {
        if (obj != null) {
            consumer.accept(obj);
        }
    }
}
