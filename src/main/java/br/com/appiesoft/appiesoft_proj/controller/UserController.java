package br.com.appiesoft.appiesoft_proj.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.appiesoft.appiesoft_proj.dto.AuthLoginDto;
import br.com.appiesoft.appiesoft_proj.dto.UserDto;
import br.com.appiesoft.appiesoft_proj.model.User;
import br.com.appiesoft.appiesoft_proj.secutiry.JwtUtil;
import br.com.appiesoft.appiesoft_proj.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto) {
        User newUser = userService.register(userDto);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginDto authLoginDto) {
        try {
            User user = userService.authenticate(authLoginDto.getEmail(), authLoginDto.getPassword());
            String token = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok().body(Map.of(
                    "token", token,
                    "user", user.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

}
