package com.ssd.product_api

import com.ssd.persistance.repository.ProductRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.DockerComposeContainer
import java.io.File
import java.net.http.HttpClient
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseIT {

    init {
        System.setProperty("SA_DB_PASSWORD", "service_account_password")
        System.setProperty("FLYWAY_DB_PASSWORD", "product_api_pwd")
    }

    companion object {

        private lateinit var postgresContainer: KDockerComposeContainer

        class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)

        private fun defineDockerCompose(): KDockerComposeContainer {
            val userDir = System.getProperty("user.dir")
            println("Project root directory: $userDir")
            val localFolder: Path = Paths.get(userDir, "local")
            val dockerComposeFile: Path =
                localFolder.resolve("docker-compose.yml")

            if (Files.notExists(dockerComposeFile)) {
                throw IllegalStateException("docker-compose.yml not found at: ${dockerComposeFile.toAbsolutePath()}")
            }

            return KDockerComposeContainer(dockerComposeFile.toFile())
                .withExposedService("postgres_1", 5432)
        }

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            postgresContainer = defineDockerCompose()
            println("starting pg..")
            postgresContainer.start()
            println("pg started")
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            postgresContainer.stop()
        }

        @DirtiesContext
        @DynamicPropertySource
        @JvmStatic
        fun registerDBContainer(registry: DynamicPropertyRegistry) {

            val postgresHost = postgresContainer.getServiceHost("postgres", 5432)
            val postgresPort = postgresContainer.getServicePort("postgres", 5432)

            val jdbcUrl = "jdbc:postgresql://$postgresHost:$postgresPort/product_db"

            println("Registering the jdbcUrl: $jdbcUrl")

            registry.add("spring.datasource.url") { jdbcUrl }
            registry.add("spring.flyway.url") { jdbcUrl }
        }
    }

    @Autowired
    lateinit var productRepository: ProductRepository

    val httpClient = HttpClient.newHttpClient()
    val baseUrl = "http://localhost"
}


