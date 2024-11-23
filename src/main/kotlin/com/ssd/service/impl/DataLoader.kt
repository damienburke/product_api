package com.ssd.service.impl

import com.ssd.persistance.entities.ProductEntity
import com.ssd.persistance.repository.ProductRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class DataLoader(val productRepository: ProductRepository) {

    @PostConstruct
    fun init() {
        productRepository.save(
            ProductEntity(
                artist = "Derek & The Dominos",
                albumTitle = "Layla And Other Assorted Love Songs",
                releaseYear = 1970,
                price = BigDecimal.valueOf(45L)
            )
        )
        productRepository.save(
            ProductEntity(
                artist = "Beastie Boys",
                albumTitle = "Licensed To Ill",
                releaseYear = 1986,
                price = BigDecimal.valueOf(30L),
                promoCode = "12345"
            )
        )
    }
}