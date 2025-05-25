package com.devsuperior.dscatalog.dtos;

import java.io.Serializable;

import com.devsuperior.dscatalog.entities.Category;

import lombok.Data;

@Data
public class CategoryDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

	public CategoryDto(Category entity) {
		this.id = entity.getId();
		this.name = entity.getName();
	}
}
