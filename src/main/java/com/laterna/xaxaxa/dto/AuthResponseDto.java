package com.laterna.xaxaxa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String lastName;
    private String firstName;
}
