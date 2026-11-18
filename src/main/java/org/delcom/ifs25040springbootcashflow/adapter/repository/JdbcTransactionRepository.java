package org.delcom.ifs25040springbootcashflow.adapter.repository;

import org.delcom.ifs25040springbootcashflow.domain.entity.*;
import org.delcom.ifs25040springbootcashflow.domain.repository.ITransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class JdbcTransactionRepository implements ITransactionRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<Transaction> mapper = (rs,n) -> new Transaction(rs.getLong("id"),rs.getLong("user_id"),
        rs.getString("description"),rs.getLong("amount"),TransactionType.valueOf(rs.getString("type")),
        rs.getString("proof_path"),rs.getTimestamp("created_at").toLocalDateTime());
    public JdbcTransactionRepository(JdbcTemplate jdbc) { this.jdbc=jdbc; }
    public Optional<Transaction> findById(Long id) { return jdbc.query("select * from transactions where id=?",mapper,id).stream().findFirst(); }
    public List<Transaction> findAll() { return jdbc.query("select * from transactions order by created_at desc",mapper); }
    public List<Transaction> findByUserId(long id) { return jdbc.query("select * from transactions where user_id=? order by created_at desc",mapper,id); }
    public Transaction save(Transaction t) {
        var key=new GeneratedKeyHolder();
        jdbc.update(c->{var p=c.prepareStatement("insert into transactions(user_id,description,amount,type,proof_path) values(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            p.setLong(1,t.userId());p.setString(2,t.description());p.setLong(3,t.amount());p.setString(4,t.type().name());p.setString(5,t.proofPath());return p;},key);
        return findById(Objects.requireNonNull(key.getKey()).longValue()).orElseThrow();
    }
    public void deleteById(Long id) { jdbc.update("delete from transactions where id=?",id); }
}
