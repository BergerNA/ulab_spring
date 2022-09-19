package com.edu.ulab.app.config;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.storage.dao.DaoEntity;
import com.edu.ulab.app.storage.dao.impl.inmemory.InMemoryBookDao;
import com.edu.ulab.app.storage.dao.impl.inmemory.InMemoryUserDao;
import com.edu.ulab.app.storage.dao.proxy.DaoEntityPrePersistIdentityProxy;
import com.edu.ulab.app.storage.identity.IdentityProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public DaoEntity<User, Long> userDao(IdentityProvider<Long> idGenerator) {
        return new DaoEntityPrePersistIdentityProxy<>(idGenerator, new InMemoryUserDao());
    }

    @Bean
    public DaoEntity<Book, Long> bookDao(IdentityProvider<Long> idGenerator) {
        return new DaoEntityPrePersistIdentityProxy<>(idGenerator, new InMemoryBookDao());
    }

}
