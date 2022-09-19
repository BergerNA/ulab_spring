package com.edu.ulab.app.storage.identity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SerialLongIdentityProvider implements IdentityProvider<Long> {

    private long counter = 0;

    @Override
    public Long getIdentity() {
        return counter++;
    }
}
