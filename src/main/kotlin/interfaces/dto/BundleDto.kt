package com.checkout.interfaces.dto

import lombok.Data

@Data
data class BundleDto(val id: Long?,
                     val product: ProductDto?,
                     val offeredProduct: ProductDto?)