package com.michalkowol

import com.michalkowol.base.Slf4jKoinLogger
import com.michalkowol.cars.carsModule
import com.michalkowol.configurations.configModule
import com.michalkowol.configurations.databaseModule
import com.michalkowol.configurations.errorsControllerModule
import com.michalkowol.configurations.httpClientModule
import com.michalkowol.configurations.httpServerModule
import com.michalkowol.configurations.jsonXmlModule
import com.michalkowol.demo.demoModule
import com.michalkowol.hackernews.hackerNewsModule
import com.michalkowol.web.HttpServer
import org.flywaydb.core.Flyway
import org.h2.tools.Server
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.get

fun main(args: Array<String>) {

    startKoin(listOf(
        configModule,
        errorsControllerModule,
        httpClientModule,
        httpServerModule,
        hackerNewsModule,
        demoModule,
        carsModule,
        jsonXmlModule,
        databaseModule
    ), logger = Slf4jKoinLogger())
    Boot().start()
}

class Boot : KoinComponent {

    private val h2DatabaseServer: Server = get()
    private val flyway: Flyway = get()
    private val httpServer: HttpServer = get()

    fun start() {
        h2DatabaseServer.start()
        flyway.migrate()
        httpServer.start()
    }
}
