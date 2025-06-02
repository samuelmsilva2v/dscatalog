package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dtos.CategoryDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CategoryDto> findAll() {
		List<Category> list = categoryRepository.findAll();
		return list.stream().map(x -> new CategoryDto(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDto findById(Long id) {
		Optional<Category> obj = categoryRepository.findById(id);
		var entity = obj.get();
		
		return new CategoryDto(entity);
	}
}
