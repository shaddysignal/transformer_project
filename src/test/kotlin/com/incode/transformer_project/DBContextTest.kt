package com.incode.transformer_project

import org.junit.ClassRule
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

class DBContextTest : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        postgreSQLContainer.start()
        postgreSQLContainer.setCommand("postgres", "-c", "fsync=off", "-c", "log_statement=all")
        TestPropertyValues.of(
            "spring.datasource.url=" + postgreSQLContainer.jdbcUrl,
            "spring.datasource.username=" + postgreSQLContainer.username,
            "spring.datasource.password=" + postgreSQLContainer.password,
            "spring.datasource.port=" + postgreSQLContainer.firstMappedPort
        ).applyTo(applicationContext.environment)
    }

    companion object {
        @JvmField
        @ClassRule
        val postgreSQLContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:15.3-alpine3.18")
            .withEnv("TZ", "Europe/Belgrade")
    }
}