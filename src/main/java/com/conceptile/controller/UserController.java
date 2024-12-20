package com.conceptile.controller;

import com.conceptile.dto.request.RegisterUserRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.dto.response.UserDTO;
import com.conceptile.exception.ErrorDetailResponse;
import com.conceptile.exception.FlowChartMgmtException;
import com.conceptile.exception.NoDataFoundException;
import com.conceptile.service.implLayer.UserServiceImpl;
import com.conceptile.service.innterfaceLayer.UserService;
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

import java.util.List;


@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register user",
            description = "Endpoint Registers an user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful registration",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @PostMapping("/v1/register")
    public ResponseEntity<UserDTO> registerUserHandler(@Valid @RequestBody RegisterUserRequest request) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userService.registerUser(request));
        } catch (FlowChartMgmtException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
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
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.findUserUsingEmail(email));
        } catch (NoDataFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
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
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.findUserUsingId(id));
        } catch (NoDataFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Delete user data using its id",
            description = "Delete user and its all flowchart and associated nodes, connections (edges) using userId",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @DeleteMapping("/v1/deleteById")
    public ResponseEntity<UserDTO> deleterUserAndCorrespondingFlowchartDataHandler(@RequestParam Long id) {
        try {
            userService.deleterUserAndCorrespondingFlowchartData(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw e;
        }
    }

    @Operation(
            summary = "Get all Flowcharts with Nodes (only) using user ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Flowcharts found for user",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @GetMapping("/v1/getAllFlowcharts")
    public ResponseEntity<List<FlowchartDTO>> getAllFlowchartsForUserHandler(@RequestParam Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.getAllFlowchartsForUser(id));
        } catch (NoDataFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }


}
