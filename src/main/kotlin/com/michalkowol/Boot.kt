package com.michalkowol

import com.michalkowol.base.context
import com.michalkowol.base.web.HttpServer
import org.flywaydb.core.Flyway
import org.h2.tools.Server
import org.kodein.di.generic.instance

fun main(args: Array<String>) {
    Boot.start()
}

object Boot {

    fun start() {
        val h2DatabaseServer: Server by context.instance()
        val flyway: Flyway by context.instance()
        val httpServer: HttpServer by context.instance()

        h2DatabaseServer.start()
        flyway.migrate()
        httpServer.start()
    }
}
