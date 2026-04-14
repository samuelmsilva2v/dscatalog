package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        ResultActions result = mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
                .contentType("application/json").accept("application/json"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.[0].name").value("Macbook Pro"));
    }

//    @Test
//    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {
//        ProductDto productDto = Factory.createProductDto();
//        String jsonBody = objectMapper.writeValueAsString(productDto);
//
//        String expectedName = productDto.getName();
//        String expectedDescription = productDto.getDescription();
//
//        ResultActions result = mockMvc.perform(put("/products/{id}", existingId).content(jsonBody)
//                .contentType("application/json").accept("application/json"));
//
//        result.andExpect(status().isOk());
//        result.andExpect(jsonPath("$.id").value(existingId.intValue()));
//        result.andExpect(jsonPath("$.name").value(expectedName));
//        result.andExpect(jsonPath("$.description").value(expectedDescription));
//    }

}
