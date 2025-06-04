package com.tecnotrans.microservice_user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

private Long userId;
private String name;
private String phoneNumber;
private String email;

}
