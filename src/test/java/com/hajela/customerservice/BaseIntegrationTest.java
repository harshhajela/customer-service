package com.hajela.customerservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
@Rollback
@Transactional
@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {CustomerServiceApplication.class})
public abstract class BaseIntegrationTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("customer")
            .withUsername("customer")
            .withPassword("customer");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
        dymDynamicPropertyRegistry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        dymDynamicPropertyRegistry.add("spring.datasource.username", postgresContainer::getUsername);
        dymDynamicPropertyRegistry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeAll
    public static void setUp() {
        postgresContainer.start();
    }

    @BeforeEach
    public void SetUpTest(TestInfo testInfo) {
        log.info("Starting test: {}", testInfo.getDisplayName());
    }

    @AfterAll
    public static void tearDown() {
        // Stop and remove the PostgreSQL container after all tests are finished
        postgresContainer.stop();
    }

}
