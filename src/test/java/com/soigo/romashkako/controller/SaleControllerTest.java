package com.soigo.romashkako.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
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
@Sql(scripts = "classpath:sale_create.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class SaleControllerTest {

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
    void getSales() throws Exception {
        mvc.perform(get("/sale"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10));
    }

    @Test
    void getSalesByNameFilter() throws Exception {
        mvc.perform(get("/sale").param("name", "Sale 1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Sale 1"));
    }

    @Test
    void getSalesByProductIdFilter() throws Exception {
        mvc.perform(get("/sale").param("productId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void getSalesByMinCostFilter() throws Exception {
        mvc.perform(get("/sale").param("minCost", "5.00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5));
    }

    @Test
    void getSalesByMaxCostFilter() throws Exception {
        mvc.perform(get("/sale").param("maxCost", "5.00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(6));
    }

    @Test
    void getSalesSortedByCreatedAt() throws Exception {
        mvc.perform(get("/sale").param("sortBy", "createdAt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Sale 0"))
                .andExpect(jsonPath("$.content[9].name").value("Sale 9"));
    }

    @Test
    void getSaleByIdSuccess() throws Exception {
        mvc.perform(get("/sale/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sale 1"));
    }

    @Test
    void getSaleByIdNotFound() throws Exception {
        mvc.perform(get("/sale/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSaleSuccess() throws Exception {

        ResultActions resultActions = mvc.perform(get("/sale"))
                .andDo(print())
                .andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray content = jsonResponse.getJSONArray("content");
        JSONObject item = content.getJSONObject(content.length() - 1);
        long id = item.getLong("id");

        mvc.perform(delete("/sale/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());

        mvc.perform(get("/sale/" + id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSaleNotFound() throws Exception {
        mvc.perform(delete("/sale/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createSaleSuccess() throws Exception {
        String saleJson = "{\"name\":\"New Sale\",\"count\":10,\"product\":{\"id\":1}}";
        mvc.perform(post("/sale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saleJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Sale"));
    }

    @Test
    void updateSaleSuccess() throws Exception {
        String saleJson = "{\"name\":\"Updated Sale\"}";
        mvc.perform(patch("/sale/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saleJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Sale"));
    }

    @Test
    void updateSaleNotFound() throws Exception {
        String saleJson = "{\"name\":\"Updated Sale\"}";
        mvc.perform(patch("/sale/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saleJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
