package com.checkout.infrastructure.services

import com.checkout.domain.model.Product
import com.checkout.domain.repository.ProductRepository
import com.checkout.infrastructure.repository.ProductService
import com.checkout.interfaces.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class ProductServiceImpl(@Autowired
                         private val productRepository: ProductRepository) : ProductService  {

    override fun getProduct(name: String): ProductDto? {
        val product : Product = productRepository.findByName(name)
        return if(product != null) toDto(product) else product
    }

    override fun createProduct(productDto: ProductDto): ProductDto? {
        return try {
            val product: Product = productRepository.save(toEntity(productDto))
            return toDto(product)
        }catch(ex: Exception) {
            return null
        }


    }

    fun toDto(product: Product): ProductDto {
        return ProductDto(product.id,
                        product.type,
                        product.name,
                        product.price,
                        product.description,
                        product.remainingQty)
    }

    fun toEntity(productDto: ProductDto): Product {
        return Product(id= productDto.id!!,
                    type = productDto.type!!,
                    name = productDto.name!!,
                    price = productDto.price!!,
                    description = productDto.description!!,
                    remainingQty = productDto.remainingQty!!)
    }
}