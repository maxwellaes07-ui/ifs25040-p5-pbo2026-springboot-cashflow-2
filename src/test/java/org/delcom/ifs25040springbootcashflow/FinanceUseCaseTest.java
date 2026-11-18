package org.delcom.ifs25040springbootcashflow;

import org.delcom.ifs25040springbootcashflow.domain.entity.*;
import org.delcom.ifs25040springbootcashflow.domain.repository.ITransactionRepository;
import org.delcom.ifs25040springbootcashflow.usecase.FinanceUseCase;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class FinanceUseCaseTest {
    @Test void rejectsNonPositiveAmount() {
        ITransactionRepository repository = new ITransactionRepository() {
            public Optional<Transaction> findById(Long id){return Optional.empty();}
            public List<Transaction> findAll(){return List.of();}
            public List<Transaction> findByUserId(long id){return List.of();}
            public Transaction save(Transaction t){return t;}
            public void deleteById(Long id){}
        };
        FinanceUseCase useCase=new FinanceUseCase(repository);
        assertThrows(RuntimeException.class,()->useCase.add(1,"Tes",0,TransactionType.INCOME,null));
    }
}
