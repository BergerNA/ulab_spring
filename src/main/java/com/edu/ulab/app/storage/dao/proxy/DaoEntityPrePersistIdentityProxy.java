package com.edu.ulab.app.storage.dao.proxy;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.exception.NullPointerException;
import com.edu.ulab.app.storage.dao.DaoEntity;
import com.edu.ulab.app.storage.identity.IdentityProvider;
import com.edu.ulab.app.utils.CommonUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class DaoEntityPrePersistIdentityProxy<T extends BaseEntity<ID>, ID> implements DaoEntity<T, ID> {

    private final IdentityProvider<ID> identityProvider;
    private final DaoEntity<T, ID> entityDao;

    public DaoEntityPrePersistIdentityProxy(IdentityProvider<ID> identityProvider, DaoEntity<T, ID> daoEntity) {
        this.identityProvider = identityProvider;
        this.entityDao = daoEntity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return entityDao.findById(id);
    }

    @Override
    public <S extends T> S save(S entity) {
        CommonUtils.requireNonNull(entity, "Entity to save is null");
        entity.setId(identityProvider.getIdentity());
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
