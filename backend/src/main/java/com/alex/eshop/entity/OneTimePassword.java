package com.alex.eshop.entity;

import com.alex.eshop.constants.OneTimePasswordType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class OneTimePassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private String value;
    @Enumerated(EnumType.STRING)
    private OneTimePasswordType type;
    private ZonedDateTime expireAt;
}