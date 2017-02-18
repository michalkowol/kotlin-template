package pl.michalkowol

import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.*
import java.io.PrintWriter
import java.io.StringWriter

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
            val errorMsgWriter = StringWriter()
            e.printStackTrace(PrintWriter(errorMsgWriter))
            val errorMsg = errorMsgWriter.toString()
            response.type("text/plain")
            response.status(500)
            response.body(errorMsg)
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

    private fun assignedPort(): Int {
        val envs = System.getenv()
        return envs["PORT"]?.toInt() ?: 8080
    }
}
