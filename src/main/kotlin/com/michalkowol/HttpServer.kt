package com.michalkowol

import com.google.common.collect.ImmutableMap
import com.michalkowol.cars.CarsController
import com.michalkowol.hackernews.HackerNewsController
import com.softwareberg.JsonMapper
import spark.Request
import spark.Response
import spark.Spark.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HttpServer @Inject constructor(private val serverConfiguration: ServerConfiguration, private val jsonMapper: JsonMapper, private val errorsController: ErrorsController, private val hackerNewsController: HackerNewsController, private val carsController: CarsController) {

    fun start() {
        port(serverConfiguration.port)

        staticFiles.location("/public")
        redirect.get("/redirect", "/health")
        get("/health", this::health)
        get("/errors", this::errors, jsonMapper::write)

        errorsController.start()
        carsController.start()
        hackerNewsController.start()
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

    internal data class ServerConfiguration(val port: Int)
}
