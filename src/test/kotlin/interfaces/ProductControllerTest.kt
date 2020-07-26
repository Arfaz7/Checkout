package com.checkout.interfaces

import com.checkout.Checkout
import com.checkout.interfaces.controller.ProductController
import com.checkout.interfaces.dto.ProductDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Checkout::class, ProductController::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebMvc
@ActiveProfiles(value = ["test"])
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:schema.sql", "classpath:data.sql"])
class ProductControllerTest {

    private val builder = StringBuilder()
    private var SERVER_URL = "http://localhost:8080"
    private val restTemplate: TestRestTemplate = TestRestTemplate()

    @Test
    fun testGetProductNotFound() {
        val productName = "DELL 400"
        builder.clear()
        val endpoint = builder.append(SERVER_URL).append("/product/get?productName=").append(productName).toString()

        val result : ResponseEntity<ProductDto> = restTemplate.getForEntity(
                endpoint,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun testGetProductSuccess() {
        val productName = "DELL 140"
        builder.clear()
        val endpoint = builder.append(SERVER_URL).append("/product/get?productName=").append(productName).toString()

        val result : ResponseEntity<ProductDto> = restTemplate.getForEntity(
                endpoint,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}