package com.devsuperior.dscatalog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EmailDto {

    @Email(message = "E-mail inválido")
    @NotBlank(message = "Campo obrigatório")
    private String email;

    public EmailDto(String email) {
        this.email = email;
    }
}
