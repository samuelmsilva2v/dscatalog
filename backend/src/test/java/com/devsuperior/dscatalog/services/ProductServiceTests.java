package com.devsuperior.dscatalog.services;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dtos.ProductDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

/*
 * Não carrega o contexto, mas permite usar os
 * recursos do Spring com JUnit (teste de unidade: service/component)
 */
@ExtendWith(SpringExtension.class) 
public class ProductServiceTests {

	/*
	 * Injetar automaticamente os mocks anotados
	 * dentro do objeto real que está sendo testado
	 */
	@InjectMocks 
	private ProductService productService; // Classe real
	
	/*
	 * Simula o comportamento da dependência
	 * para controle nos testes
	 */
	@Mock
	private ProductRepository productRepository; // Dependência simulada
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	
	@BeforeEach
	void setUp() throws Exception {
		
		/*
		 * IDs simulados para os diferentes cenários de teste
		 */
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		category = Factory.createCategory();
		page = new PageImpl<>(List.of(product));
		
		/*
		 * Simula exceção de integridade referencial ao tentar deletar um "dependentId"
		 */
		Mockito.doThrow(DatabaseException.class).when(productRepository).deleteById(dependentId);
		
		/*
		 * Configura o comportamento do existsById para cada cenário 
		 */
		Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
		Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);
	}
	
	@Test
	public void updateShouldReturnAProductDtoWhenIdExists() {
		
		var productDto = Factory.createProductDto();
		
		var result = productService.update(existingId, productDto);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		var productDto = Factory.createProductDto();
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.update(nonExistingId, productDto);
		});
	}
	
	@Test
	public void findByIdShouldReturnAProductDtoWhenIdExists() {
		
		var result = productService.findById(existingId);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.findById(nonExistingId);
		});
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		var pageable = PageRequest.of(0, 10);
		
		Page<ProductDto> result = productService.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			productService.delete(dependentId);
		});
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.delete(nonExistingId);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			productService.delete(existingId);
		});
		
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
	}
	
}
