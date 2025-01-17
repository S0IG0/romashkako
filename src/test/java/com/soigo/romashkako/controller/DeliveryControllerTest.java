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
@Sql(scripts = "classpath:delivery_create.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class DeliveryControllerTest {

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
    void getDeliveries() throws Exception {
        mvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10));
    }

    @Test
    void getDeliveriesByNameFilter() throws Exception {
        mvc.perform(get("/delivery").param("name", "Delivery 1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Delivery 1"));
    }

    @Test
    void getDeliveriesByProductIdFilter() throws Exception {
        mvc.perform(get("/delivery").param("productId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void getDeliveriesSortedByCreatedAt() throws Exception {
        mvc.perform(get("/delivery").param("sortBy", "createdAt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Delivery 0"))
                .andExpect(jsonPath("$.content[9].name").value("Delivery 9"));
    }

    @Test
    void getDeliveryByIdSuccess() throws Exception {
        mvc.perform(get("/delivery/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Delivery 0"));
    }

    @Test
    void getDeliveryByIdNotFound() throws Exception {
        mvc.perform(get("/delivery/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDeliverySuccess() throws Exception {
        mvc.perform(delete("/delivery/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        mvc.perform(get("/delivery/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDeliveryNotFound() throws Exception {
        mvc.perform(delete("/delivery/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createDeliverySuccess() throws Exception {
        String deliveryJson = "{\"name\":\"New Delivery\",\"count\":10,\"product\":{\"id\":1}}";
        mvc.perform(post("/delivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deliveryJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Delivery"));
    }

    @Test
    void updateDeliverySuccess() throws Exception {
        String deliveryJson = "{\"name\":\"Updated Delivery\"}";
        mvc.perform(patch("/delivery/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deliveryJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Delivery"));
    }

    @Test
    void updateDeliveryNotFound() throws Exception {
        String deliveryJson = "{\"name\":\"Updated Delivery\"}";
        mvc.perform(patch("/delivery/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deliveryJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
