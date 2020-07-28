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

        logger.info("Get bundle for product : ${productName}")

        var response: ResponseEntity<BundleDto>
        val product = productService.getProduct(productName)

        if (product != null) {
            val existingBundle = bundleService.getBundle(productId = product.id!!)

            if (existingBundle != null)
                response = ResponseEntity.status(HttpStatus.OK).body(existingBundle)
            else
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)

        return response
    }

    @PostMapping(value= ["/create"])
    fun createBundle(@RequestParam @ApiParam(name= "productName", required = true) productName: String,
                   @RequestParam @ApiParam(name= "offeredProductName", required = true) offeredProductName: String): ResponseEntity<BundleDto> {

        logger.info("Creating bundle for products : ${productName} & ${offeredProductName}")

        var response : ResponseEntity<BundleDto>
        val product = productService.getProduct(productName)
        val offeredProduct = productService.getProduct(offeredProductName)

        if (product != null && offeredProduct != null) {
            val existingBundle = bundleService.getBundle(productId = product.id!!)

            if(existingBundle != null) {
                bundleService.deleteBundle(existingBundle.id!!)
            }

            val bundle = BundleDto(id = -1,
                    product = product,
                    offeredProduct = offeredProduct)

            val createdBundle = bundleService.createOrUpdateProduct(bundle)

            if (createdBundle == null)
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
            else
                response = ResponseEntity.status(HttpStatus.CREATED).body(createdBundle)
        }
        else {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }

        return response
    }

    @DeleteMapping(value= ["/delete"])
    fun deleteDeal(@RequestParam @ApiParam(name= "productName", required = true) productName: String): ResponseEntity<String> {

        logger.info("Get bundle for product : ${productName}")

        var response: ResponseEntity<String>
        val product = productService.getProduct(productName)

        if (product != null) {
            val existingBundle = bundleService.getBundle(productId = product.id!!)

            if (existingBundle != null) {
                val result = bundleService.deleteBundle(existingBundle.id!!)

                if(result)
                    response = ResponseEntity.status(HttpStatus.OK).body("SUCCESS")
                else
                    response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR DURING BUNDLE DELETION")
            }
            else
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("BUNDLE NOT FOUND FOR PRODUCT: ${productName}")
        }
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUCT ${productName} NOT FOUND")

        return response
    }
}