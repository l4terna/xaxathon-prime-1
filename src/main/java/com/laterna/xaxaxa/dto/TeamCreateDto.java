package com.laterna.xaxaxa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamCreateDto {
    
    @NotBlank(message = "Название команды обязательно")
    @Size(max = 255, message = "Название команды не должно превышать 255 символов")
    private String name;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;

    @NotNull(message = "Идентификатор спортивного мероприятия обязателен")
    private Long sportEventId;
}
