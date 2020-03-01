package com.michalkowol.hackernews

import com.softwareberg.HttpClient
import com.softwareberg.HttpMethod.GET
import com.softwareberg.HttpRequest
import com.softwareberg.JsonMapper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.future.await

@Suppress("UseCheckOrError", "GlobalCoroutineUsage", "WildcardImport")
class HackerNewsService(
    private val httpClient: HttpClient,
    private val jsonMapper: JsonMapper
) {

    private fun topStoriesAsync(): Deferred<List<Int>> = GlobalScope.async {
        val response = httpClient.execute(HttpRequest(GET, "https://hacker-news.firebaseio.com/v0/topstories.json"))
        val body = response.await().body ?: throw IllegalStateException("empty hackernews list")
        jsonMapper.read<List<Int>>(body)
    }

    private fun storyByIdAsync(id: Int): Deferred<HackerNews> = GlobalScope.async {
        val response = httpClient.execute(HttpRequest(GET, "https://hacker-news.firebaseio.com/v0/item/$id.json"))
        val body = response.await().body ?: throw IllegalStateException("empty hackernews")
        jsonMapper.read<HackerNews>(body)
    }

    fun topStory(): HackerNews = runBlocking {
        val topStoryId = topStoriesAsync().await().first()
        storyByIdAsync(topStoryId).await()
    }
}
