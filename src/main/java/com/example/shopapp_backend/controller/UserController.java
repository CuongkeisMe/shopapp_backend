package com.example.shopapp_backend.controller;

import com.example.shopapp_backend.response.LoginResponse;
import com.example.shopapp_backend.response.RegisterResponse;
import com.example.shopapp_backend.service.UserService;
import com.example.shopapp_backend.component.LocalizationUtil;
import com.example.shopapp_backend.util.MessageKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.shopapp_backend.dto.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LocalizationUtil localizationUtil;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPass().equals(userDTO.getRetypePass())){
                return ResponseEntity.badRequest().body(
                        RegisterResponse.builder()
                                .message(localizationUtil.getLocalizedMessage(MessageKey.PASSWORD_NOT_MATCH))
                        .build()
                );
            }
            userService.createUser(userDTO);
            return ResponseEntity.ok(
                    RegisterResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(MessageKey.REGISTER_SUCCESSFULLY))
                            .build()
            );
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPass(), userLoginDTO.getRoleId());
            return ResponseEntity.ok(
                    LoginResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(MessageKey.LOGIN_SUCCESSFULLY))
                            .token(token)
                            .build()
            );
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(MessageKey.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }
    }
}
