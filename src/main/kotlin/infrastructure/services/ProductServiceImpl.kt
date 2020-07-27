package com.checkout.infrastructure.services

import com.checkout.domain.model.Deal
import com.checkout.domain.model.Product
import com.checkout.domain.repository.ProductRepository
import com.checkout.infrastructure.repository.ProductService
import com.checkout.interfaces.dto.DealDto
import com.checkout.interfaces.dto.ProductDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class ProductServiceImpl(@Autowired
                         private val productRepository: ProductRepository) : ProductService  {

    override fun getProduct(name: String): ProductDto? {
        val product : Product = productRepository.findByName(name)
        return if(product != null) toDto(product) else product
    }

    override fun createOrUpdateProduct(productDto: ProductDto): ProductDto? {
        return try {
            val product: Product = productRepository.save(toEntity(productDto))
            toDto(product)
        }catch(ex: Exception) {
            null
        }
    }

    override fun deleteProduct(productDto: ProductDto): Boolean {
        return try {
            productRepository.delete(toEntity(productDto))
            true
        } catch (ex: Exception) {
            false
        }
    }


    fun toDto(product: Product): ProductDto = ProductDto(id = product.id,
            type = product.type,
            name = product.name,
            price = product.price,
            description = product.description,
            remainingQty = product.remainingQty,
            deal = toDto(product.deal!!))

    fun toEntity(productDto: ProductDto): Product = Product(id= productDto.id,
            type = productDto.type!!.toUpperCase(),
            name = productDto.name!!.toUpperCase(),
            price = productDto.price!!,
            description = productDto.description!!,
            remainingQty = productDto.remainingQty!!,
            deal = toEntity(productDto.deal!!))

    fun toDto(deal: Deal): DealDto = DealDto(id = deal.id,
            nbProductToBuy = deal.nbProductToBuy,
            nbProductDiscounted = deal.nbProductDiscounted,
            discount = deal.discount)

    fun toEntity(dealDto: DealDto): Deal = Deal(id = dealDto.id,
            nbProductToBuy = dealDto.nbProductToBuy!!,
            nbProductDiscounted = dealDto.nbProductDiscounted!!,
            discount = dealDto.discount!!)
}