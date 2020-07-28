package com.checkout.infrastructure.services

import com.checkout.domain.model.Deal
import com.checkout.interfaces.dto.DealDto

interface DealService {
    fun deleteDeal(dealId: Long)
    fun toDto(deal: Deal): DealDto
    fun toEntity(dealDto: DealDto): Deal
}