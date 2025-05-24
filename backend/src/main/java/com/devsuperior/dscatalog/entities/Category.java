package com.devsuperior.dscatalog.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

	public Category(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
}
