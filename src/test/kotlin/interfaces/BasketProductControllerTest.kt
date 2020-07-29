package interfaces

import com.checkout.Checkout
import com.checkout.interfaces.dto.BasketProductDto
import com.checkout.interfaces.dto.BundleDto
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Checkout::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebMvc
@ActiveProfiles(value = ["test"])
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:/schema.sql", "classpath:/data.sql"])
class BasketProductControllerTest {

    private val builder = StringBuilder()
    private val SERVER_URL = "http://localhost:8080/api/v1"
    private val restTemplate: TestRestTemplate = TestRestTemplate()

    // Get tests
    @Test
    fun `get all basketProducts and get 200 OK`() {
        builder.clear()

        val endpoint = builder.append(SERVER_URL).append("/basket/getAll").toString()

        val result : ResponseEntity<List<BasketProductDto>> = restTemplate.getForEntity(
                endpoint,
                object: ParameterizedTypeReference<List<BasketProductDto>>(){}
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.body.size).isEqualTo(1)
    }

    @Test
    fun `add a product to a basket and get a 200 OK`() {
        builder.clear()

        val productName = "LOGITECH G35"
        val endpoint = builder.append(SERVER_URL).append("/basket/add?productName=").append(productName).toString()

        val result : ResponseEntity<BasketProductDto> = restTemplate.postForEntity(
                endpoint,
                null,
                BasketProductDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.body.quantity).isEqualTo(1)
        Assertions.assertThat(result.body.product!!.name).isEqualTo(productName)
    }


    @Test
    fun `add a non existing product and get a 404 NOT FOUND`() {
        builder.clear()

        val productName = "LOGITECH X5"
        val endpoint = builder.append(SERVER_URL).append("/basket/add?productName=").append(productName).toString()

        val result : ResponseEntity<BasketProductDto> = restTemplate.postForEntity(
                endpoint,
                null,
                BasketProductDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.body).isNull()
    }

    @Test
    fun `update a non existing product quantity and get a 404 NOT FOUND`() {
        builder.clear()

        val productName = "LOGITECH X5"
        val endpoint = builder.append(SERVER_URL).append("/basket/update?productName=").append(productName)
                .append("&quantity=").append(10).toString()

        val result : ResponseEntity<BasketProductDto> = restTemplate.postForEntity(
                endpoint,
                null,
                BasketProductDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.body).isNull()
    }

    @Test
    fun `update a product quantity and get a 200 OK`() {
        builder.clear()

        val productName = "DELL 150"
        val endpoint = builder.append(SERVER_URL).append("/basket/update?productName=").append(productName)
                .append("&quantity=").append(10).toString()

        val result : ResponseEntity<BasketProductDto> = restTemplate.postForEntity(
                endpoint,
                null,
                BasketProductDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.body.quantity).isEqualTo(10)
    }

    @Test
    fun `remove a product from basket and get a 200 OK`() {
        builder.clear()

        val productName = "DELL 150"
        val endpoint = builder.append(SERVER_URL).append("/basket/remove?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `remove a product which is not in the basket and get a 500 INTERNAL SERVER ERROR`() {
        builder.clear()

        val productName = "LOGITECH G35"
        val endpoint = builder.append(SERVER_URL).append("/basket/remove?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `remove a non existing product get a 500 INTERNAL SERVER ERROR`() {
        builder.clear()

        val productName = "DELL 870"
        val endpoint = builder.append(SERVER_URL).append("/basket/remove?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `get basket total price and get 200 OK`() {
        builder.clear()

        val endpoint = builder.append(SERVER_URL).append("/basket/total").toString()

        val result : ResponseEntity<String> = restTemplate.getForEntity(
                endpoint,
                Double::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.body).isEqualTo(2800.0.toString())

    }
}