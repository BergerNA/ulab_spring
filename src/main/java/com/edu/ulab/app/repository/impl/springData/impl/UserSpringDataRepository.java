package com.edu.ulab.app.repository.impl.springData.impl;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.repository.impl.springData.SpringDataRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserSpringDataRepository extends SpringDataRepository<User, Long> {

    public UserSpringDataRepository(CrudRepository<User, Long> userCrudRepository) {
        super(userCrudRepository);
    }
}
