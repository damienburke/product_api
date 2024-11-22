package com.ssd.service

import com.ssd.persistance.entities.ProductEntity
import java.util.Optional

interface ProductService {

    fun getProducts(): List<ProductEntity>
    fun addProduct(entity: ProductEntity)
    fun findProductById(id: String): Optional<ProductEntity?>
}