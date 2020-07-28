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
    private val SERVER_URL = "http://localhost:8080/api/v1"
    private val restTemplate: TestRestTemplate = TestRestTemplate()

    // Get endpoint tests
    @Test
    fun `request a product with non existing name and get 404 NOT FOUND`  () {
        val productName = "DELL 400"
        builder.clear()
        val endpoint = builder.append(SERVER_URL).append("/product/get?productName=").append(productName).toString()

        val result : ResponseEntity<ProductDto> = restTemplate.getForEntity(
                endpoint,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(result.body).isNull()
    }

    @Test
    fun `request a product with an existing name and get 200 SUCCESS`() {
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

    // Create endpoint tests
    @Test
    fun `create a product with missing data and get 500 INTERNAL SERVER ERROR`() {
        builder.clear()
        val endpoint = builder.append(SERVER_URL).append("/product/create").toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val product = ProductDto(
                id = -1,
                type = null,
                name = "Razer T20",
                price = 50,
                description = "Razer Gaming Mouse",
                remainingQty = 10,
                deal = null)

        val requestEntity: HttpEntity<ProductDto> = HttpEntity<ProductDto>(product, headers)

        val result : ResponseEntity<ProductDto> = restTemplate.postForEntity(
                endpoint,
                requestEntity,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(result.body).isNull()
    }

    @Test
    fun `create a product and get 201 CREATED`() {
        builder.clear()
        val endpoint = builder.append(SERVER_URL).append("/product/create").toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val product = ProductDto(
                id = -1,
                type = "Mouse",
                name = "Razer T20",
                price = 50,
                description = "Razer Gaming Mouse",
                remainingQty = 10,
                deal = null)

        val requestEntity: HttpEntity<ProductDto> = HttpEntity<ProductDto>(product, headers)

        val result : ResponseEntity<ProductDto> = restTemplate.postForEntity(
                endpoint,
                requestEntity,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(result.body.type).isEqualTo("MOUSE")
        assertThat(result.body.name).isEqualTo("RAZER T20")
        assertThat(result.body.price).isEqualTo(50)
        assertThat(result.body.description).isEqualTo("Razer Gaming Mouse")
        assertThat(result.body.remainingQty).isEqualTo(10)
    }

    // Update endpoints tests
    @Test
    fun `update price of a non existing product and get 404 NOT FOUND`() {
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
        assertThat(result.body).isNull()
    }

    @Test
    fun `update price of an existing product and get 200 OK`() {
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

    @Test
    fun `update description of a non existing product and get 404 NOT FOUND`() {
        builder.clear()

        val productName = "DELL 170"
        val description = "LAPTOP - DELL 170 new Description"
        val endpoint = builder.append(SERVER_URL).append("/product/update/description?productName=").append(productName).toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestEntity: HttpEntity<String> = HttpEntity<String>(description, headers)

        val result : ResponseEntity<ProductDto> = restTemplate.exchange(
                endpoint,
                HttpMethod.PUT,
                requestEntity,
                ProductDto::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(result.body).isNull()
    }

    @Test
    fun `update description of an existing product and get 200 OK`() {
        builder.clear()

        val productName = "DELL 150"
        val description = "LAPTOP - DELL 150 New Description"
        val endpoint = builder.append(SERVER_URL).append("/product/update/description?productName=").append(productName).toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestEntity: HttpEntity<String> = HttpEntity<String>(description, headers)

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
        assertThat(result.body.price).isEqualTo(700)
        assertThat(result.body.description).isEqualTo("LAPTOP - DELL 150 New Description")
        assertThat(result.body.remainingQty).isEqualTo(5)
    }


    // Delete endpoint test
    @Test
    fun `delete a non existing product and get 404 NOT FOUND`() {
        builder.clear()

        val productName = "DELL 170"
        val endpoint = builder.append(SERVER_URL).append("/product/delete?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(result.body).isEqualTo("ERROR PRODUCT NOT FOUND")
    }

    @Test
    fun `delete an existing product and get 200 SUCCESS`() {
        builder.clear()

        val productName = "DELL 150"
        val endpoint = builder.append(SERVER_URL).append("/product/delete?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(result.body).isEqualTo("SUCCESS")
    }

}