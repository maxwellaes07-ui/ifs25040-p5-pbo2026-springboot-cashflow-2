package org.delcom.ifs25040springbootcashflow.domain.entity;

import java.time.LocalDateTime;

public record Transaction(long id, long userId, String description, long amount,
                          TransactionType type, String proofPath, LocalDateTime createdAt) {}
