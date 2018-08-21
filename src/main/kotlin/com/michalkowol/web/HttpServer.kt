package com.michalkowol.web

import com.michalkowol.cars.CarsController
import com.michalkowol.configurations.ServerConfiguration
import com.michalkowol.demo.DemoErrorHandlingController
import com.michalkowol.demo.DemoRedirectController
import com.michalkowol.hackernews.HackerNewsController
import com.michalkowol.web.errors.ErrorsController
import spark.Spark.port
import spark.Spark.staticFiles

class HttpServer(
    private val serverConfiguration: ServerConfiguration,
    private val healthController: HealthController,
    private val errorsController: ErrorsController,
    private val hackerNewsController: HackerNewsController,
    private val carsController: CarsController,
    private val demoErrorHandlingController: DemoErrorHandlingController,
    private val demoRedirectController: DemoRedirectController
) {

    fun start() {
        port(serverConfiguration.port)
        staticFiles.location("/public")
        start(
            healthController,
            errorsController,
            carsController,
            hackerNewsController,
            demoErrorHandlingController,
            demoRedirectController
        )
    }

    private fun start(vararg controllers: Controller) {
        controllers.forEach { it.start() }
    }
}
