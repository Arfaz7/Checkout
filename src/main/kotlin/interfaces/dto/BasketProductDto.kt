package com.checkout.interfaces.dto

import com.checkout.domain.model.Product
import lombok.Data

@Data
data class BasketProductDto(val id: Long?,
                            val quantity: Int,
                            val product: ProductDto?)