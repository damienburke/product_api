package com.ssd.web.api

import com.ssd.persistance.entities.ProductEntity
import com.ssd.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ProductApiController(val productService: ProductService) {

    @GetMapping("/products")
    fun getProducts(): ResponseEntity<List<ProductEntity>> {
        var products = productService.getProducts()
        return ResponseEntity.ok(products)
    }
}