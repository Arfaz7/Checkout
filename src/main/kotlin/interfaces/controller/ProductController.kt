package com.checkout.interfaces.controller

import com.checkout.infrastructure.repository.ProductService
import com.checkout.infrastructure.services.BasketService
import com.checkout.infrastructure.services.BundleService
import com.checkout.interfaces.dto.ProductDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiParam
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/product"])
@Api(tags = ["product"], value= "Methods to manipulate product")
class ProductController(@Autowired
                        val productService: ProductService,
                        @Autowired
                        val bundleService: BundleService,
                        @Autowired
                        val basketService: BasketService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping(value = ["/get"])
    fun getProductByName(@RequestParam
                       @ApiParam(name= "productName", required = true)
                       productName: String): ResponseEntity<ProductDto> {

        logger.info("Getting product: ${productName.toUpperCase()}")

        return try{
            val result = productService.getProduct(productName.toUpperCase())
            ResponseEntity.status(HttpStatus.OK).body(result)
        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @PostMapping(value= ["/create"])
    fun createProduct(@RequestBody @ApiParam(name= "product", required = true) productDto: ProductDto): ResponseEntity<ProductDto> {

        logger.info("Creating product ${productDto}")

        return try {
            val createdProduct: ProductDto? = productService.createOrUpdateProduct(productDto.copy(id = -1, deal = null))
            ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

    @PutMapping(value= ["/update/price"])
    fun updateProductPrice(@RequestParam @ApiParam(name= "productName", required = true) productName: String,
                           @RequestBody @ApiParam(name= "price", required = true) productPrice: Int): ResponseEntity<ProductDto> {

        logger.info("Updating product ${productName.toUpperCase()} with price ${productPrice}")

        return try {
            val product = productService.getProduct(productName.toUpperCase())
            val updatedProduct: ProductDto? = productService.createOrUpdateProduct(product!!.copy(price = productPrice))

            if (updatedProduct != null) ResponseEntity.status(HttpStatus.OK).body(updatedProduct)
            else ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @PutMapping(value= ["/update/description"])
    fun updateProductPrice(@RequestParam @ApiParam(name= "productName", required = true) productName: String,
                           @RequestBody @ApiParam(name= "description", required = true) productDescription: String): ResponseEntity<ProductDto> {

        logger.info("Updating product ${productName.toUpperCase()} description")

        return try {
            val product = productService.getProduct(productName.toUpperCase())

            val updatedProduct: ProductDto? = productService.createOrUpdateProduct(product!!.copy(description = productDescription))
            ResponseEntity.status(HttpStatus.OK).body(updatedProduct)
        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }


    @DeleteMapping(value= ["/delete"])
    fun updateProductPrice(@RequestParam @ApiParam(name= "productName", required = true) productName: String): ResponseEntity<String> {

        logger.info("Updating product ${productName.toUpperCase()} description")

        return try {
            val product = productService.getProduct(productName.toUpperCase())

            // Remove product from basket
            val basket = basketService.getBasketProductByProductId(product!!.id!!)
            if (basket != null) basketService.removeBasketProduct(basket)

            // Delete product bundle
            val bundle = bundleService.getBundle(product.id!!)
            if(bundle != null) bundleService.deleteBundle(bundle.id!!)

            productService.deleteProduct(product)

            ResponseEntity.status(HttpStatus.OK).body("SUCCESS")

        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }
}