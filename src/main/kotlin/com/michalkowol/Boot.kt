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

    private val h2DatabaseServer: Server by context.instance()
    private val flyway: Flyway by context.instance()
    private val httpServer: HttpServer by context.instance()

    fun start() {
        h2DatabaseServer.start()
        flyway.migrate()
        httpServer.start()
    }
}
