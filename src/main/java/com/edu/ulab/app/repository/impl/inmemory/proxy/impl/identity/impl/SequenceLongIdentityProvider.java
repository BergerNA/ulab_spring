package com.edu.ulab.app.repository.impl.inmemory.proxy.impl.identity.impl;

import com.edu.ulab.app.repository.impl.inmemory.proxy.impl.identity.IdentityProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SequenceLongIdentityProvider implements IdentityProvider<Long> {

    private final AtomicLong sequenceGenerator = new AtomicLong(0);

    @Override
    public Long nextIdentity() {
        return sequenceGenerator.getAndIncrement();
    }
}
