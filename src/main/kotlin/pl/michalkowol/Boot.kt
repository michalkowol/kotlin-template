package pl.michalkowol

import com.softwareberg.SimpleHttpClient
import kotlinx.coroutines.experimental.runBlocking
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark.*
import java.io.PrintWriter
import java.io.StringWriter

fun main(args: Array<String>) {
    val httpClient = SimpleHttpClient.create()
    val jsonMapper = JsonMapper()
    val hackerNews = HackerNews(httpClient, jsonMapper)
    Boot(jsonMapper, hackerNews).start()
}

class Boot(private val jsonMapper: JsonMapper, private val hackerNews: HackerNews) {

    private val log = LoggerFactory.getLogger(Boot::class.java)

    fun start() {
        port(assignedPort())
        get("/news/top", this::top)
        get("/news/show", this::show)
        get("/news/ask", this::ask)
        get("/news/job", this::job)
        get("/news/:id", this::newsById)

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

    private fun newsById(request: Request, response: Response): String {
        response.type("application/json")
        val id = request.params(":id").toInt()
        val story = runBlocking { hackerNews.byId(id).await() }
        return jsonMapper.write(story)
    }

    private fun top(request: Request, response: Response): String {
        response.type("application/json")
        val topStories = runBlocking { hackerNews.topStories().await() }
        return jsonMapper.write(topStories)
    }

    private fun ask(request: Request, response: Response): String {
        response.type("application/json")
        val topStories = runBlocking { hackerNews.askStories().await() }
        return jsonMapper.write(topStories)
    }

    private fun show(request: Request, response: Response): String {
        response.type("application/json")
        val topStories = runBlocking { hackerNews.showStories().await() }
        return jsonMapper.write(topStories)
    }

    private fun job(request: Request, response: Response): String {
        response.type("application/json")
        val topStories = runBlocking { hackerNews.jobStories().await() }
        return jsonMapper.write(topStories)
    }

    private fun assignedPort(): Int {
        val envs = System.getenv()
        return envs["PORT"]?.toInt() ?: 8080
    }
}
