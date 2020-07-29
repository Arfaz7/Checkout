package com.checkout.infrastructure.services

import com.checkout.domain.model.BasketProduct
import com.checkout.domain.repository.BasketRepository
import com.checkout.infrastructure.repository.ProductService
import com.checkout.interfaces.dto.BasketProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BasketServiceImpl(@Autowired
                        private val basketRepository: BasketRepository,
                        @Autowired
                        private val productService: ProductService): BasketService {

    override fun getAllBasketProducts(): List<BasketProductDto> = basketRepository.findAll()
            .map { basketProduct -> toDto(basketProduct) }


    override fun addOrUpdateBasketProduct(basketProductDto: BasketProductDto): BasketProductDto {
        val basketProduct: BasketProduct = basketRepository.save(toEntity(basketProductDto))
        return toDto(basketProduct)
    }

    override fun getBasketProductByProductId(productId: Long): BasketProductDto {
        val basketProduct : BasketProduct = basketRepository.findByProductId(productId)
        return toDto(basketProduct)
    }

    override fun removeBasketProduct(basketProductDto: BasketProductDto) = basketRepository.delete(toEntity(basketProductDto))

    override fun toDto(basketProduct: BasketProduct): BasketProductDto = BasketProductDto(
            id = basketProduct.id!!,
            quantity = basketProduct.quantity,
            product = productService.toDto(basketProduct.product!!)
    )

    override fun toEntity(basketProductDto: BasketProductDto): BasketProduct = BasketProduct(
            id = basketProductDto.id,
            quantity = basketProductDto.quantity,
            product = productService.toEntity(basketProductDto.product!!)
    )
}