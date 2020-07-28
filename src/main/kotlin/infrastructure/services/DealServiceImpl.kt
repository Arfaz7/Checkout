package com.checkout.infrastructure.services

import com.checkout.domain.model.Deal
import com.checkout.domain.model.Product
import com.checkout.domain.repository.DealRepository
import com.checkout.domain.repository.ProductRepository
import com.checkout.interfaces.dto.DealDto
import com.checkout.interfaces.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class DealServiceImpl(@Autowired
                         private val dealRepository: DealRepository) : DealService {

    override fun deleteDeal(dealId: Long): Boolean =
        try {
            dealRepository.deleteById(dealId)
            true
        } catch (ex: Exception) {
            false
        }
}
