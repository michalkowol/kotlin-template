package com.michalkowol.base

import com.michalkowol.base.web.HealthController
import com.michalkowol.base.web.HttpServer
import com.michalkowol.base.web.ServerConfiguration
import com.michalkowol.base.web.errors.ErrorsController
import com.softwareberg.*
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.DefaultAsyncHttpClient
import org.flywaydb.core.Flyway
import org.h2.tools.Server
import org.kodein.di.Kodein
import org.kodein.di.generic.allInstances
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import javax.sql.DataSource

val httpClientModule = Kodein.Module("httpClientModule") {
    bind<AsyncHttpClient>() with singleton { DefaultAsyncHttpClient() }
    bind<HttpClient>() with singleton { SimpleHttpClient(instance()) }
}

val jsonXmlModule = Kodein.Module("jsonXmlModule") {
    bind<JsonMapper>() with singleton { JsonMapper.create() }
    bind<XmlMapper>() with singleton { XmlMapper.create() }
}

val databaseModule = Kodein.Module("databaseModule") {
    bind<DataSource>() with singleton {
        val config: Config = instance()
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = config.getString("datasource.jdbcUrl")
        hikariConfig.username = config.getString("datasource.username")
        hikariConfig.password = config.getString("datasource.password")
        HikariDataSource(hikariConfig)
    }
    bind<Database>() with singleton { Database(instance()) }
    bind<Flyway>() with singleton {
        val flyway = Flyway()
        flyway.dataSource = instance()
        flyway
    }
    bind<Server>() with singleton { Server.createTcpServer() }
}

val configModule = Kodein.Module("configModule") {
    bind<Config>() with singleton { ConfigFactory.load() }
}

val httpServerModule = Kodein.Module("httpServerModule") {
    bind<HttpServer>() with singleton { HttpServer(instance(), allInstances()) }
    bind<ServerConfiguration>() with singleton {
        val config: Config = instance()
        val port = config.getInt("server.port")
        ServerConfiguration(port)
    }
    bind<HealthController>() with singleton { HealthController(instance()) }
}

val errorsControllerModule = Kodein.Module("errorsControllerModule") {
    bind<ErrorsController>() with singleton { ErrorsController(instance()) }
}
