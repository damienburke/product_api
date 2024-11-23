package com.ssd.product_api

import com.ssd.persistance.repository.ProductRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile
import java.net.http.HttpClient

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AbstractIntegrationTest {

    init {
        System.setProperty("SA_DB_PASSWORD", "service_account_password")
        System.setProperty("FLYWAY_DB_PASSWORD", "product_api_pwd")
    }

    companion object {

        private val postgresContainer: PostgreSQLContainer<*> =
            PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("product_db")
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

    @Autowired
    lateinit var productRepository: ProductRepository

    val httpClient = HttpClient.newHttpClient()
    val baseUrl = "http://localhost"
}
