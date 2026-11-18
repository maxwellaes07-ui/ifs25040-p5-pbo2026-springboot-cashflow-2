package org.delcom.ifs25040springbootcashflow.domain.entity;

import java.util.Comparator;

public enum SortOption {
    AMOUNT_ASC("Jumlah: rendah ke tinggi", Comparator.comparingLong(Transaction::amount)),
    AMOUNT_DESC("Jumlah: tinggi ke rendah", Comparator.comparingLong(Transaction::amount).reversed()),
    INCOME_FIRST("Pemasukan dahulu", Comparator.comparing(t -> t.type() == TransactionType.INCOME ? 0 : 1)),
    EXPENSE_FIRST("Pengeluaran dahulu", Comparator.comparing(t -> t.type() == TransactionType.EXPENSE ? 0 : 1));

    private final String label;
    private final Comparator<Transaction> comparator;
    SortOption(String label, Comparator<Transaction> comparator) { this.label = label; this.comparator = comparator; }
    public String label() { return label; }
    public Comparator<Transaction> comparator() { return comparator.thenComparing(Transaction::createdAt, Comparator.reverseOrder()); }
}
