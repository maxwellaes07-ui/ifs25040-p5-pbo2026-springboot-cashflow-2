package org.delcom.ifs25040springbootcashflow.usecase;

import org.delcom.ifs25040springbootcashflow.domain.PasswordHasher;
import org.delcom.ifs25040springbootcashflow.domain.entity.*;
import org.delcom.ifs25040springbootcashflow.domain.exception.ValidationException;
import org.delcom.ifs25040springbootcashflow.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthUseCase {
    private final IUserRepository users;
    public AuthUseCase(IUserRepository users) { this.users=users; }
    public User register(String name,String email,String password) {
        if(name==null||name.isBlank()||email==null||!email.contains("@")||password==null||password.length()<6)
            throw new ValidationException("Nama, email valid, dan password minimal 6 karakter wajib diisi.");
        if(users.emailExists(email.trim())) throw new ValidationException("Email sudah terdaftar.");
        return users.save(new User(0,name.trim(),email.trim().toLowerCase(),PasswordHasher.hash(password),Role.USER,null));
    }
    public User login(String email,String password) {
        User u=users.findByEmail(email==null?"":email.trim()).orElseThrow(()->new ValidationException("Email atau password salah."));
        if(!PasswordHasher.matches(password==null?"":password,u.passwordHash())) throw new ValidationException("Email atau password salah.");
        return u;
    }
}
