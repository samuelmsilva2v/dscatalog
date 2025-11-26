package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dtos.CategoryDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CategoryDto> findAll() {
		var list = categoryRepository.findAll();
		return list.stream().map(x -> new CategoryDto(x)).toList();
	}

	@Transactional(readOnly = true)
	public CategoryDto findById(Long id) {

		Optional<Category> obj = categoryRepository.findById(id);
		var entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

		return new CategoryDto(entity);
	}

	@Transactional(readOnly = true)
	public CategoryDto insert(CategoryDto dto) {

		var entity = new Category();
		entity.setName(dto.getName());
		entity = categoryRepository.save(entity);

		return new CategoryDto(entity);
	}

	@Transactional
	public CategoryDto update(Long id, CategoryDto dto) {

		try {
			var entity = categoryRepository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = categoryRepository.save(entity);
			return new CategoryDto(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id " + id + " not found");
		}
	}

	@Transactional
	public void delete(Long id) {
		
		if (!categoryRepository.existsById(id)) {
			throw new ResourceNotFoundException("Resource not found");
		}
		
		try {
			categoryRepository.deleteById(id);
		} 
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
}
