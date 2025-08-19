package com.devsuperior.dscatalog.dtos;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@Size(min = 4, max = 60, message = "O nome do produto deve ter entre 4 e 60 caracteres")
	@NotBlank(message = "Campo obrigatório")
	private String name;
	
	@NotBlank(message = "Campo obrigatório")
	@Size(max = 155, message = "A descrição deve ter no máximo 155 caracteres")
	private String description;
	
	@Positive(message = "O preço do produto deve ser um valor positivo")
	private Double price;
	private String imgUrl;
	
	@PastOrPresent(message = "Data inválida")
	private Instant date;
	
	private List<CategoryDto> categories = new ArrayList<>();
	
	public ProductDto(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	
	public ProductDto(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
		this.date = entity.getDate();
	}
	
	public ProductDto(Product entity, Set<Category> categories) {
		this(entity);
		categories.forEach(cat -> this.categories.add(new CategoryDto(cat)));
	}
}
