package com.conceptile.controller;

import com.conceptile.dto.request.RegisterUserRequest;
import com.conceptile.dto.response.UserDTO;
import com.conceptile.exception.ErrorDetailResponse;
import com.conceptile.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register user",
            description = "Endpoint Registers an user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful registration",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @PostMapping("/v1/register")
    public ResponseEntity<UserDTO> registerUserHandler(@Valid @RequestBody RegisterUserRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerUser(request));
    }

    @Operation(
            summary = "Find user by email",
            description = "Find user details using its email address",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @GetMapping("/v1/findByEmail")
    public ResponseEntity<UserDTO> findUserUsingEmailHandler(@RequestParam String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findUserUsingEmail(email));
    }

    @Operation(
            summary = "Find user by userId",
            description = "Find user details using its user id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @GetMapping("/v1/findByUserId")
    public ResponseEntity<UserDTO> findUserUsingIdHandler(@RequestParam Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findUserUsingId(id));
    }


}