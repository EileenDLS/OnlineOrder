package com.fullstack.onlineorder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("customers")
public record CustomerEntity(
        @Id Long id,   // @Id means it's auto fill and specify it is PK
        String email,
        String password,
        boolean enabled,
        String firstName,
        String lastName
) {
}
