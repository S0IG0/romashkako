package com.soigo.romashkako.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:product_create.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:alpine"));

    @DynamicPropertySource
    static void configurePostgres(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void getProducts() throws Exception {
        mvc.perform(get("/product"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10));
    }

    @Test
    void getProductsByNameFilter() throws Exception {
        mvc.perform(get("/product").param("name", "Product 1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Product 1"));
    }

    @Test
    void getProductsByMinPriceFilter() throws Exception {
        mvc.perform(get("/product").param("minPrice", "5.00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5));
    }

    @Test
    void getProductsByMaxPriceFilter() throws Exception {
        mvc.perform(get("/product").param("maxPrice", "5.00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(6));
    }

    @Test
    void getProductsByAvailabilityFilter() throws Exception {
        mvc.perform(get("/product").param("availability", "OUT_OF_STOCK"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10));
    }

    @Test
    void getProductsSortedByName() throws Exception {
        mvc.perform(get("/product").param("sortBy", "name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Product 0"))
                .andExpect(jsonPath("$.content[9].name").value("Product 9"));
    }

    @Test
    void getProductsSortedByPrice() throws Exception {
        mvc.perform(get("/product").param("sortBy", "price"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].price").value(0.00))
                .andExpect(jsonPath("$.content[9].price").value(9.00));
    }

    @Test
    void getProductsSortedByNameReversed() throws Exception {
        mvc.perform(get("/product").param("sortBy", "name").param("reverse", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Product 9"))
                .andExpect(jsonPath("$.content[9].name").value("Product 0"));
    }

    @Test
    void getProductsSortedByPriceReversed() throws Exception {
        mvc.perform(get("/product").param("sortBy", "price").param("reverse", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].price").value(9.00))
                .andExpect(jsonPath("$.content[9].price").value(0.00));
    }

    @Test
    void getProductsSortedByNameWithMinPriceFilter() throws Exception {
        mvc.perform(get("/product").param("sortBy", "name").param("minPrice", "5.00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Product 5"))
                .andExpect(jsonPath("$.content[4].name").value("Product 9"));
    }

    @Test
    void getProductsSortedByPriceWithMaxPriceFilter() throws Exception {
        mvc.perform(get("/product").param("sortBy", "price").param("maxPrice", "5.00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].price").value(0.00))
                .andExpect(jsonPath("$.content[5].price").value(5.00));
    }

    @Test
    void getProductsSortedByNameReversedWithAvailabilityFilter() throws Exception {
        mvc.perform(get("/product").param("sortBy", "name").param("reverse", "true").param("availability", "OUT_OF_STOCK"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Product 9"))
                .andExpect(jsonPath("$.content[9].name").value("Product 0"));
    }

    @Test
    void getProductsSortedByPriceReversedWithNameFilter() throws Exception {
        mvc.perform(get("/product").param("sortBy", "price").param("reverse", "true").param("name", "Product 5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].price").value(5.00));
    }

    @Test
    void getProductByIdSuccess() throws Exception {
        mvc.perform(get("/product/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product 0"));
    }

    @Test
    void getProductByIdNotFound() throws Exception {
        mvc.perform(get("/product/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProductSuccess() throws Exception {
        mvc.perform(delete("/product/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        mvc.perform(get("/product/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(true));
    }

    @Test
    void deleteProductNotFound() throws Exception {
        mvc.perform(delete("/product/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createProductSuccess() throws Exception {
        String productJson = "{\"name\":\"New Product\",\"description\":\"Description for New Product\",\"price\":10.00}";
        mvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    void updateProductSuccess() throws Exception {
        String productJson = "{\"name\":\"Updated Product\"}";
        mvc.perform(patch("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void updateProductNotFound() throws Exception {
        String productJson = "{\"name\":\"Updated Product\"}";
        mvc.perform(patch("/product/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
