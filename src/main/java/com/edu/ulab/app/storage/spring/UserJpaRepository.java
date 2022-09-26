package com.edu.ulab.app.storage.spring;

import com.edu.ulab.app.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


//@Repository
public interface UserJpaRepository extends CrudRepository<User, Long> {

}
