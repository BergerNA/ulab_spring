package com.edu.ulab.app.config;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.storage.dao.DaoEntity;
import com.edu.ulab.app.storage.dao.impl.inmemory.InMemoryBookDao;
import com.edu.ulab.app.storage.dao.impl.inmemory.InMemoryDaoEntity;
import com.edu.ulab.app.storage.dao.impl.inmemory.InMemoryUserDao;
import com.edu.ulab.app.storage.dao.impl.springData.SpringDataDaoEntity;
import com.edu.ulab.app.storage.dao.proxy.DaoEntityPrePersistIdentityEntityProxy;
import com.edu.ulab.app.storage.identity.IdentityProvider;
import com.edu.ulab.app.storage.repository.impl.CrudRepositoryBaseEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;

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
    InMemoryDaoEntity<Book, Long> inMemoryBookEntity(){
        return new InMemoryBookDao();
    }

    @Bean
    public DaoEntity<User, Long> inMemoryUserDao(Consumer<BaseEntity<Long>> entityIdentityEngine) {
        return new DaoEntityPrePersistIdentityEntityProxy<>(
                new InMemoryUserDao(inMemoryBookEntity()),
                entityIdentityEngine);
    }

    @Bean
    public DaoEntity<Book, Long> inMemoryBookDao(Consumer<BaseEntity<Long>> entityIdentityEngine) {
        return new DaoEntityPrePersistIdentityEntityProxy<>(inMemoryBookEntity(), entityIdentityEngine);
    }

    @Bean
    public DaoEntity<User, Long> springDataUserDao(CrudRepository<User,Long> repository){
        return new SpringDataDaoEntity<>(repository);
    }

    @Bean
    public DaoEntity<Book, Long> springDataBookDao(CrudRepository<Book, Long> repository){
        return new SpringDataDaoEntity<>(repository);
    }

    @Bean
    public CrudRepositoryBaseEntity<User, Long> userRepository(@Qualifier("jdbcTemplateUserDao") DaoEntity<User, Long> userDao) {
        return new CrudRepositoryBaseEntity<>(userDao);
    }

    @Bean
    public CrudRepositoryBaseEntity<Book, Long> bookRepository(@Qualifier("jdbcTemplateBookDao") DaoEntity<Book, Long> bookDao) {
        return new CrudRepositoryBaseEntity<>(bookDao);
    }
}
