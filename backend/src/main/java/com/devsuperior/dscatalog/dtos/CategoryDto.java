package com.devsuperior.dscatalog.dtos;

import java.io.Serializable;

import com.devsuperior.dscatalog.entities.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank(message = "Campo obrigatório")
	@Size(min = 4, max = 60, message = "O nome deve ter entre 4 e 60 caracteres")
	private String name;

	public CategoryDto(Category entity) {
		this.id = entity.getId();
		this.name = entity.getName();
	}
}
