package com.devsuperior.dscatalog.services;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

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
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	
	@BeforeEach
	void setUp() throws Exception {
		
		/*
		 * IDs simulados para os diferentes cenários de teste
		 */
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		/*
		 * Simula exceção de integridade referencial ao tentar deletar um "dependentId"
		 */
		Mockito.doThrow(DatabaseException.class).when(productRepository).deleteById(dependentId);
		
		/*
		 * Configura o comportamento do existsById para cada cenário 
		 */
		Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
		Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);
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
