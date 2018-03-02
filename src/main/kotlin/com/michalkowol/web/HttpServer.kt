package com.michalkowol.web

import com.google.common.collect.ImmutableMap
import com.michalkowol.cars.CarsController
import com.michalkowol.configurations.ServerConfiguration
import com.michalkowol.hackernews.HackerNewsController
import com.michalkowol.web.errors.ErrorsController
import com.michalkowol.web.errors.NotFoundException
import com.softwareberg.JsonMapper
import spark.Request
import spark.Response
import spark.Spark.get
import spark.Spark.port
import spark.Spark.redirect
import java.util.Random

class HttpServer(
    private val serverConfiguration: ServerConfiguration,
    private val jsonMapper: JsonMapper,
    private val errorsController: ErrorsController,
    private val staticFilesController: StaticFilesController,
    private val hackerNewsController: HackerNewsController,
    private val carsController: CarsController
) {

    fun start() {
        port(serverConfiguration.port)
        staticFilesController.start()
        errorsController.start()
        carsController.start()
        hackerNewsController.start()

        redirect.get("/redirect", "/health")
        get("/health", this::health)
        get("/errors", this::errors, jsonMapper::write)
    }

    private fun health(request: Request, response: Response): String {
        response.type("application/json")
        return """{"health": "ok"}"""
    }

    private fun errors(request: Request, response: Response): Map<String, String> {
        response.type("application/json")
        val random = Random().nextInt(3)
        if (random == 0) {
            throw IllegalStateException("Random error")
        } else if (random == 1) {
            throw NotFoundException("Not found")
        }
        return ImmutableMap.of("health", "ok")
    }
}
