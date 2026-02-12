package com.example.apirestful.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequestDTO {
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 200, message = "O título deve ter entre 3 e 200 caracteres")
    private String title;

    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    private String description;
}

