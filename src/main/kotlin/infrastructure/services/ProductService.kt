package com.checkout.infrastructure.repository

import com.checkout.interfaces.dto.ProductDto

interface ProductService {
    fun getProduct(name:String): ProductDto?
    fun createProduct(productDto: ProductDto): ProductDto?
}