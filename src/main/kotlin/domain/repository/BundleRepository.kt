package com.checkout.domain.repository

import com.checkout.domain.model.Bundle
import org.springframework.data.repository.CrudRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository
interface BundleRepository: CrudRepository<Bundle, Long> {
    @Nullable
    fun findByProductId(productId: Long): Bundle

    @Nullable
    fun findByOfferedProductId(offeredProductId: Long): Bundle
}