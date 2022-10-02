package com.edu.ulab.app.repository.impl.inmemory.proxy.impl.identity;

public interface IdentityProvider<T> {
    T nextIdentity();
}
