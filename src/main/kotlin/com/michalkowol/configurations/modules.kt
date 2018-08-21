package com.michalkowol.configurations

import com.michalkowol.web.HealthController
import com.michalkowol.web.HttpServer
import com.michalkowol.web.errors.ErrorsController
import com.softwareberg.Database
import com.softwareberg.HttpClient
import com.softwareberg.JsonMapper
import com.softwareberg.SimpleHttpClient
import com.softwareberg.XmlMapper
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.DefaultAsyncHttpClient
import org.flywaydb.core.Flyway
import org.h2.tools.Server
import org.koin.dsl.module.module
import javax.sql.DataSource

val httpClientModule = module {
    single { DefaultAsyncHttpClient() as AsyncHttpClient }
    single { SimpleHttpClient(get()) as HttpClient }
}

val jsonXmlModule = module {
    single { JsonMapper.create() }
    single { XmlMapper.create() }
}

val databaseModule = module {
    single {
        val config: Config = get()
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = config.getString("datasource.jdbcUrl")
        hikariConfig.username = config.getString("datasource.username")
        hikariConfig.password = config.getString("datasource.password")
        HikariDataSource(hikariConfig) as DataSource
    }
    single { Database(get()) }
    single {
        val flyway = Flyway()
        flyway.dataSource = get()
        flyway
    }
    single { Server.createTcpServer() as Server }
}

val configModule = module {
    single { ConfigFactory.load() }
}

val httpServerModule = module {
    single { HttpServer(get(), get(), get(), get(), get(), get(), get()) }
    single {
        val config: Config = get()
        val port = config.getInt("server.port")
        ServerConfiguration(port)
    }
    single { HealthController(get()) }
}

val errorsControllerModule = module {
    single { ErrorsController(get()) }
}
