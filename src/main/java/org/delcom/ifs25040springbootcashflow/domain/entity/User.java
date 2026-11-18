package org.delcom.ifs25040springbootcashflow.domain.entity;

public record User(long id, String name, String email, String passwordHash, Role role, String photoPath) {}
