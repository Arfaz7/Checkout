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

        val basketProducts = basketService.getAllBasketProducts()
        return ResponseEntity.status(HttpStatus.OK).body(basketProducts)
    }

    @PostMapping(value = ["/add"])
    fun addProduct(@RequestParam @ApiParam(name= "productName", required = true) productName: String): ResponseEntity<BasketProductDto> {

        logger.info("Add product : ${productName.toUpperCase()} to basket")

        return try {
            val product = productService.getProduct(productName.toUpperCase())

            val basketProduct = BasketProductDto(
                    id = -1,
                    quantity = 1,
                    product = product
            )

            val insertedBasketProduct = basketService.addOrUpdateBasketProduct(basketProduct)
            ResponseEntity.status(HttpStatus.OK).body(insertedBasketProduct)
        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @PostMapping(value = ["/update"])
    fun updateProductQuantity(@RequestParam @ApiParam(name= "productName", required = true) productName: String,
                              @RequestParam @ApiParam(name= "quantity", required = true) quantity: Int): ResponseEntity<BasketProductDto> {

        logger.info("Update product qty : ${productName.toUpperCase()}")

        return try {
            val product = productService.getProduct(productName.toUpperCase())
            val basketProduct = basketService.getBasketProductByProductId(product!!.id!!)
            val updatedBasketProduct = basketService.addOrUpdateBasketProduct(basketProduct!!.copy(quantity = quantity))
            ResponseEntity.status(HttpStatus.OK).body(updatedBasketProduct)

        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

    @DeleteMapping(value = ["/remove"])
    fun removeProduct(@RequestParam @ApiParam(name= "productName", required = true) productName: String): ResponseEntity<String> {

        logger.info("Remove product from basket : ${productName.toUpperCase()}")

        return try {
            val product = productService.getProduct(productName.toUpperCase())
            val basketProduct = basketService.getBasketProductByProductId(product!!.id!!)
            basketService.removeBasketProduct(basketProduct!!)

            ResponseEntity.status(HttpStatus.OK).body("SUCCESS")
        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR")
        }
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