package com.checkout.infrastructure.services

import com.checkout.domain.model.Bundle
import com.checkout.domain.model.Deal
import com.checkout.domain.model.Product
import com.checkout.domain.repository.BundleRepository
import com.checkout.interfaces.dto.BundleDto
import com.checkout.interfaces.dto.DealDto
import com.checkout.interfaces.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BundleServiceImpl(@Autowired
                        private val bundleRepository: BundleRepository): BundleService {
    override fun getBundle(productId: Long): BundleDto? =
        try {
            val bundle: Bundle = bundleRepository.findByProductId(productId)
            toDto(bundle)
        }catch(ex: Exception) {
            null
        }


    override fun createOrUpdateProduct(bundleDto: BundleDto): BundleDto? =
        try {
            val bundle: Bundle = bundleRepository.save(toEntity(bundleDto))
            toDto(bundle)
        }catch(ex: Exception) {
            null
        }

    override fun deleteBundle(bundleId: Long): Boolean =
        try {
            bundleRepository.deleteById(bundleId)
            true
        } catch (ex: Exception) {
            false
        }

    fun toEntity(bundleDto: BundleDto): Bundle = Bundle(id= bundleDto.id!!,
            product = ProductServiceImpl.toEntity(bundleDto.product!!),
            offeredProduct = ProductServiceImpl.toEntity(bundleDto.offeredProduct!!)
    )

    fun toDto(bundle: Bundle): BundleDto = BundleDto(id = bundle.id,
            product = ProductServiceImpl.toDto(bundle.product!!),
            offeredProduct = ProductServiceImpl.toDto(bundle.offeredProduct!!)
    )
}