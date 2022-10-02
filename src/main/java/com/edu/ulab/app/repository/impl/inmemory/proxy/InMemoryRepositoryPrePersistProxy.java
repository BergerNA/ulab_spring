package com.edu.ulab.app.repository.impl.inmemory.proxy;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.repository.IRepository;
import com.edu.ulab.app.repository.impl.inmemory.InMemoryRepository;
import com.edu.ulab.app.repository.impl.inmemory.proxy.impl.identity.IdentityProvider;
import com.edu.ulab.app.utils.CommonUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Component
@AllArgsConstructor
public abstract class InMemoryRepositoryPrePersistProxy<T extends BaseEntity<ID>, ID extends Serializable> implements IRepository<T, ID> {

    private final InMemoryRepository<T, ID> repository;

    private final IdentityProvider<ID> identityProvider;

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public <S extends T> S save(S entity) {
        CommonUtils.requireNonNull(entity, "Entity to save is null");
        entity.setId(identityProvider.nextIdentity());
        return repository.save(entity);
    }

    @Override
    public <S extends T> S update(S entity) {
        return repository.update(entity);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }
}
