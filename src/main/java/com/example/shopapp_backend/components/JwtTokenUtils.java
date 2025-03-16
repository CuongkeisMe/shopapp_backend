package com.example.shopapp_backend.components;

import com.example.shopapp_backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private int expiration;

//    public String generateToken(User user) {
//        Map<String, Object> claims = new HashMap<>();
//    }
}
