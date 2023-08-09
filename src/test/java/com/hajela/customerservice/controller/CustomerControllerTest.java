package com.hajela.customerservice.controller;

import com.hajela.customerservice.BaseIntegrationTest;
import com.hajela.customerservice.domain.AllCustomers;
import com.hajela.customerservice.domain.CustomerDto;
import com.hajela.customerservice.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Rollback
@Transactional
class CustomerControllerTest extends BaseIntegrationTest {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private CustomerRepository customerRepository;

    private final RestAssuredConfig config = new RestAssuredConfig().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.basePath = "/v1/customers";
        customerRepository.deleteAll(customerRepository.findAll());
        log.info("Total no of records: {}", customerRepository.findAll().size());
    }

    @AfterEach
    void cleanUp() {
        customerRepository.deleteAll(customerRepository.findAll());
        log.info("Total no of records after deletion: {}", customerRepository.findAll().size());
    }

    @Test
    @DisplayName("Get all customers")
    void getAllCustomers() {
        postCustomer(CustomerHelper.createCustomerDto());

        // @formatter:off
        AllCustomers allCustomers = given().log().all()
                .config(config)
                .when()
                .get()
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract().response().as(AllCustomers.class);
        // @formatter:on
        assertEquals(1, allCustomers.getTotalPages(), "Pages match");
        assertTrue(allCustomers.getTotalItems() >= 1, "Total items >= 1");
    }

    @Test
    @DisplayName("Get a customer")
    void getCustomer() {
        postCustomer(CustomerHelper.createCustomerDto());

        // @formatter:off
        given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .pathParam("customerId", 1)
                .when()
                .get("/{customerId}")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue());
        // @formatter:on

    }

    @Test
    @DisplayName("Save a new customer")
    void saveCustomer() {
        var customerDto = CustomerHelper.createCustomerDto();
        postCustomer(customerDto);
    }

    @Test
    @DisplayName("Update a customer")
    void updateCustomer() {
        var customerDto = CustomerHelper.createCustomerDto();
        // @formatter:off
        CustomerDto response = given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .body(customerDto)
                .when()
                .post()
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .extract().response().as(CustomerDto.class);
        // @formatter:on

        // test GET
        given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .pathParam("customerId", response.getId())
                .when()
                .get("/{customerId}")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("id", is(response.getId()));

        customerDto.setUserName("UPDATED_USERNAME");
        given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .pathParam("customerId", response.getId())
                .body(customerDto)
                .when()
                .put("/{customerId}")
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .body("userName", is("UPDATED_USERNAME"));
    }

    private void postCustomer(CustomerDto customerDto) {
        // @formatter:off
        given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .body(customerDto)
                .when()
                .post()
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .body("id", notNullValue());
        // @formatter:on
    }

    @Test
    void invalidDtoDuringPostShouldThrowError() {
        CustomerDto invalidDto = CustomerDto.builder()
                .email("invalidemail")
                .firstName("")
                .lastName("")
                .userName("12")
                .build();
        // @formatter:off
        given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .body(invalidDto)
                .when()
                .post()
                .then().log().all()
                .assertThat()
                .statusCode(400);
        // @formatter:on
    }

    @Test
    void invalidCustomerIdReturns404() {
        // @formatter:off
        given().log().all()
                .config(config)
                .contentType(ContentType.JSON)
                .pathParam("customerId", 100)
                .when()
                .get("/{customerId}")
                .then().log().all()
                .assertThat()
                .statusCode(404);
        // @formatter:on
    }
}