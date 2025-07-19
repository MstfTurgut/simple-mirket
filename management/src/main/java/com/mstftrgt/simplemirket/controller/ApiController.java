package com.mstftrgt.simplemirket.controller;

import com.mstftrgt.simplemirket.domain.User;
import com.mstftrgt.simplemirket.dto.ApiDto;
import com.mstftrgt.simplemirket.service.ApiService;
import com.mstftrgt.simplemirket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apis")
@RequiredArgsConstructor
public class ApiController {
    private final ApiService apiService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiDto> createApi(@RequestBody ApiDto dto, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        return ResponseEntity.ok(apiService.createApi(dto, user));
    }

    @GetMapping
    public ResponseEntity<List<ApiDto>> getUserApis(Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        return ResponseEntity.ok(apiService.getUserApis(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDto> getApi(@PathVariable Long id, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        return ResponseEntity.ok(apiService.getApi(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDto> updateApi(@PathVariable Long id, @RequestBody ApiDto dto, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        return ResponseEntity.ok(apiService.updateApi(id, dto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApi(@PathVariable Long id, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        apiService.deleteApi(id, user);
        return ResponseEntity.noContent().build();
    }
}