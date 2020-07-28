package com.checkout.domain.model

import javax.persistence.*

@Entity
@Table(name = "bundle")
data class Bundle(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
                  @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name="PRODUCT_ID") val product: Product?,
                  @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name="OFFERED_PRODUCT_ID") val offeredProduct: Product?) {

    constructor() : this(
            id = -1,
            product = null,
            offeredProduct = null
    )
}