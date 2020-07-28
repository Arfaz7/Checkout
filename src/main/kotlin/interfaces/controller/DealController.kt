package com.checkout.interfaces.controller

import com.checkout.infrastructure.repository.ProductService
import com.checkout.infrastructure.services.DealService
import com.checkout.interfaces.dto.DealDto
import com.checkout.interfaces.dto.ProductDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiParam
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(value = ["/api/v1/deal"])
@Api(tags = ["deal"], value= "Methods to manipulate deal")
class DealController(@Autowired
                     val productService: ProductService,
                     @Autowired
                     val dealService: DealService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping(value= ["/create"])
    fun createDeal(@RequestParam @ApiParam(name= "productName", required = true) productName: String,
                      @RequestBody @ApiParam(name= "deal", required = true) dealDto: DealDto): ResponseEntity<ProductDto> {

        logger.info("Creating deal ${dealDto} for product : ${productName}")

        return try {
            val product = productService.getProduct(productName)
            val deal = dealDto.copy(id = -1)
            val updatedProduct = productService.createOrUpdateProduct(product!!.copy(deal = deal))
            ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct)

        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

    @DeleteMapping(value= ["/delete"])
    fun deleteDeal(@RequestParam @ApiParam(name= "productName", required = true) productName: String): ResponseEntity<String> {

        logger.info("Deleting deal from product : ${productName}")

        return try {
            val product = productService.getProduct(productName)
            val updatedProduct = productService.createOrUpdateProduct(product!!.copy(deal = null))
            dealService.deleteDeal(product.deal!!.id!!)
            ResponseEntity.status(HttpStatus.OK).body("SUCCESS")

        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR")
        }
    }
}