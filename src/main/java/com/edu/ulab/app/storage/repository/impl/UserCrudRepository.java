package com.edu.ulab.app.storage.repository.impl;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.storage.dao.DaoEntity;
import com.edu.ulab.app.storage.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class UserCrudRepository implements CrudRepository<User, Long> {

    private final DaoEntity<User, Long> userDao;

    public UserCrudRepository(DaoEntity<User, Long> userDao) {
        this.userDao = userDao;
    }

    @Override
    public <S extends User> S save(S entity) {
        return userDao.save(entity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        userDao.delete(id);
    }

    @Override
    public <S extends User> S update(S entity) {
        return userDao.update(entity);
    }

    @Override
    public List<User> getByCriteria(Predicate<User> predicate) {
        return userDao.getByCriteria(predicate);
    }
}
