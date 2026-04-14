package com.devsuperior.dscatalog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserInsertDto extends UserDto {

    private String password;

}
