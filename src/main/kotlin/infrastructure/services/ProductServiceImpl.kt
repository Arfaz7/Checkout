package com.checkout.infrastructure.services

import com.checkout.domain.model.Product
import com.checkout.domain.repository.ProductRepository
import com.checkout.infrastructure.repository.ProductService
import com.checkout.interfaces.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(@Autowired
                         private val productRepository: ProductRepository,
                         @Autowired
                         private val dealService: DealService) : ProductService  {

    override fun getProduct(name: String): ProductDto? {
        val product : Product = productRepository.findByName(name)
        return if(product != null) toDto(product) else null
    }

    override fun createOrUpdateProduct(productDto: ProductDto): ProductDto?{
        val product: Product = productRepository.save(toEntity(productDto))
        return toDto(product)
    }

    override fun deleteProduct(productDto: ProductDto): Boolean {
        productRepository.delete(toEntity(productDto))
        return true
    }

    override fun toDto(product: Product): ProductDto = ProductDto(id = product.id,
            type = product.type,
            name = product.name,
            price = product.price,
            description = product.description,
            remainingQty = product.remainingQty,
            deal = if(product.deal != null) dealService.toDto(product.deal) else null
    )

    override fun toEntity(productDto: ProductDto)= Product (
            id = productDto.id,
            type = productDto.type!!.toUpperCase(),
            name = productDto.name!!.toUpperCase(),
            price = productDto.price!!,
            description = productDto.description!!,
            remainingQty = productDto.remainingQty!!,
            deal = if(productDto.deal != null) dealService.toEntity(productDto.deal) else null
    )
}