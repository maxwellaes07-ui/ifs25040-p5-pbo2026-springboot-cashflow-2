package org.delcom.ifs25040springbootcashflow.domain.repository;

import org.delcom.ifs25040springbootcashflow.domain.entity.Transaction;
import java.util.List;

public interface ITransactionRepository extends IRepository<Transaction, Long> {
    List<Transaction> findByUserId(long userId);
}
