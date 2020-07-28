package com.checkout.domain.model

import javax.persistence.*

@Entity
@Table(name = "basket_product")
data class BasketProduct(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
                         @Column(name="quantity", nullable = false) val quantity: Int,
                         @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name="PRODUCT_ID") val product: Product?) {

    constructor(): this(
            id = -1,
            quantity = 0,
            product = null
    )
}