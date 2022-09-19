package com.edu.ulab.app.storage.dao.impl.inmemory;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.storage.dao.DaoEntity;
import com.edu.ulab.app.utils.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class InMemoryDaoEntity<T extends BaseEntity<ID>, ID> implements DaoEntity<T, ID> {

    HashMap<ID, T> entities = new HashMap<>();

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
    public void delete(ID id) {
        CommonUtils.requireNonNull(id, "Deleted id is null.");
        entities.remove(id);
    }

    @Override
    public List<T> getByCriteria(Predicate<T> predicate) {
        CommonUtils.requireNonNull(predicate, "Searching criteria is null");
        return entities.values().stream().filter(predicate).toList();
    }

    <T> void setIfPresent(T obj, Consumer<T> consumer) {
        if (obj != null) {
            consumer.accept(obj);
        }
    }
}
