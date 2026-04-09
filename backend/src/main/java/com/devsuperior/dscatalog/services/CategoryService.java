package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDto> findAllPaged(PageRequest pageRequest) {
        Page<Category> categories = repository.findAll(pageRequest);
        return categories.map(CategoryDto::new);
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return new CategoryDto(category);
    }

    @Transactional
    public CategoryDto insert(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category = repository.save(category);
        return new CategoryDto(category);
    }

    @Transactional
    public CategoryDto update(Long id, CategoryDto dto) {
        try {
            Category category = repository.getReferenceById(id);
            category.setName(dto.getName());
            category = repository.save(category);
            return new CategoryDto(category);
        }  catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation: cannot delete category with id: " + id);
        }
    }
}
