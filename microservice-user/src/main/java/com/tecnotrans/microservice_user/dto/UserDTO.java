package com.tecnotrans.microservice_user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "DTO representing a user")
public class UserDTO {

@Schema(description = "Identifier of a user", example = "1")
private Long userId;

@Schema(description = "Full name of a user", example = "Juan Lopez")
private String name;

@Schema(description = "Phone number of a user", example = "98765432")
private String phoneNumber;

@Schema(description = "Email of a user", example = "email@example.com")
private String email;
}
