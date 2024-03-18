package com.info_hub.controllers.auth;


import com.info_hub.dtos.ResponseMessage;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.user.UserDetailDTO;
import com.info_hub.dtos.responses.user.UserListResponse;
import com.info_hub.services.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse<UserListResponse> getAllUser(@RequestParam(required = false) Map<String, String> params) {
        SimpleResponse<UserListResponse> response = userService.getAllUser(params);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail/{userId}")
    public ResponseEntity<UserDetailDTO> getUserById(@PathVariable("userId") Integer userId) {
        UserDetailDTO response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/update")
    public ResponseEntity<ResponseMessage> updateUserById(@RequestBody UserDetailDTO params,
                                                  @PathVariable Integer userId) {
        ResponseMessage message = userService.updateUserById(params, userId);
        return ResponseEntity.ok(message);
    }
}
