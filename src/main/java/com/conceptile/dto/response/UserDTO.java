package com.conceptile.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserDTO {
    Long userId;
    String username;
    String password;
    String email;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
