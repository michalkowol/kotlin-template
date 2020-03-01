package com.michalkowol.base.web

import com.softwareberg.JsonMapper
import spark.Request
import spark.Response
import spark.Spark.get

@Suppress("UnusedPrivateMember")
class HealthController(private val jsonMapper: JsonMapper) : Controller {

    override fun start() {
        get("/health", this::health, jsonMapper::write)
    }

    private fun health(request: Request, response: Response): Map<String, String> {
        response.type("application/json")
        return mapOf("health" to "ok")
    }
}
