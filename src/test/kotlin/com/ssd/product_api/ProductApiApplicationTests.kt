package com.ssd.product_api

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.dao.InvalidDataAccessResourceUsageException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlin.test.assertFailsWith

class ProductApiApplicationTests : AbstractIntegrationTest() {

    @LocalServerPort
    var port: Int = 0

    @Test
    fun contextLoads() {
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("$baseUrl:$port/actuator/health"))
            .build()

        val response = httpClient.send(request, BodyHandlers.ofString())
        assertThat(response.statusCode()).isEqualTo(200)
    }


    @Test
    fun `attempt to inject SQL`() {

        // happy path
        val product = productRepository.findAll().first()
        val retrievedProduct = productRepository.findById(product.id.toString()).get()

        val request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("$baseUrl:$port/product/${retrievedProduct.id}"))
            .build()

        val response = httpClient.send(request, BodyHandlers.ofString())
        println("Body: ${response.body()}")
        assertThat(response.statusCode()).isEqualTo(200)


        val maliciousRequest = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("$baseUrl:$port/product/1;%20DROP%20TABLE%20products;%20--"))
            .build()

        val response2 = httpClient.send(maliciousRequest, BodyHandlers.ofString())
        // Spring Web chops and ignores the semi-colon and everthing after it
        assertThat(response2.statusCode()).isEqualTo(200)

    }
}
