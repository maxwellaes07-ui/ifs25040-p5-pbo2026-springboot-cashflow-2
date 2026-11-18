package org.delcom.ifs25040springbootcashflow.adapter.repository;

import org.delcom.ifs25040springbootcashflow.domain.entity.*;
import org.delcom.ifs25040springbootcashflow.domain.repository.IUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Statement;
import java.util.*;

@Repository
public class JdbcUserRepository implements IUserRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper = (rs,n) -> new User(rs.getLong("id"), rs.getString("name"),
        rs.getString("email"), rs.getString("password_hash"), Role.valueOf(rs.getString("role")), rs.getString("photo_path"));
    public JdbcUserRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public Optional<User> findById(Long id) { return jdbc.query("select * from users where id=?", mapper,id).stream().findFirst(); }
    public Optional<User> findByEmail(String email) { return jdbc.query("select * from users where lower(email)=lower(?)",mapper,email).stream().findFirst(); }
    public boolean emailExists(String email) { return Boolean.TRUE.equals(jdbc.queryForObject("select exists(select 1 from users where lower(email)=lower(?))",Boolean.class,email)); }
    public List<User> findAll() { return jdbc.query("select * from users order by id",mapper); }
    public User save(User u) {
        var key = new GeneratedKeyHolder();
        jdbc.update(c -> { var p=c.prepareStatement("insert into users(name,email,password_hash,role,photo_path) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            p.setString(1,u.name()); p.setString(2,u.email()); p.setString(3,u.passwordHash()); p.setString(4,u.role().name()); p.setString(5,u.photoPath()); return p; },key);
        return findById(Objects.requireNonNull(key.getKey()).longValue()).orElseThrow();
    }
    public User updateProfile(long id,String name,String passwordHash,String photoPath) {
        jdbc.update("update users set name=?, password_hash=coalesce(?,password_hash), photo_path=coalesce(?,photo_path) where id=?",name,passwordHash,photoPath,id);
        return findById(id).orElseThrow();
    }
    public void deleteById(Long id) { jdbc.update("delete from users where id=?",id); }
}
