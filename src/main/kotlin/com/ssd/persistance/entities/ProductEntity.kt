package com.ssd.persistance.entities

import com.ssd.extensions.kotlinEquals
import com.ssd.extensions.kotlinHashCode
import com.ssd.extensions.kotlinToString
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnTransformer
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

    @ColumnTransformer(
        read = "pgp_sym_decrypt(" + "promo_code, " + "current_setting('encryption.key')" + ")",
        write = "pgp_sym_encrypt(" + "?::text," + "current_setting('encryption.key')" + ")"
    )
    @Column(columnDefinition = "bytea")
    val promoCode: String = ""
) {

    companion object {
        private val properties = arrayOf(
            ProductEntity::id,
            ProductEntity::artist,
            ProductEntity::price,
            ProductEntity::albumTitle,
            ProductEntity::releaseYear,
            ProductEntity::promoCode
        )
    }

    override fun equals(other: Any?) = kotlinEquals(other = other, properties = properties)

    override fun toString() = kotlinToString(properties = properties)

    override fun hashCode(): Int {
        return kotlinHashCode(properties = properties)
    }

}