package com.ostia.productcatalogservice.controller;

import com.ostia.productcatalogservice.common.ApiVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

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