package com.devsuperior.dscatalog.dtos;

import java.io.Serializable;

import com.devsuperior.dscatalog.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

	public CategoryDto(Category entity) {
		this.id = entity.getId();
		this.name = entity.getName();
	}
}
