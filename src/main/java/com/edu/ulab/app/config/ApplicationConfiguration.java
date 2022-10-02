package com.edu.ulab.app.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

//    @Bean
//    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
//    public IdentityProvider<Long> serialLongIdentityProvider() {
//        return new AtomicLong(0)::getAndIncrement;
//    }
//
//    @Bean
//    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
//    public Consumer<BaseEntity<Long>> entityIdentityEngine(IdentityProvider<Long> identityProvider) {
//        return entity -> entity.setId(identityProvider.getIdentity());
//    }
//
//    @Bean
//    InMemoryDaoEntity<Book, Long> inMemoryBookEntity(){
//        return new InMemoryBookDao();
//    }
//
//    @Bean
//    public DaoEntity<User, Long> inMemoryUserDao(Consumer<BaseEntity<Long>> entityIdentityEngine) {
//        return new DaoEntityPrePersistIdentityEntityProxy<>(
//                new InMemoryUserDao(inMemoryBookEntity()),
//                entityIdentityEngine);
//    }
//
//    @Bean
//    public DaoEntity<Book, Long> inMemoryBookDao(Consumer<BaseEntity<Long>> entityIdentityEngine) {
//        return new DaoEntityPrePersistIdentityEntityProxy<>(inMemoryBookEntity(), entityIdentityEngine);
//    }
//
//    @Bean
//    public DaoEntity<User, Long> springDataUserDao(DaoEntity<User,Long> repository){
//        return new SpringDataDaoEntity<>(repository);
//    }
//
//    @Bean
//    public DaoEntity<Book, Long> springDataBookDao(CrudRepository<Book, Long> repository){
//        return new SpringDataDaoEntity<>(repository);
//    }
//
//    @Bean
//    public CrudRepositoryBaseEntity<User, Long> userRepository(@Qualifier("jdbcTemplateUserDao") DaoEntity<User, Long> userDao) {
//        return new CrudRepositoryBaseEntity<>(userDao);
//    }
//
//    @Bean
//    public CrudRepositoryBaseEntity<Book, Long> bookRepository(@Qualifier("jdbcTemplateBookDao") DaoEntity<Book, Long> bookDao) {
//        return new CrudRepositoryBaseEntity<>(bookDao);
//    }
}
