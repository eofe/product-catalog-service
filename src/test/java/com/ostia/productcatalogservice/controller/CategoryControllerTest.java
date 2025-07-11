package com.ostia.productcatalogservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ostia.productcatalogservice.common.ApiVersion;
import com.ostia.productcatalogservice.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import java.util.Objects;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @LocalServerPort
    private int port;

    private String productEndpoint;

    @BeforeEach
    public void setUp() {
        productEndpoint = "http://localhost:" + port + "/" + ApiVersion.V1 +"/categories";
    }

    @Test
    public void shouldReturn201WithLocationHeaderContainingNewCategoryId() {

        // Arrange
        String json = """
                {
                  "name": "blabla",
                  "description": "Devices, gadgets, and accessories"
                }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        // Act
        var response = restTemplate.postForEntity(productEndpoint, request, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getHeaders().getLocation().toString()).contains("/categories/");

        // Extract UUID from Location header
        String[] parts = response.getHeaders().getLocation().getPath().split("/");
        String uuid = parts[parts.length - 1];

        // Validate UUID format
        assertThat(isValidUUID(uuid)).isTrue();
    }

    @Test
    void shouldReturnBadRequestErrorResponseForMalformedJsonPayload() {
        // Arrange
        String invalidPayload = """
            {
              "name": "Electronics"
              "description": "Devices, gadgets, and tech accessories"
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(invalidPayload, headers);

        // Act
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/api/v1/categories", request, ErrorResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponse body = Objects.requireNonNull(response.getBody());

        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getError()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertThat(body.getMessage()).isEqualTo("Request JSON is malformed or invalid.");
        assertThat(body.getPath()).isEqualTo("/api/v1/categories");
        assertThat(body.getTimestamp()).isNotNull();
    }

    @Test
    void shouldReturnUnprocessableEntityForInvalidCategoryPayload() {
        // Arrange
        String invalidPayload = """
        {
          "name": "",
          "description": "Devices, gadgets, and tech accessories"
        }
    """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(invalidPayload, headers);

        // Act
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/api/v1/categories", request, ErrorResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        ErrorResponse body = Objects.requireNonNull(response.getBody());

        assertThat(body.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(body.getError()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());

        assertThat(body.getMessage()).contains("Validation failed for one or more fields.");
        assertThat(body.getPath()).isEqualTo("/api/v1/categories");
        assertThat(body.getTimestamp()).isNotNull();
    }

    @Test
    void shouldGetCategory() throws JsonProcessingException {
        // Arrange and act
        var response = restTemplate.exchange(
                productEndpoint + "?name=" + "Books",
                HttpMethod.GET,
                null,
                String.class);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Parse raw JSON body
        var body = mapper.readTree(response.getBody());

        // Extract fields
        String name = body.get("name").asText();
        String description = body.get("description").asText();

        assertThat(name).isEqualTo("Books");
        assertThat(description).isEqualTo("Fiction, non-fiction, academic, and more");
    }

    @Test
    void shouldDeleteCategory() {
        // Arrange and act
        var response = restTemplate.exchange(
                productEndpoint + "?name=" + "Video Games",
                HttpMethod.DELETE,
                null,
                String.class);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldThrowEntityNotFoundWhenCategoryNotExist() throws JsonProcessingException {

        var response = restTemplate.exchange(
                productEndpoint + "?name=France",
                HttpMethod.GET,
                null,
                String.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // Parse raw JSON body
        var body = mapper.readTree(response.getBody());

        // Extract fields
        int status = body.get("status").asInt();
        String error = body.get("error").asText();
        String message = body.get("message").asText();
        String path = body.get("path").asText();

        assertThat(status).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(error).isEqualTo("Not Found");
        assertThat(message).isEqualTo("Category with name France does not exist.");
        assertThat(path).isEqualTo("/api/v1/categories");
    }

    private static boolean isValidUUID(String str) {
        if (str == null) {
            return false;
        }
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}