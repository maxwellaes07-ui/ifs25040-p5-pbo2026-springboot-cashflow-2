package org.delcom.ifs25040springbootcashflow.adapter.repository;

import org.delcom.ifs25040springbootcashflow.domain.PasswordHasher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SchemaInitializer implements CommandLineRunner {
    private final JdbcTemplate jdbc;
    public SchemaInitializer(JdbcTemplate jdbc) { this.jdbc=jdbc; }
    @Override public void run(String... args) {
        jdbc.execute("create table if not exists users (id bigserial primary key, name varchar(120) not null, email varchar(180) not null unique, password_hash varchar(64) not null, role varchar(10) not null default 'USER', photo_path varchar(500))");
        jdbc.execute("create table if not exists transactions (id bigserial primary key, user_id bigint not null references users(id) on delete cascade, description varchar(250) not null, amount bigint not null check(amount>0), type varchar(10) not null check(type in ('INCOME','EXPENSE')), proof_path varchar(500), created_at timestamp not null default current_timestamp)");
        String email=System.getProperty("ROOT_EMAIL","admin@cashflow.local");
        String password=System.getProperty("ROOT_PASSWORD","admin123");
        Integer count=jdbc.queryForObject("select count(*) from users where lower(email)=lower(?)",Integer.class,email);
        if(count!=null && count==0) jdbc.update("insert into users(name,email,password_hash,role) values(?,?,?,'ADMIN')","Administrator",email,PasswordHasher.hash(password));
    }
}
