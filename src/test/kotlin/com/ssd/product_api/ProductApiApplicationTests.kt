package com.ssd.product_api

import com.ssd.persistance.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.dao.InvalidDataAccessResourceUsageException
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import javax.sql.DataSource
import kotlin.test.assertFailsWith

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductApiApplicationTests {

    init {
        System.setProperty("SA_DB_PASSWORD", "service_account_password")
        System.setProperty("FLYWAY_DB_PASSWORD", "product_api_pwd")
    }

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var dataSource: DataSource

    companion object {

        private val postgresContainer: PostgreSQLContainer<*> =
            PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("product_api_db")
                .withUsername("product_api_user")
                .withPassword("product_api_pwd")
                .withCopyFileToContainer(
                    MountableFile.forClasspathResource("create-service-account.sql"),
                    "/docker-entrypoint-initdb.d/"
                )

        @BeforeAll
        @JvmStatic
        fun startDBContainer() {
            postgresContainer.start()
        }

        @AfterAll
        @JvmStatic
        fun stopDBContainer() {
            postgresContainer.stop()
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerDBContainer(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.flyway.url", postgresContainer::getJdbcUrl)
        }
    }

    @LocalServerPort
    var port: Int = 0

    @Test
    fun contextLoads() {
        val client = HttpClient.newHttpClient()
        val baseUrl = "http://localhost"
        val request = HttpRequest.newBuilder().GET().uri(URI.create("$baseUrl:$port/products")).build()
        val response = client.send(request, BodyHandlers.ofString())
        println("RESULT:${response.body()}")
    }

    @Test
    fun attempt_to_delete() {

        println("dataSource.connection ${dataSource.connection}")


        assertFailsWith<InvalidDataAccessResourceUsageException>(
            block = {
                productRepository.deleteAll()
            }
        )
    }
}
