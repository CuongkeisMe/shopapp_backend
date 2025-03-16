package com.example.shopapp_backend.service;

import com.example.shopapp_backend.dto.UserDTO;
import com.example.shopapp_backend.exception.DataNotFoundException;
import com.example.shopapp_backend.model.Role;
import com.example.shopapp_backend.model.User;
import com.example.shopapp_backend.repository.RoleRepository;
import com.example.shopapp_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        // Kiem tra xem sdt da ton tai hay chua
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        // convert tu dto -> object
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(phoneNumber)
                .pass(userDTO.getPass())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAcountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);
        // Kiem tra neu co accountId -> Kh yeu cau nhap pass
        if(userDTO.getGoogleAccountId() == 0 && userDTO.getFacebookAccountId() == 0) {
            String password = userDTO.getPass();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPass(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public User login(String phoneNumber, String password) throws DataNotFoundException {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isPresent()) {
            throw new DataNotFoundException("Invalid phone number or password");
        }
        return optionalUser.get();
    }
}
