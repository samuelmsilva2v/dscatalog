package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.Category;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private Long id;
    private String name;

    public CategoryDto(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
