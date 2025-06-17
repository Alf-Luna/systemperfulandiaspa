package com.tecnotrans.microservice_user.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "USER")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long userId;

@Column(name = "NAME", length = 40, nullable = false)
private String name;

@Column(name = "PHONE_NUMBER", nullable = false)
private String phoneNumber;

@Column(name = "EMAIL", nullable = false, unique = true)
private String email;
}
