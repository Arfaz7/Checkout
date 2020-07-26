package com.checkout.infrastructure.services

import com.checkout.domain.model.Product
import com.checkout.domain.repository.ProductRepository
import com.checkout.infrastructure.repository.ProductService
import com.checkout.interfaces.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(@Autowired
                         private val productRepository: ProductRepository) : ProductService  {

    override fun getProduct(name: String): ProductDto? {
        val product : Product = productRepository.findByName(name)
        return if(product != null) toDto(product) else product
    }

    fun toDto(product: Product): ProductDto {
        return ProductDto(product.id,
                        product.type,
                        product.name,
                        product.description,
                        product.remainingQty)
    }
}