package com.michalkowol.web

import com.michalkowol.configurations.ServerConfiguration
import spark.Spark.port
import spark.Spark.staticFiles

class HttpServer(
    private val serverConfiguration: ServerConfiguration,
    private val controllers: List<Controller>
) {

    fun start() {
        port(serverConfiguration.port)
        staticFiles.location("/public")
        controllers.forEach { it.start() }
    }
}
