package com.checkout.domain.model

import javax.persistence.*

@Entity
@Table(name = "deal")
data class Deal(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
                @Column(name="nb_product_to_buy", nullable = false) val nbProductToBuy: Int,
                @Column(name="nb_product_discounted", nullable = false) val nbProductDiscounted: Int,
                @Column(name="discount", nullable = false) val discount: Double) {

    constructor(): this(
            id = -1,
            nbProductToBuy = 0,
            nbProductDiscounted = 0,
            discount = .0
    )
}