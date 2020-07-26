package com.checkout.domain.repository

import com.checkout.domain.model.Product
import org.springframework.data.repository.CrudRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: CrudRepository<Product, Long> {
    @Nullable
    fun findByName(name: String): Product
}