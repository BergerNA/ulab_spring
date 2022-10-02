package com.edu.ulab.app.repository.impl.springData;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.repository.IRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.Optional;

@AllArgsConstructor
public abstract class SpringDataRepository<T extends BaseEntity<ID>, ID extends Serializable> implements IRepository<T, ID> {

    private CrudRepository<T, ID> repository;

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public <S extends T> S save(S entity) {
        return repository.save(entity);
    }

    @Override
    public <S extends T> S update(S entity) {
        if (repository.existsById(entity.getId())) {
            return repository.save(entity);
        }
        throw new NotFoundException(String.format("User to update with id %s is not found.", entity.getId()));
    }

    @Override
    public void delete(T entity) {
        if (repository.existsById(entity.getId())) {
            repository.delete(entity);
        } else {
            throw new NotFoundException(String.format("User to delete with id %s is not found.", entity.getId()));
        }
    }
}
