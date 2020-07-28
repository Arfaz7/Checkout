package interfaces

import com.checkout.Checkout
import com.checkout.interfaces.dto.DealDto
import com.checkout.interfaces.dto.ProductDto
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
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
class DealControllerTest {

    private val builder = StringBuilder()
    private var SERVER_URL = "http://localhost:8080/api/v1"
    private val restTemplate: TestRestTemplate = TestRestTemplate()

    // Creation tests
    @Test
    fun `create a new deal with missing data and get 500 INTERNAL SERVER ERROR`() {
        builder.clear()

        val productName = "DELL 150"
        val endpoint = builder.append(SERVER_URL).append("/deal/create?productName=").append(productName).toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val deal = DealDto(
                id = -1,
                nbProductToBuy = 1,
                nbProductDiscounted = null,
                discount = .0
        )

        val requestEntity: HttpEntity<DealDto> = HttpEntity<DealDto>(deal, headers)

        val result : ResponseEntity<ProductDto> = restTemplate.postForEntity(
                endpoint,
                requestEntity,
                ProductDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        Assertions.assertThat(result.body).isNull()
    }

    @Test
    fun `create a new deal for a non existing product and get 404 NOT FOUND`() {
        builder.clear()

        val productName = "DELL 170"
        val endpoint = builder.append(SERVER_URL).append("/deal/create?productName=").append(productName).toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val deal = DealDto(
                id = -1,
                nbProductToBuy = 1,
                nbProductDiscounted = 1,
                discount = .0
        )

        val requestEntity: HttpEntity<DealDto> = HttpEntity<DealDto>(deal, headers)

        val result : ResponseEntity<ProductDto> = restTemplate.postForEntity(
                endpoint,
                requestEntity,
                ProductDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.body).isNull()
    }

    @Test
    fun `create a new deal for an existing product and get 201 CREATED`() {
        builder.clear()

        val productName = "DELL 150"
        val endpoint = builder.append(SERVER_URL).append("/deal/create?productName=").append(productName).toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val deal = DealDto(
                id = -1,
                nbProductToBuy = 1,
                nbProductDiscounted = 1,
                discount = 50.0
        )

        val requestEntity: HttpEntity<DealDto> = HttpEntity<DealDto>(deal, headers)

        val result : ResponseEntity<ProductDto> = restTemplate.postForEntity(
                endpoint,
                requestEntity,
                ProductDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(result.body.deal!!.nbProductToBuy).isEqualTo(1)
        Assertions.assertThat(result.body.deal!!.nbProductDiscounted).isEqualTo(1)
        Assertions.assertThat(result.body.deal!!.discount).isEqualTo(50.0)
    }

    // Deletion tests
    @Test
    fun `delete a deal for a non existing product and get 404 NOT FOUND`() {
        builder.clear()

        val productName = "DELL 310"
        val endpoint = builder.append(SERVER_URL).append("/deal/delete?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.body).isEqualTo("ERROR PRODUCT NOT FOUND")
    }

    @Test
    fun `delete a deal for an existing product without deal and get 500 INTERNAL SERVER ERROR`() {
        builder.clear()

        val productName = "LOGITECH G35"
        val endpoint = builder.append(SERVER_URL).append("/deal/delete?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        Assertions.assertThat(result.body).isEqualTo("NO DEAL IN THIS PRODUCT")
    }

    @Test
    fun `delete a deal for an existing product and get 200 OK`() {
        builder.clear()

        val productName = "DELL 150"
        val endpoint = builder.append(SERVER_URL).append("/deal/delete?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.body).isEqualTo("SUCCESS")
    }
}