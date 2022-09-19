package com.edu.ulab.app.storage.dao.impl.inmemory;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.utils.CommonUtils;

public class InMemoryUserDao extends InMemoryDaoEntity<User, Long> {

    @Override
    public <S extends User> S update(S entity) {
        CommonUtils.requireNonNull(entity, "Updated user is null");
        CommonUtils.requireNonNull(entity.getId(), "Updated user id is null");
        if (!entities.containsKey(entity.getId())) {
            throw new NotFoundException(String.format("Updated user with id %d isn't found.", entity.getId()));
        }
        entities.computeIfPresent(entity.getId(), (id, oldEntity) -> {
            setIfPresent(entity.getFullName(), oldEntity::setFullName);
            setIfPresent(entity.getTitle(), oldEntity::setTitle);
            setIfPresent(entity.getAge(), oldEntity::setAge);
            return oldEntity;
        });

        return (S) entities.get(entity.getId());
    }
}
