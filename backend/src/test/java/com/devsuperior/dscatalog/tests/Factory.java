package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dtos.ProductDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		
		var product = new Product(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(createCategory());
		
		return product;
	}
	
	public static ProductDto createProductDto() {
		
		var product = createProduct();
		
		return new ProductDto(product, product.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(1L, "Electronics", null, null);
	}
}
