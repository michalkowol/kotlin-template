package com.michalkowol.hackernews

import com.michalkowol.web.Controller
import com.softwareberg.JsonMapper
import spark.Request
import spark.Response
import spark.Spark.get

class HackerNewsController(
    private val hackerNewsService: HackerNewsService,
    private val jsonMapper: JsonMapper
) : Controller {

    override fun start() {
        get("/news", this::news, jsonMapper::write)
    }

    private fun news(request: Request, response: Response): HackerNews {
        response.type("application/json")
        return hackerNewsService.topStory()
    }
}
