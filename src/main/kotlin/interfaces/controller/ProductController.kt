package com.checkout.interfaces.controller

import com.checkout.domain.repository.ProductRepository
import com.checkout.infrastructure.repository.ProductService
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
                        val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping(value = ["/get"])
    fun getProductByName(@RequestParam
                       @ApiParam(name= "productName", required = true)
                       productName: String): ResponseEntity<ProductDto> {

        logger.info("Getting product: ${productName}")

        var response : ResponseEntity<ProductDto>
        val result = productService.getProduct(productName)

        if (result == null)
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        else
            response = ResponseEntity.status(HttpStatus.OK).body(result)

        return response
    }

    @PostMapping(value= ["/create"])
    fun createProduct(@RequestBody @ApiParam(name= "type", required = true) productDto: ProductDto): ResponseEntity<ProductDto> {

        logger.info("Creating product ${productDto.name}")

        var response : ResponseEntity<ProductDto>
        val createdProduct: ProductDto? = productService.createProduct(
                ProductDto(id= productDto.id,
                        type = productDto.type,
                        name = productDto.name,
                        price = productDto.price,
                        description = productDto.description,
                        remainingQty = productDto.remainingQty
                )
        )

        if (createdProduct == null)
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        else
            response = ResponseEntity.status(HttpStatus.OK).body(createdProduct)

        return response
    }

    @PutMapping(value= ["/update/price"])
    fun updateProductPrice(@RequestParam @ApiParam(name= "name", required = true) productDto: ProductDto): ResponseEntity<ProductDto> {

        logger.info("Creating product ${productDto.name}")

        var response : ResponseEntity<ProductDto>
        val createdProduct: ProductDto? = productService.createProduct(
                ProductDto(id= productDto.id,
                        type = productDto.type,
                        name = productDto.name,
                        price = productDto.price,
                        description = productDto.description,
                        remainingQty = productDto.remainingQty
                )
        )

        if (createdProduct == null)
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        else
            response = ResponseEntity.status(HttpStatus.OK).body(createdProduct)

        return response
    }
}