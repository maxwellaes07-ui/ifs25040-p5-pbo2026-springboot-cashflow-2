package org.delcom.ifs25040springbootcashflow.usecase;

import org.delcom.ifs25040springbootcashflow.domain.entity.*;
import org.delcom.ifs25040springbootcashflow.domain.exception.*;
import org.delcom.ifs25040springbootcashflow.domain.repository.ITransactionRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FinanceUseCase {
    private final ITransactionRepository transactions;
    public FinanceUseCase(ITransactionRepository transactions) { this.transactions=transactions; }
    public Transaction add(long userId,String description,long amount,TransactionType type,String proofPath) {
        if(userId<=0) throw new ValidationException("ID pengguna tidak valid.");
        if(description==null||description.isBlank()) throw new ValidationException("Keterangan wajib diisi.");
        if(amount<=0) throw new ValidationException("Jumlah harus lebih besar dari nol.");
        if(type==null) throw new ValidationException("Jenis transaksi wajib dipilih.");
        return transactions.save(new Transaction(0,userId,description.trim(),amount,type,proofPath,java.time.LocalDateTime.now()));
    }
    public List<Transaction> list(long userId,String keyword,SortOption sort) {
        String q=keyword==null?"":keyword.toLowerCase();
        return transactions.findByUserId(userId).stream().filter(t->t.description().toLowerCase().contains(q))
            .sorted(sort==null?SortOption.AMOUNT_DESC.comparator():sort.comparator()).toList();
    }
    public List<Transaction> listAll() { return transactions.findAll(); }
    public void delete(long transactionId,long ownerId,boolean admin) {
        Transaction t=transactions.findById(transactionId).orElseThrow(()->new EntityNotFoundException("Transaksi tidak ditemukan."));
        if(t.userId()!=ownerId && !admin) throw new ValidationException("Anda tidak berhak menghapus transaksi ini.");
        transactions.deleteById(transactionId);
    }
    public long income(long userId) { return transactions.findByUserId(userId).stream().filter(t->t.type()==TransactionType.INCOME).mapToLong(Transaction::amount).sum(); }
    public long expense(long userId) { return transactions.findByUserId(userId).stream().filter(t->t.type()==TransactionType.EXPENSE).mapToLong(Transaction::amount).sum(); }
}
