package com.ssd.persistance.entities

import com.ssd.extensions.kotlinEquals
import com.ssd.extensions.kotlinHashCode
import com.ssd.extensions.kotlinToString
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "product")
data class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val artist: String = "",
    val albumTitle: String = "",
    val releaseYear: Int = 0,
    val price: BigDecimal = BigDecimal.ZERO,
) {

    companion object {
        private val properties = arrayOf(
            ProductEntity::id,
            ProductEntity::artist,
            ProductEntity::price,
            ProductEntity::albumTitle,
            ProductEntity::releaseYear
        )
    }

    override fun equals(other: Any?) = kotlinEquals(other = other, properties = properties)

    override fun toString() = kotlinToString(properties = properties)

    override fun hashCode(): Int {
        return kotlinHashCode(properties = properties)
    }

}