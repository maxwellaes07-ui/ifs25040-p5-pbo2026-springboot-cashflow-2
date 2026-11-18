package org.delcom.ifs25040springbootcashflow.domain.repository;

import org.delcom.ifs25040springbootcashflow.domain.entity.User;
import java.util.Optional;

public interface IUserRepository extends IRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean emailExists(String email);
    User updateProfile(long id, String name, String passwordHash, String photoPath);
}
