package com.checkout.interfaces.controller

import com.checkout.infrastructure.repository.ProductService
import com.checkout.infrastructure.services.BasketService
import com.checkout.interfaces.dto.BasketProductDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiParam
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1/basket"])
@Api(tags = ["basket"], value= "Methods to manipulate basket")
class BasketController(@Autowired
                       val basketService: BasketService,
                       @Autowired
                       val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping(value = ["/getAll"])
    fun getBasketProducts(): ResponseEntity<List<BasketProductDto>> {

        logger.info("Get all basket products")

        var response: ResponseEntity<List<BasketProductDto>>
        val basketProducts = basketService.getAllBasketProducts()

        if (!basketProducts.isNullOrEmpty())
            response = ResponseEntity.status(HttpStatus.OK).body(basketProducts)
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return response
    }

    @PostMapping(value = ["/add"])
    fun addProduct(@RequestParam @ApiParam(name= "productName", required = true) productName: String): ResponseEntity<BasketProductDto> {

        logger.info("Add product : ${productName} to basket")

        var response: ResponseEntity<BasketProductDto>
        val product = productService.getProduct(productName)

        if (product != null) {
            val basketProduct = BasketProductDto(
                    id = -1,
                    quantity = 1,
                    product = product
            )
            val insertedBasketProduct = basketService.addOrUpdateBasketProduct(basketProduct)

            if(insertedBasketProduct != null)
                response = ResponseEntity.status(HttpStatus.OK).body(insertedBasketProduct)
            else
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return response
    }

    @PostMapping(value = ["/update"])
    fun updateProductQuantity(@RequestParam @ApiParam(name= "productName", required = true) productName: String,
                              @RequestParam @ApiParam(name= "quantity", required = true) quantity: Int): ResponseEntity<BasketProductDto> {

        logger.info("Update product qty : ${productName}")

        var response: ResponseEntity<BasketProductDto>
        val product = productService.getProduct(productName)

        if (product != null) {
            val basketProduct = basketService.getBasketProductByProductId(product.id!!)

            if(basketProduct != null) {
                val updatedBasketProduct = basketService.addOrUpdateBasketProduct(basketProduct.copy(quantity = quantity))
                response = ResponseEntity.status(HttpStatus.OK).body(updatedBasketProduct)
            } else
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return response
    }

    @DeleteMapping(value = ["/remove"])
    fun removeProduct(@RequestParam @ApiParam(name= "productName", required = true) productName: String): ResponseEntity<String> {

        logger.info("Remove product from basket : ${productName}")

        var response: ResponseEntity<String>
        val product = productService.getProduct(productName)

        if (product != null) {
            val basketProduct = basketService.getBasketProductByProductId(productId = product.id!!)

            if(basketProduct != null){
                val result = basketService.removeBasketProduct(basketProduct)
                if(result)
                    response = ResponseEntity.status(HttpStatus.OK).body("SUCCESS")
                else
                    response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR WHILE REMOVING PRODUCT FROM BASKET")
            }
            else
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUCT NOT FOUND IN THE BASKET")
        }
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUCT NOT FOUND")

        return response
    }

   /* @GetMapping(value = ["/total"])
    fun getBasketTotalPrice(): ResponseEntity<BasketDto> {

        logger.info("Get basket : ${basketId}")

        var response: ResponseEntity<BasketDto>
        val basket = basketService.getBasket(basketId)

        if (basket != null)
            response = ResponseEntity.status(HttpStatus.OK).body(basket)
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return response
    }*/

}