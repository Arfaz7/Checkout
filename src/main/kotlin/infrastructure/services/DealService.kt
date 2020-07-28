package com.checkout.infrastructure.services

import com.checkout.interfaces.dto.DealDto

interface DealService {
    fun deleteDeal(dealId: Long): Boolean
}