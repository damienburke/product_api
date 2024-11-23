package com.ssd.persistance.repository

import com.ssd.persistance.entities.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<ProductEntity, Int> {

    /**
     * The method is the exactly same as the spring magic method:
     *
     * {@code findByArtistContainingIgnoreCase}
     *
     * , but is additionally implemented as Native Query to demonstrate its ability to prevent SQL injection attacks.
     *
     * Also... this method does not decrypt the promo code. The Query Method methods do decrypt.
     */
    @Query(
        value = "SELECT * FROM product WHERE LOWER(TRIM(artist)) LIKE LOWER(CONCAT('%', :artist, '%'))",
        nativeQuery = true
    )
    fun findProductsByName(artist: String): List<ProductEntity>


    /**
     * <h2>SQL Injection Prevention</h2>
     *
     * Spring Data JPA uses hibernate behind the scenes which in turn uses PreparedStatements way to deal with database.
     *
     * PreparedStatements
     * sanitize the input by escaping special characters, such as the single quote ({@code '}).</li>
     *
     * Example:
     *
     *<pre>
     * $query = "SELECT * FROM users where id=$expected_data";
     * $spoiled_data = "1; DROP TABLE users;"
     * $query:              "SELECT * FROM users where id=$spoiled_data";
     * $resolved_query:     "SELECT * FROM users WHERE id = '1; DROP TABLE users;'
     * Will produce a malicious query:
     *
     * SELECT * FROM users where id=1; DROP TABLE users;
     *</pre>
     *
     */
    fun findByArtistContainingIgnoreCase(artist: String): List<ProductEntity>
}
