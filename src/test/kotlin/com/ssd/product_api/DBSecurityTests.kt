package com.ssd.product_api

import com.ssd.persistance.entities.ProductEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class DBSecurityTests : BaseIT() {

    /**
     * Principle of Least Privilege
     *
     * Select, Insert, and update allowed.
     * Delete not allowed
     */
    @Test
    fun `attempt to delete product data`() {

        productRepository.findAll()
        val createdRecord = productRepository.save(
            ProductEntity(
                artist = "James Brown",
                albumTitle = "Gold",
                releaseYear = 1997,
                price = BigDecimal.valueOf(30L)
            )
        )

        productRepository.save(createdRecord.copy(releaseYear = 1977))

        assertFailsWith<InvalidDataAccessResourceUsageException>(
            block = {
                productRepository.deleteAll()
            }
        )
    }


    @Test
    fun `verify decryption`() {
        val existingProducts = productRepository.findProductsByName("Beastie Boys")
        assertThat(existingProducts.first().promoCode).isNotEqualTo("12345") // encrypted
        println("encrypted: ${existingProducts.first().promoCode}")

        val existingProduct = productRepository.findById(existingProducts.first().id)
        assertThat(existingProduct.get().promoCode).isEqualTo("12345")
    }


    @Test
    fun `attempt to inject SQL - Native Query`() {

        // Pre-condition - this product exists
        val existingProducts = productRepository.findProductsByName("Beastie Boys")
        assertThat(existingProducts.size).isEqualTo(1)

        // Simulate a SQL injection attempt using malicious input
        assertThat(productRepository.findProductsByName(sqlInjectionAttempt).isEmpty()).isTrue

        // Post-condition - this product still exists
        // Validate the database integrity is preserved after the injection attempt
        val allProductsAfterAttempt = productRepository.findProductsByName("Beastie Boys")
        assertThat(allProductsAfterAttempt.size).isEqualTo(1)
        assertThat(allProductsAfterAttempt.first().id).isEqualTo(existingProducts.first().id)
    }

    @Test
    fun `attempt to inject SQL - Query Method`() {

        // Pre-condition - this product exists
        val existingProducts = productRepository.findByArtistContainingIgnoreCase("Beastie Boys")
        assertThat(existingProducts.size).isEqualTo(1)

        // Simulate a SQL injection attempt using malicious input
        assertThat(productRepository.findProductsByName(sqlInjectionAttempt).isEmpty()).isTrue

        // Post-condition - this product still exists
        // Validate the database integrity is preserved after the injection attempt
        val allProductsAfterAttempt = productRepository.findProductsByName("Beastie Boys")
        assertThat(allProductsAfterAttempt.size).isEqualTo(1)
        assertThat(allProductsAfterAttempt.first().id).isEqualTo(existingProducts.first().id)
    }
}

const val sqlInjectionAttempt = "laptop'; DROP TABLE product; --"