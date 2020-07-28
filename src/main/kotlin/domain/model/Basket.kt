package com.checkout.domain.model

import com.checkout.interfaces.dto.BundleDto
import javax.persistence.*

@Entity
@Table(name = "basket")
data class Basket(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
                  @Column(name="quantity", nullable = false) val quantity: Int,
                  @OneToMany(fetch = FetchType.LAZY) @JoinColumn(name="PRODUCT_ID") val products: List<Product>?) {

    constructor(): this(
            id = -1,
            quantity = 0,
            products = null
    )
}