package com.checkout.infrastructure.services

import com.checkout.domain.model.BasketProduct
import com.checkout.interfaces.dto.BasketProductDto

interface BasketService {
    fun getAllBasketProducts(): List<BasketProductDto>?
    fun addOrUpdateBasketProduct(basketProductDto: BasketProductDto): BasketProductDto?
    fun getBasketProductByProductId(productId: Long): BasketProductDto?
    fun removeBasketProduct(basketProductDto: BasketProductDto)
    fun toDto(basketProduct: BasketProduct): BasketProductDto
    fun toEntity(basketProductDto: BasketProductDto): BasketProduct
}