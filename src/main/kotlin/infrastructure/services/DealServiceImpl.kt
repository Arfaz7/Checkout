package com.checkout.infrastructure.services

import com.checkout.domain.model.Deal
import com.checkout.domain.repository.DealRepository
import com.checkout.interfaces.dto.DealDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DealServiceImpl(@Autowired
                         private val dealRepository: DealRepository) : DealService {

    override fun deleteDeal(dealId: Long): Boolean {
        dealRepository.deleteById(dealId)
        return true
    }

    override fun toDto(deal: Deal)= DealDto (
            id = deal.id,
            nbProductToBuy = deal.nbProductToBuy,
            nbProductDiscounted = deal.nbProductDiscounted,
            discount = deal.discount
    )

    override fun toEntity(dealDto: DealDto) = Deal(
            id= dealDto.id,
            nbProductToBuy = dealDto.nbProductToBuy!!,
            nbProductDiscounted = dealDto.nbProductDiscounted!!,
            discount = dealDto.discount!!
    )
}
