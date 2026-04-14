package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoleDto {

    private Long id;
    private String authority;

    public RoleDto(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDto(Role entity) {
        id = entity.getId();
        authority = entity.getAuthority();
    }
}
