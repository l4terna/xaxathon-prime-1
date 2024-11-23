package com.laterna.xaxaxa.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBasicInfoDto {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
