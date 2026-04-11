package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.resources.exceptions.ResourceExceptionHandler;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
@Import(ResourceExceptionHandler.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @Autowired
    private ObjectMapper mapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ProductDto productDto;
    private PageImpl<ProductDto> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        productDto = Factory.createProductDto();
        page = new PageImpl<>(List.of(new ProductDto()));


//        doThrow(DatabaseException.class).when(service).delete(dependentId);

    }

    @Test
    public void insertShouldReturnProductDto() throws Exception {
        when(service.insert(any())).thenReturn(productDto);

        String jsonBody = mapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(post("/products").content(jsonBody)
                .contentType("application/json").accept("application/json"));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);

        ResultActions result = mockMvc.perform((delete("/products/{id}", nonExistingId).accept("application/json")));
        result.andExpect(status().isNotFound());
    }


    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        doNothing().when(service).delete(existingId);

        ResultActions result = mockMvc.perform((delete("/products/{id}", existingId).accept("application/json")));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {
        when(service.update(eq(existingId), any())).thenReturn(productDto);

        String jsonBody = mapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId).content(jsonBody)
                .contentType("application/json").accept("application/json"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }



    @Test
    public void updateShouldReturnProductDtoWhenIdDoesNotExists() throws Exception {
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        String jsonBody = mapper.writeValueAsString(productDto);

        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId).content(jsonBody)
                .contentType("application/json").accept("application/json"));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnNotFound() throws Exception {
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        ResultActions result = mockMvc.perform((get("/products/{id}", nonExistingId).accept("application/json")));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        when(service.findById(existingId)).thenReturn(productDto);

        ResultActions result = mockMvc.perform((get("/products/{id}", existingId).accept("application/json")));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        when(service.findAllPaged(any())).thenReturn(page);

        ResultActions result = mockMvc.perform((get("/products").accept("application/json")));
        result.andExpect(status().isOk());
    }

}
