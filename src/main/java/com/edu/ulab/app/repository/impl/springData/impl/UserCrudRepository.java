package com.edu.ulab.app.repository.impl.springData.impl;

import com.edu.ulab.app.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserCrudRepository extends CrudRepository<User, Long> {
}
