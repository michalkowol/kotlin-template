package com.michalkowol

import com.google.inject.Guice
import com.google.inject.Injector
import org.flywaydb.core.Flyway
import org.h2.tools.Server

object Boot {

    @JvmStatic
    fun main(args: Array<String>) {
        val injector = configure()
        val h2DatabaseServer = injector.getInstance(Server::class.java)
        val flyway = injector.getInstance(Flyway::class.java)
        val httpServer = injector.getInstance(HttpServer::class.java)

        h2DatabaseServer.start()
        flyway.migrate()
        httpServer.start()
    }

    private fun configure(): Injector {
        return Guice.createInjector(
            ConfigModule(),
            HttpClientModule(),
            HttpServerModule(),
            JsonXmlModule(),
            DatabaseModule()
        )
    }
}
