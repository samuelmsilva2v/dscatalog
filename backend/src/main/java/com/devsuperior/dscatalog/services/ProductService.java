package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDto> findAllPaged(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductDto::new);
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return new ProductDto(product, product.getCategories());
    }

    @Transactional
    public ProductDto insert(ProductDto dto) {
        Product product = new Product();
        copyDtoToEntity(dto, product);
        product = productRepository.save(product);
        return new ProductDto(product);
    }

    @Transactional
    public ProductDto update(Long id, ProductDto dto) {
        try {
            Product product = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, product);
            product = productRepository.save(product);
            return new ProductDto(product);
        }  catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation: cannot delete product with id: " + id);
        }
    }

    private void copyDtoToEntity(ProductDto dto, Product product) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setDate(dto.getDate());
        product.setImgUrl(dto.getImgUrl());
        product.setPrice(dto.getPrice());

        product.getCategories().clear();
        for (CategoryDto categoryDto : dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(categoryDto.getId());
            product.getCategories().add(category);
        }
    }
}
