package com.example.demo.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String refreshToken;
    private String role;
    private UserDTO user;

    @Data
    public static class UserDTO {
        private Integer id;
        private String email;
        private String role;
        private String name;
    }
}
