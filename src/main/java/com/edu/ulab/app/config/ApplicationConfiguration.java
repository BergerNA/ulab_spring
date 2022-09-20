package com.edu.ulab.app.config;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.storage.dao.DaoEntity;
import com.edu.ulab.app.storage.dao.impl.inmemory.InMemoryBookDao;
import com.edu.ulab.app.storage.dao.impl.inmemory.InMemoryUserDao;
import com.edu.ulab.app.storage.dao.proxy.DaoEntityPrePersistIdentityEntityProxy;
import com.edu.ulab.app.storage.identity.IdentityProvider;
import com.edu.ulab.app.storage.repository.CrudRepository;
import com.edu.ulab.app.storage.repository.impl.CrudRepositoryBaseEntity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Configuration
public class ApplicationConfiguration {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public IdentityProvider<Long> serialLongIdentityProvider() {
        return new AtomicLong(0)::getAndIncrement;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Consumer<BaseEntity<Long>> entityIdentityEngine(IdentityProvider<Long> identityProvider) {
        return entity -> entity.setId(identityProvider.getIdentity());
    }

    @Bean
    public DaoEntity<User, Long> userDao(Consumer<BaseEntity<Long>> entityIdentityEngine) {
        return new DaoEntityPrePersistIdentityEntityProxy<>(new InMemoryUserDao(), entityIdentityEngine);
    }

    @Bean
    public CrudRepository<User, Long> userRepository(DaoEntity<User, Long> userDao) {
        return new CrudRepositoryBaseEntity<>(userDao);
    }

    @Bean
    public DaoEntity<Book, Long> bookDao(Consumer<BaseEntity<Long>> entityIdentityEngine) {
        return new DaoEntityPrePersistIdentityEntityProxy<>(new InMemoryBookDao(), entityIdentityEngine);
    }

    @Bean
    public CrudRepository<Book, Long> bookRepository(DaoEntity<Book, Long> bookDao) {
        return new CrudRepositoryBaseEntity<>(bookDao);
    }

}
