package com.ssd.product_api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

class ProductApiApplicationTests : BaseIT() {

    @LocalServerPort
    var port: Int = 0

    @Test
    fun contextLoads() {
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("$baseUrl:$port/api/products"))
            .build()

        val response = httpClient.send(request, BodyHandlers.ofString())
        assertThat(response.statusCode()).isEqualTo(200)
    }
}
