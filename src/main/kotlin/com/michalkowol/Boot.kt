package com.michalkowol

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
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

fun main(args: Array<String>) {

    val kodein = Kodein {
        import(configModule)
        import(errorsControllerModule)
        import(httpClientModule)
        import(httpServerModule)
        import(hackerNewsModule)
        import(demoModule)
        import(carsModule)
        import(jsonXmlModule)
        import(databaseModule)
    }
    Boot(kodein).start()
}

class Boot(kodein: Kodein) {

    private val h2DatabaseServer: Server by kodein.instance()
    private val flyway: Flyway by kodein.instance()
    private val httpServer: HttpServer by kodein.instance()

    fun start() {
        h2DatabaseServer.start()
        flyway.migrate()
        httpServer.start()
    }
}
