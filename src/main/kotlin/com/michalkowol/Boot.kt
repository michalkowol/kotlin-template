package com.michalkowol

import com.michalkowol.configurations.Configuration
import com.michalkowol.web.HttpServer
import org.flywaydb.core.Flyway
import org.h2.tools.Server

fun main(args: Array<String>) {
    Boot.start()
}

object Boot {

    fun start() {
        val injector = Configuration.injector

        val h2DatabaseServer = injector.getInstance(Server::class.java)
        val flyway = injector.getInstance(Flyway::class.java)
        val httpServer = injector.getInstance(HttpServer::class.java)

        h2DatabaseServer.start()
        flyway.migrate()
        httpServer.start()
    }
}
