package org.delcom.ifs25040springbootcashflow.domain.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    void deleteById(ID id);
}
