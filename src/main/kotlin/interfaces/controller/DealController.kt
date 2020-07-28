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

        var response : ResponseEntity<ProductDto>
        val product = productService.getProduct(productName)
        val deal = DealDto(
                        id= -1,
                        nbProductDiscounted = dealDto.nbProductDiscounted,
                        nbProductToBuy = dealDto.nbProductToBuy,
                        discount = dealDto.discount)

        if (product == null)
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        else {

            val updatedProduct = productService.createOrUpdateProduct(ProductDto(
                    id = product.id,
                    type = product.type,
                    name = product.name,
                    price = product.price,
                    description = product.description,
                    remainingQty = product.remainingQty,
                    deal = deal)
            )

            if (updatedProduct == null)
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
            else
                response = ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct)
        }

        return response
    }

    @DeleteMapping(value= ["/delete"])
    fun deleteDeal(@RequestParam @ApiParam(name= "productName", required = true) productName: String): ResponseEntity<String> {

        logger.info("Deleting deal from product : ${productName}")

        var response : ResponseEntity<String>
        val product = productService.getProduct(productName)

        if (product == null)
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR PRODUCT NOT FOUND")
        else {
            val productWithoutDeal = ProductDto(
                    id = product.id,
                    type = product.type,
                    name = product.name,
                    price = product.price,
                    description = product.description,
                    remainingQty = product.remainingQty,
                    deal = null
            )
            val updatedProduct = productService.createOrUpdateProduct(productWithoutDeal)
            val result = if(product.deal != null) dealService.deleteDeal(product.deal.id!!) else false

            if (result == false)
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("NO DEAL IN THIS PRODUCT")
            else
                response = ResponseEntity.status(HttpStatus.OK).body("SUCCESS")
        }

        return response
    }
}