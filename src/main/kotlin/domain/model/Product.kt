package com.checkout.domain.model

import javax.persistence.*

@Entity
@Table(name = "product")
data class Product(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
              @Column(name="type", nullable = false) val type: String,
              @Column(name="name", nullable = false) val name: String,
              @Column(name="description", nullable = true) val description: String,
              @Column(name="remaining_qty", nullable = false) val remainingQty: Int) {

    constructor(): this(
            id=-1,
            type = "",
            name = "",
            description = "",
            remainingQty = 0
    )
}