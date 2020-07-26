package com.checkout.interfaces

import com.checkout.Checkout
import com.checkout.interfaces.dto.ProductDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Checkout::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebMvc
@ActiveProfiles(value = ["test"])
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:/schema.sql", "classpath:/data.sql"])
class ProductControllerTest {

    private val builder = StringBuilder()
    private var SERVER_URL = "http://localhost:8080/api/v1"
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
        val productName = "DELL 150"
        builder.clear()
        val endpoint = builder.append(SERVER_URL).append("/product/get?productName=").append(productName).toString()

        val result : ResponseEntity<ProductDto> = restTemplate.getForEntity(
                endpoint,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body.id).isEqualTo(1)
        assertThat(result.body.type).isEqualTo("LAPTOP")
        assertThat(result.body.name).isEqualTo("DELL 150")
        assertThat(result.body.price).isEqualTo(700)
        assertThat(result.body.description).isEqualTo("LAPTOP - DELL 150 16Go Ram")
        assertThat(result.body.remainingQty).isEqualTo(5)
    }

    @Test
    fun testCreateProductFailure() {
        builder.clear()
        val endpoint = builder.append(SERVER_URL).append("/product/create").toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val product = ProductDto(id = 3,
                type = null,
                name = "Razer T20",
                price = 50,
                description = "Razer Gaming Mouse",
                remainingQty = 10)

        val requestEntity: HttpEntity<ProductDto> = HttpEntity<ProductDto>(product, headers)

        val result : ResponseEntity<ProductDto> = restTemplate.postForEntity(
                endpoint,
                requestEntity,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun testCreateProductSuccess() {
        builder.clear()
        val endpoint = builder.append(SERVER_URL).append("/product/create").toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val product = ProductDto(id = 2,
                type = "Mouse",
                name = "Razer T20",
                price = 50,
                description = "Razer Gaming Mouse",
                remainingQty = 10)

        val requestEntity: HttpEntity<ProductDto> = HttpEntity<ProductDto>(product, headers)

        val result : ResponseEntity<ProductDto> = restTemplate.postForEntity(
                endpoint,
                requestEntity,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body.id).isEqualTo(2)
        assertThat(result.body.type).isEqualTo("Mouse")
        assertThat(result.body.name).isEqualTo("Razer T20")
        assertThat(result.body.price).isEqualTo(50)
        assertThat(result.body.description).isEqualTo("Razer Gaming Mouse")
        assertThat(result.body.remainingQty).isEqualTo(10)
    }

    @Test
    fun testUpdateProductPriceFailure() {
        builder.clear()

        val productName = "DELL 170"
        val price = 67
        val endpoint = builder.append(SERVER_URL).append("/product/update/price?productName=").append(productName).toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestEntity: HttpEntity<Int> = HttpEntity<Int>(price, headers)

        val result : ResponseEntity<ProductDto> = restTemplate.exchange(
                endpoint,
                HttpMethod.PUT,
                requestEntity,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun testUpdateProductPriceSuccess() {
        builder.clear()

        val productName = "DELL 150"
        val price = 900
        val endpoint = builder.append(SERVER_URL).append("/product/update/price?productName=").append(productName).toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestEntity: HttpEntity<Int> = HttpEntity<Int>(price, headers)

        val result : ResponseEntity<ProductDto> = restTemplate.exchange(
                endpoint,
                HttpMethod.PUT,
                requestEntity,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body.id).isEqualTo(1)
        assertThat(result.body.type).isEqualTo("LAPTOP")
        assertThat(result.body.name).isEqualTo("DELL 150")
        assertThat(result.body.price).isEqualTo(900)
        assertThat(result.body.description).isEqualTo("LAPTOP - DELL 150 16Go Ram")
        assertThat(result.body.remainingQty).isEqualTo(5)
    }
}