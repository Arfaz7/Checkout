package com.checkout.interfaces.dto

import lombok.Data

@Data
data class DealDto(val id: Long?,
                   val nbProductToBuy: Int?,
                   val nbProductDiscounted: Int?,
                   val discount: Double?)