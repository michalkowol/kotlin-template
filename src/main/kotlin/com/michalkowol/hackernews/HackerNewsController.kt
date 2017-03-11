package com.michalkowol.hackernews

import com.softwareberg.JsonMapper
import spark.Request
import spark.Response
import spark.Spark.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HackerNewsController @Inject constructor(private val hackerNewsService: HackerNewsService, private val jsonMapper: JsonMapper) {

    fun start() {
        get("/news", this::news, jsonMapper::write)
    }

    private fun news(request: Request, response: Response): HackerNews {
        response.type("application/json")
        return hackerNewsService.topStory()
    }
}
