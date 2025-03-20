package com.example.shopapp_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("phone_number")
    @NotNull(message = "Phone number is required")
    private String phoneNumber;
    private String address;
    @NotBlank(message = "Password can not be blank")
    @JsonProperty("pass")
    private String pass;
    @JsonProperty("retype_password")
    private String retypePass;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("facebook_account_id")
    private int facebookAccountId;
    @JsonProperty("google_account_id")
    private int googleAccountId;
    @NotNull(message = "Role id is required")
    @JsonProperty("role_id")
    private Long roleId;
}

