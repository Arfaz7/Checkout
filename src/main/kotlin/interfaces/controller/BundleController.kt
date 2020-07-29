package com.checkout.interfaces.controller

import com.checkout.infrastructure.repository.ProductService
import com.checkout.infrastructure.services.BundleService
import com.checkout.interfaces.dto.BundleDto
import com.checkout.interfaces.dto.DealDto
import com.checkout.interfaces.dto.ProductDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiParam
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@RestController
@RequestMapping(value = ["/api/v1/bundle"])
@Api(tags = ["bundle"], value= "Methods to manipulate bundles")
class BundleController(@Autowired
                       val productService: ProductService,
                       @Autowired
                       val bundleService: BundleService) {


    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping(value = ["/get"])
    fun getBundle(@RequestParam @ApiParam(name= "productName", required = true) productName: String): ResponseEntity<BundleDto> {

        logger.info("Get bundle for product : ${productName.toUpperCase()}")

        return try {
            val product = productService.getProduct(productName.toUpperCase())
            val existingBundle = bundleService.getBundle(productId = product.id!!)
            ResponseEntity.status(HttpStatus.OK).body(existingBundle)

        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

    @PostMapping(value= ["/create"])
    fun createBundle(@RequestParam @ApiParam(name= "productName", required = true) productName: String,
                   @RequestParam @ApiParam(name= "offeredProductName", required = true) offeredProductName: String): ResponseEntity<BundleDto> {

        logger.info("Creating bundle for products : ${productName.toUpperCase()} & ${offeredProductName.toUpperCase()}")

        return try {
            val product = productService.getProduct(productName.toUpperCase())
            val offeredProduct = productService.getProduct(offeredProductName.toUpperCase())

            val existingBundle = try{bundleService.getBundle(productId = product.id!!)} catch(ex: Exception){null}

            if(existingBundle != null) {
                bundleService.deleteBundle(existingBundle.id!!)
            }

            val bundle = BundleDto(id = -1,
                    product = product,
                    offeredProduct = offeredProduct)

            val createdBundle = bundleService.createOrUpdateProduct(bundle)
            ResponseEntity.status(HttpStatus.CREATED).body(createdBundle)
        } catch (ex: Exception) {
            logger.error(ex.localizedMessage)
             ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        }
    }

    @DeleteMapping(value= ["/delete"])
    fun deleteDeal(@RequestParam @ApiParam(name= "productName", required = true) productName: String): ResponseEntity<String> {

        logger.info("Get bundle for product : ${productName.toUpperCase()}")

        return try {
            val product = productService.getProduct(productName.toUpperCase())
            val existingBundle = bundleService.getBundle(productId = product.id!!)
            bundleService.deleteBundle(existingBundle!!.id!!)
            ResponseEntity.status(HttpStatus.OK).body("SUCCESS")

        } catch (ex: Exception){
            logger.error(ex.localizedMessage)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR DURING BUNDLE DELETION FOR ${productName.toUpperCase()}")
        }
    }
}