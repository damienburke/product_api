package com.ssd.service.impl

import com.ssd.persistance.entities.ProductEntity
import com.ssd.persistance.repository.ProductRepository
import com.ssd.service.ProductService
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(val productRepository: ProductRepository) : ProductService {
    override fun getProducts(): List<ProductEntity> = productRepository.findAll()
    override fun addProduct(entity: ProductEntity) {
        productRepository.save<ProductEntity>(entity)
    }

    override fun findProductById(id: Int) = productRepository.findById(id)
}