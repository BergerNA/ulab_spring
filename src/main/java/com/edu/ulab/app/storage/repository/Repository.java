package com.edu.ulab.app.storage.repository;

import java.util.List;
import java.util.function.Predicate;

public interface Repository<T, ID> {

    List<T> getByCriteria(Predicate<T> predicate);
}
