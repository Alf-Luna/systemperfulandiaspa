package com.tecnotrans.microservice_user.Model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Represents a user entity in the database.")
public class User {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Schema(description = "Identifier of a user", example = "1")
private Long userId;

@Column(name = "NAME", length = 40, nullable = false)
@Schema(description = "Full name of a user", example = "Juan Lopez")
private String name;

@Column(name = "PHONE_NUMBER", nullable = false)
@Schema(description = "Phone number of a user", example = "98765432")
private String phoneNumber;

@Column(name = "EMAIL", nullable = false, unique = true)
@Schema(description = "Email of a user", example = "email@example.com")
private String email;
}
