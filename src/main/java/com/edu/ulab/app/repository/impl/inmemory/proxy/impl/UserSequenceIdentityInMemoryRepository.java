package com.edu.ulab.app.repository.impl.inmemory.proxy.impl;

import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.repository.impl.inmemory.impl.UserInMemoryRepository;
import com.edu.ulab.app.repository.impl.inmemory.proxy.impl.identity.impl.SequenceLongIdentityProvider;
import com.edu.ulab.app.repository.impl.inmemory.proxy.InMemoryRepositoryPrePersistProxy;
import org.springframework.stereotype.Component;

@Component
public class UserSequenceIdentityInMemoryRepository extends InMemoryRepositoryPrePersistProxy<User, Long> {

    public UserSequenceIdentityInMemoryRepository(UserInMemoryRepository userInMemoryRepository,
                                                  SequenceLongIdentityProvider identityProvider) {
        super(userInMemoryRepository, identityProvider);
    }
}
