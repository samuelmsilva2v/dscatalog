package com.devsuperior.dscatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewPasswordDto {

    @NotBlank(message = "Campo obrigatório")
    private String token;

    @NotBlank(message = "Campo obrigatório")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String password;

    public NewPasswordDto(String token, String password) {
        this.token = token;
        this.password = password;
    }
}
