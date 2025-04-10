package com.example.shopapp_backend.service;

import com.example.shopapp_backend.dto.UserDTO;
import com.example.shopapp_backend.exception.DataNotFoundException;
import com.example.shopapp_backend.model.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password, Long roleId) throws DataNotFoundException;
}
