package com.conceptile.service;

import com.conceptile.dto.request.RegisterUserRequest;
import com.conceptile.dto.response.UserDTO;
import com.conceptile.entity.User;
import com.conceptile.exception.FlowChartMgmtException;
import com.conceptile.exception.NoDataFoundException;
import com.conceptile.mapper.FlowChartMgmtGlobalMapper;
import com.conceptile.repository.UserRepository;
import com.conceptile.util.EncryptionUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    final UserRepository userRepository;
    final FlowChartMgmtGlobalMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, FlowChartMgmtGlobalMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional
    public UserDTO registerUser(RegisterUserRequest request) {
        String email = request.getEmail().trim();
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new FlowChartMgmtException(String.format("User already registered with provided email: %s", email));
                });
        String encryptedPassword = Optional.ofNullable(request.getPassword())
                .map(password -> {
                    try {
                        return EncryptionUtil.encrypt(password.trim());
                    } catch (Exception e) {
                        log.error("Encryption error: ", e.getMessage());
                        throw new RuntimeException("Failed to encrypt password");
                    }
                })
                .orElseThrow(() -> new RuntimeException("User password found as null"));
        User user = User.builder()
                .email(email)
                .username(request.getUsername().trim())
                .password(encryptedPassword)
                .createdAt(LocalDateTime.now())
                .build();
        return mapper.fromUserEntityToUserDTO(userRepository.save(user));
    }


    public UserDTO findUserUsingEmail(String email) {
        return findUser(userRepository.findByEmail(email), email);
    }

    public UserDTO findUserUsingId(Long userId) {
        return findUser(userRepository.findByUserId(userId), userId.toString());
    }

    private UserDTO findUser(Optional<User> userOptional, String identifier) {
        User foundUser = userOptional
                .orElseThrow(() -> new NoDataFoundException(String.format("User not found with provided identifier: %s", identifier)));
        String decryptedPassword = decryptPassword(foundUser.getPassword());
        foundUser.setPassword(decryptedPassword);
        return mapper.fromUserEntityToUserDTO(foundUser);
    }

    private String decryptPassword(String encryptedPassword) {
        if (encryptedPassword == null) {
            throw new RuntimeException("Decrypted user's password found to be null");
        }
        try {
            return EncryptionUtil.decrypt(encryptedPassword);
        } catch (Exception e) {
            log.error("Decryption error: ", e);
            throw new RuntimeException("Failed to decrypt user password");
        }
    }


}
