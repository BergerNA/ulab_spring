package com.edu.ulab.app.entity;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> {

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
