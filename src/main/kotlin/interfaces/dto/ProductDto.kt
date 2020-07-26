package com.checkout.interfaces.dto

import lombok.AllArgsConstructor
import lombok.Data

@Data
@AllArgsConstructor
data class ProductDto(val id: Long?,
                      val type: String?,
                      val name: String?,
                      val description: String?,
                      val remainingQty: Int?)