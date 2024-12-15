package com.conceptile.service.implLayer;

import com.conceptile.dto.request.RegisterUserRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.dto.response.UserDTO;
import com.conceptile.entity.Flowchart;
import com.conceptile.entity.User;
import com.conceptile.exception.FlowChartMgmtException;
import com.conceptile.exception.NoDataFoundException;
import com.conceptile.mapper.FlowChartMgmtGlobalMapper;
import com.conceptile.repository.FlowchartRepository;
import com.conceptile.repository.UserRepository;
import com.conceptile.service.innterfaceLayer.UserService;
import com.conceptile.util.EncryptionUtil;
import com.conceptile.util.GenericUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final FlowchartRepository flowchartRepository;
    final FlowChartMgmtGlobalMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, FlowchartRepository flowchartRepository, FlowChartMgmtGlobalMapper mapper) {
        this.userRepository = userRepository;
        this.flowchartRepository = flowchartRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
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

    @Override
    public UserDTO findUserUsingEmail(String email) throws NoDataFoundException {
        GenericUtil.ensureNotNull(email, "Email not provided");
        return findUser(userRepository.findByEmail(email), email);
    }

    @Override
    public UserDTO findUserUsingId(Long userId) throws NoDataFoundException {
        GenericUtil.ensureNotNull(userId, "User id not provided");
        return findUser(userRepository.findByUserId(userId), userId.toString());
    }

    @Transactional
    @Override
    public void deleterUserAndCorrespondingFlowchartData(Long userId) {
        GenericUtil.ensureNotNull(userId, "User not provided");
        userRepository.deleteByUserId(userId);
    }

    @Override
    public List<FlowchartDTO> getAllFlowchartsForUser(Long userId) throws NoDataFoundException {
        GenericUtil.ensureNotNull(userId, "Please provide userId");
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NoDataFoundException("No user found with provided IU: " + userId));
        List<Flowchart> flowcharts = flowchartRepository.findAllByUser(user);
        return mapper.fromFlowchartEntitiesToFlowchartDTOs(flowcharts);
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
