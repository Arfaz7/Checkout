package interfaces

import com.checkout.Checkout
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
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Checkout::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebMvc
@ActiveProfiles(value = ["test"])
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = ["classpath:/schema.sql", "classpath:/data.sql"])
class BundleControllerTest {

    private val builder = StringBuilder()
    private val SERVER_URL = "http://localhost:8080/api/v1"
    private val restTemplate: TestRestTemplate = TestRestTemplate()

    // Get tests
    @Test
    fun `get an existing bundle and get 200 OK`() {
        builder.clear()

        val productName = "DELL 150"
        val endpoint = builder.append(SERVER_URL).append("/bundle/get?productName=").append(productName).toString()

        val result : ResponseEntity<BundleDto> = restTemplate.getForEntity(
                endpoint,
                BundleDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.body.product!!.name).isEqualTo("DELL 150")
        Assertions.assertThat(result.body.offeredProduct!!.name).isEqualTo("LOGITECH G35")
    }

    @Test
    fun `get a non existing bundle get 404 NOT FOUND`() {
        builder.clear()

        val productName = "DELL 170"
        val endpoint = builder.append(SERVER_URL).append("/bundle/get?productName=").append(productName).toString()

        val result : ResponseEntity<BundleDto> = restTemplate.getForEntity(
                endpoint,
                BundleDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.body).isNull()
    }

    @Test
    fun `get a bundle from a non existing product and get 404 NOT FOUND`() {
        builder.clear()

        val productName = "DELL 170"
        val endpoint = builder.append(SERVER_URL).append("/bundle/get?productName=").append(productName).toString()

        val result : ResponseEntity<BundleDto> = restTemplate.getForEntity(
                endpoint,
                BundleDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.body).isNull()
    }

    // Creation tests
    @Test
    fun `create a new bundle and get 201 CREATED`() {
        builder.clear()

        val productName = "DELL 150"
        val offeredProductName = "LOGITECH G35"
        val endpoint = builder.append(SERVER_URL).append("/bundle/create?productName=").append(productName)
                .append("&offeredProductName=").append(offeredProductName).toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val result : ResponseEntity<BundleDto> = restTemplate.postForEntity(
                endpoint,
                null,
                BundleDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(result.body.product!!.name).isEqualTo("DELL 150")
        Assertions.assertThat(result.body.offeredProduct!!.name).isEqualTo("LOGITECH G35")
    }

    @Test
    fun `create a new bundle with non existing product and get 404 NOT FOUND`() {
        builder.clear()

        val productName = "DELL 170"
        val offeredProductName = "LOGITECH G35"
        val endpoint = builder.append(SERVER_URL).append("/bundle/create?productName=").append(productName)
                .append("&offeredProductName=").append(offeredProductName).toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON


        val result : ResponseEntity<BundleDto> = restTemplate.postForEntity(
                endpoint,
                null,
                BundleDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.body).isNull()
    }

    @Test
    fun `create a new bundle with non existing offered product and get 404 NOT FOUND`() {
        builder.clear()

        val productName = "DELL 150"
        val offeredProductName = "LOGITECH G50"
        val endpoint = builder.append(SERVER_URL).append("/bundle/create?productName=").append(productName)
                .append("&offeredProductName=").append(offeredProductName).toString()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON


        val result : ResponseEntity<BundleDto> = restTemplate.postForEntity(
                endpoint,
                null,
                BundleDto::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.body).isNull()
    }

    // Deletion tests
    @Test
    fun `delete an existing bundle and get 200 OK`() {
        builder.clear()

        val productName = "DELL 150"
        val endpoint = builder.append(SERVER_URL).append("/bundle/delete?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.body).isEqualTo("SUCCESS")
    }

    @Test
    fun `delete a bundle from non existing product and get 404 NOT FOUND`() {
        builder.clear()

        val productName = "DELL 180"
        val endpoint = builder.append(SERVER_URL).append("/bundle/delete?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.body).isEqualTo("PRODUCT ${productName} NOT FOUND")
    }

    @Test
    fun `delete a non existing bundle from an existing product and get 404 NOT FOUND`() {
        builder.clear()

        val productName = "LOGITECH G35"
        val endpoint = builder.append(SERVER_URL).append("/bundle/delete?productName=").append(productName).toString()

        val result : ResponseEntity<String> = restTemplate.exchange(
                endpoint,
                HttpMethod.DELETE,
                null,
                String::class
        )

        Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        Assertions.assertThat(result.body).isEqualTo("BUNDLE NOT FOUND FOR PRODUCT: ${productName}")
    }
}