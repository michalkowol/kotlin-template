package com.michalkowol

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.michalkowol.HttpServer.ServerConfiguration
import com.softwareberg.*
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.DefaultAsyncHttpClient
import org.flywaydb.core.Flyway
import org.h2.tools.Server
import java.io.File
import java.util.*
import javax.inject.Singleton
import javax.sql.DataSource

internal class HttpClientModule : AbstractModule() {
    override fun configure() {}

    @Singleton
    @Provides
    private fun provideAsyncHttpClient(): AsyncHttpClient {
        return DefaultAsyncHttpClient()
    }

    @Singleton
    @Provides
    private fun provideHttpClient(asyncHttpClient: AsyncHttpClient): HttpClient {
        return SimpleHttpClient(asyncHttpClient)
    }
}

internal class JsonXmlModule : AbstractModule() {
    override fun configure() {}

    @Singleton
    @Provides
    private fun provideJsonMapper(): JsonMapper {
        return JsonMapper.create()
    }

    @Singleton
    @Provides
    private fun provideXmlMapper(): XmlMapper {
        return XmlMapper.create()
    }
}

internal class DatabaseModule : AbstractModule() {
    override fun configure() {}


    @Singleton
    @Provides
    fun provideDataSource(config: Config): DataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = config.getString("datasource.jdbcUrl")
        hikariConfig.username = config.getString("datasource.username")
        hikariConfig.password = config.getString("datasource.password")
        return HikariDataSource(hikariConfig)
    }

    @Singleton
    @Provides
    private fun provideDatabase(dataSource: DataSource): Database {
        return Database(dataSource)
    }

    @Singleton
    @Provides
    private fun provideFlyway(dataSource: DataSource): Flyway {
        val flyway = Flyway()
        flyway.dataSource = dataSource
        return flyway
    }

    @Singleton
    @Provides
    private fun provideH2Database(): Server {
        return Server.createTcpServer()
    }
}

internal class ConfigModule : AbstractModule() {
    override fun configure() {}

    @Singleton
    @Provides
    private fun provideConfig(): Config {
        val stage = Optional.ofNullable(System.getProperty("environment"))
        val configurationFile = stage
            .map { status -> "application-$status.properties" }
            .orElse("application.conf")

        return ConfigFactory
            .parseFile(File("application.conf"))
            .withFallback(ConfigFactory.load(configurationFile))
            .withFallback(ConfigFactory.load())
    }
}

internal class HttpServerModule : AbstractModule() {
    override fun configure() {}

    @Singleton
    @Provides
    private fun provideServerConfiguration(config: Config): ServerConfiguration {
        val port = config.getInt("server.port")
        return ServerConfiguration(port)
    }
}
