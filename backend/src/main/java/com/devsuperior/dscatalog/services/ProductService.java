package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dtos.CategoryDto;
import com.devsuperior.dscatalog.dtos.ProductDto;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDto> findAllPaged(Pageable pageable) {

		var list = productRepository.findAll(pageable);
		
		return list.map(x -> new ProductDto(x));
	}

	@Transactional(readOnly = true)
	public ProductDto findById(Long id) {

		Optional<Product> obj = productRepository.findById(id);
		var entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

		return new ProductDto(entity, entity.getCategories());
	}

	@Transactional(readOnly = true)
	public ProductDto insert(ProductDto dto) {

		var entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = productRepository.save(entity);

		return new ProductDto(entity);
	}

	@Transactional
	public ProductDto update(Long id, ProductDto dto) {

		try {
			var entity = productRepository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = productRepository.save(entity);
			return new ProductDto(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id " + id + " not found");
		}
	}

	@Transactional
	public void delete(Long id) {	
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Resource not found");
		}
		try {
			productRepository.deleteById(id);
		} 
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(ProductDto dto, Product entity) {
		
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());

		entity.getCategories().clear();
		for (CategoryDto catDto : dto.getCategories()) {
			var category = categoryRepository.getReferenceById(catDto.getId());
			entity.getCategories().add(category);
		}
	}
}
