package com.michalkowol

import com.softwareberg.JsonMapper
import org.slf4j.LoggerFactory
import spark.Spark.exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ErrorsController @Inject constructor(private val jsonMapper: JsonMapper) {

    private val log = LoggerFactory.getLogger(ErrorsController::class.java)

    fun start() {
        exception(NotFoundException::class.java) { ex, request, response ->
            log.info(request.url(), ex)
            response.type("application/json")
            val notFound = NotFound(ex.message.orEmpty())
            response.status(notFound.status)
            response.body(jsonMapper.write(notFound))
        }

        exception(BadRequestException::class.java) { ex, request, response ->
            log.info(request.url(), ex)
            response.type("application/json")
            val badRequest = BadRequest(ex.message.orEmpty())
            response.status(badRequest.status)
            response.body(jsonMapper.write(badRequest))
        }

        exception(Exception::class.java) { ex, request, response ->
            log.error(request.url(), ex)
            response.type("application/json")
            val internalServerError = InternalServerError.create(ex)
            response.status(internalServerError.status)
            response.body(jsonMapper.write(internalServerError))
        }
    }
}