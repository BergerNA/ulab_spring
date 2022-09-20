package com.edu.ulab.app.storage.dao.proxy;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.storage.dao.DaoEntity;
import com.edu.ulab.app.utils.CommonUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DaoEntityPrePersistIdentityEntityProxy<T extends BaseEntity<ID>, ID> implements DaoEntity<T, ID> {

    private final DaoEntity<T, ID> entityDao;

    private final Consumer<BaseEntity<ID>> entityIdentity;

    public DaoEntityPrePersistIdentityEntityProxy(DaoEntity<T, ID> daoEntity,
                                                  Consumer<BaseEntity<ID>> entityIdentity) {
        this.entityDao = daoEntity;
        this.entityIdentity = entityIdentity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return entityDao.findById(id);
    }

    @Override
    public <S extends T> S save(S entity) {
        CommonUtils.requireNonNull(entity, "Entity to save is null");
        entityIdentity.accept(entity);
        return entityDao.save(entity);
    }

    @Override
    public <S extends T> S update(S entity) {
        return entityDao.update(entity);
    }

    @Override
    public void delete(ID id) {
        entityDao.delete(id);
    }

    @Override
    public List<T> getByCriteria(Predicate<T> predicate) {
        return entityDao.getByCriteria(predicate);
    }
}
