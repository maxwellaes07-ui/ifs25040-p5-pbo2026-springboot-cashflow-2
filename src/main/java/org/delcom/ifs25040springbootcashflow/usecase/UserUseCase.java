package org.delcom.ifs25040springbootcashflow.usecase;

import org.delcom.ifs25040springbootcashflow.domain.PasswordHasher;
import org.delcom.ifs25040springbootcashflow.domain.entity.User;
import org.delcom.ifs25040springbootcashflow.domain.exception.EntityNotFoundException;
import org.delcom.ifs25040springbootcashflow.domain.exception.ValidationException;
import org.delcom.ifs25040springbootcashflow.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserUseCase {
    private final IUserRepository users;
    public UserUseCase(IUserRepository users) { this.users=users; }
    public List<User> all() { return users.findAll(); }
    public User get(long id) { return users.findById(id).orElseThrow(()->new EntityNotFoundException("Pengguna tidak ditemukan.")); }
    public User update(long id,String name,String password,String photo) {
        if(name==null||name.isBlank()) throw new ValidationException("Nama tidak boleh kosong.");
        String hash=(password==null||password.isBlank())?null:PasswordHasher.hash(password);
        return users.updateProfile(id,name.trim(),hash,photo);
    }
}
