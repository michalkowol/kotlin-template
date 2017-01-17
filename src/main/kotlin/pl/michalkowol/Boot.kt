package pl.michalkowol

import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.*

fun main(args: Array<String>) {
    Boot().start()
}

class Boot {

    private val log = LoggerFactory.getLogger(Boot::class.java)

    fun start() {
        port(assignedPort())
        get("/foo", this::foo)
        get("/bar/:id", this::bar)

        exception(Exception::class.java, { e, request, response ->
            log.error(request.url(), e)
        })
    }

    private fun foo(request: Request, response: Response): String {
        response.type("text/plain")
        return "foo"
    }

    private fun bar(request: Request, response: Response): String {
        response.type("application/json")
        val id = request.params(":id")
        return """{"id": $id}"""
    }

    fun assignedPort(): Int {
        val processBuilder = ProcessBuilder()
        if (processBuilder.environment()["PORT"] != null) {
            return Integer.parseInt(processBuilder.environment()["PORT"])
        }
        return 8080
    }
}
