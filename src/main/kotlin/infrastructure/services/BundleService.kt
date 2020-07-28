package com.checkout.infrastructure.services

import com.checkout.domain.model.Bundle
import com.checkout.interfaces.dto.BundleDto

interface BundleService {
    fun getBundle(productId: Long): BundleDto?
    fun createOrUpdateProduct(bundleDto: BundleDto): BundleDto?
    fun deleteBundle(bundleId: Long)
    fun toDto(bundle: Bundle): BundleDto
    fun toEntity(bundleDto: BundleDto): Bundle
}