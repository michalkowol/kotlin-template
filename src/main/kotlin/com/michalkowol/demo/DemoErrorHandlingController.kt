package com.michalkowol.demo

import com.michalkowol.web.Controller
import com.michalkowol.web.errors.NotFoundException
import com.softwareberg.JsonMapper
import spark.Request
import spark.Response
import spark.Spark.get
import java.util.Random

class DemoErrorHandlingController(private val jsonMapper: JsonMapper) : Controller {

    override fun start() {
        get("/errors", this::errors, jsonMapper::write)
    }

    private fun errors(request: Request, response: Response): Map<String, String> {
        response.type("application/json")
        val random = Random().nextInt(3)
        if (random == 0) {
            throw IllegalStateException("Random error")
        } else if (random == 1) {
            throw NotFoundException("Not found")
        }
        return mapOf("health" to "ok")
    }
}
