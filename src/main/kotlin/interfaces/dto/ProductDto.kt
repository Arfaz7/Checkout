package com.checkout.interfaces.dto

import com.checkout.domain.model.Deal
import lombok.AllArgsConstructor
import lombok.Data

@Data
data class ProductDto(val id: Long?,
                      val type: String?,
                      val name: String?,
                      val price: Int?,
                      val description: String?,
                      val remainingQty: Int?,
                      val deal: Deal?)