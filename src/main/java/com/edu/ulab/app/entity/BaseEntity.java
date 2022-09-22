package com.edu.ulab.app.entity;

import java.util.Objects;

public abstract class BaseEntity<T> {

    public abstract T getId();

    public abstract void setId(T t);

    @Override
    public final boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;

        if (o instanceof BaseEntity<?> that) {
            return this.getId() != null && Objects.equals(this.getId(), that.getId());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
