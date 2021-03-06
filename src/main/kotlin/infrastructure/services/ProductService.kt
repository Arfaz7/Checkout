package com.checkout.infrastructure.repository

import com.checkout.domain.model.Product
import com.checkout.interfaces.dto.ProductDto

interface ProductService {
    fun getProduct(name:String): ProductDto
    fun createOrUpdateProduct(productDto: ProductDto): ProductDto
    fun deleteProduct(productDto: ProductDto)
    fun toDto(product: Product): ProductDto
    fun toEntity(productDto: ProductDto): Product
}