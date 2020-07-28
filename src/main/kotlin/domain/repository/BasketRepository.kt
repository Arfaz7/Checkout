package com.checkout.domain.repository

import com.checkout.domain.model.BasketProduct
import org.springframework.data.repository.CrudRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface BasketRepository: CrudRepository<BasketProduct, Long> {
    @Nullable
    fun findByProductId(productId: Long): BasketProduct
}