package pl.michalkowol

import com.softwareberg.JsonMapper
import com.softwareberg.SimpleHttpClient
import org.slf4j.LoggerFactory
import spark.Filter
import spark.Request
import spark.Response
import spark.Spark.*
import java.io.PrintWriter
import java.io.StringWriter
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_MOVED_TEMP

fun main(args: Array<String>) {
    val httpClient = SimpleHttpClient.create()
    val jsonMapper = JsonMapper.create()
    val hackerNews = HackerNews(httpClient, jsonMapper)
    Boot(jsonMapper, hackerNews).start()
}

class Boot(private val jsonMapper: JsonMapper, private val hackerNews: HackerNews) {

    private val log = LoggerFactory.getLogger(Boot::class.java)

    fun start() {
        port(assignedPort())

        before(authenticatedFilter)
        after(gzip)

        get("/news/top", this::top)
        get("/news/show", this::show)
        get("/news/ask", this::ask)
        get("/news/job", this::job)
        get("/news/:id", this::newsById)
        get("/login", this::login)
        get("/logout", this::logout)

        exception(Exception::class.java, { e, request, response ->
            log.error(request.url(), e)
            val errorMsgWriter = StringWriter()
            e.printStackTrace(PrintWriter(errorMsgWriter))
            val errorMsg = errorMsgWriter.toString()
            response.type("text/plain")
            response.status(HTTP_INTERNAL_ERROR)
            response.body(errorMsg)
        })
    }

    private val authenticatedFilter = Filter { request, response ->
        if (request.pathInfo() != "/login") {
            val session = request.session()
            val user: String? = session.attribute("user")
            if (user == null) {
                session.attribute("path", request.pathInfo())
                response.redirect("/login", HTTP_MOVED_TEMP)
            }
        }
    }

    private val gzip = Filter { request, response ->
        response.header("Content-Encoding", "gzip")
    }

    private fun login(request: Request, response: Response): Response {
        val user: String? = request.queryParams("name")
        if (user != null) {
            val session = request.session()
            session.attribute("user", user)
            val path = session.attribute("path") ?: "/"
            response.redirect(path, HTTP_MOVED_TEMP)
        } else {
            response.body("""<a href="/login?name=michal">Login</a>""")
        }
        return response
    }

    private fun logout(request: Request, response: Response): String {
        request.session().invalidate()
        return "logout"
    }

    private fun newsById(request: Request, response: Response): String {
        response.type("application/json")
        val id = request.params("id").toInt()
        val story = hackerNews.byId(id)
        return jsonMapper.write(story)
    }

    private fun top(request: Request, response: Response): String {
        response.type("application/json")
        return jsonMapper.write(hackerNews.topStories())
    }

    private fun ask(request: Request, response: Response): String {
        response.type("application/json")
        return jsonMapper.write(hackerNews.askStories())
    }

    private fun show(request: Request, response: Response): String {
        response.type("application/json")
        return jsonMapper.write(hackerNews.showStories())
    }

    private fun job(request: Request, response: Response): String {
        response.type("application/json")
        return jsonMapper.write(hackerNews.jobStories())
    }

    private fun assignedPort(): Int {
        val envs = System.getenv()
        return envs["PORT"]?.toInt() ?: 8080
    }
}
