package pl.michalkowol

import com.softwareberg.HttpClient
import com.softwareberg.HttpMethod
import com.softwareberg.HttpRequest
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.future.await

private data class SingleHackerNewsResp(val id: Int, val score: Int, val time: Int, val title: String, val text: String?, val url: String?, val type: String)
data class SingleHackerNews(val id: Int, val title: String, val url: String?, val href: String)
data class JobSingleHackerNews(val id: Int, val title: String, val text: String?, val url: String?, val href: String)

class HackerNews(private val httpClient: HttpClient, private val jsonMapper: JsonMapper) {

    fun stories(ids: List<Int>): Deferred<List<SingleHackerNews>> = async(CommonPool) {
        val resp = storiesResp(ids)
        resp.await().map { SingleHackerNews(it.id, it.title, it.url, "https://news.ycombinator.com/item?id=${it.id}") }
    }

    fun byId(id: Int): Deferred<SingleHackerNews> = async(CommonPool) {
        val story = httpClient.execute(HttpRequest(HttpMethod.GET, "https://hacker-news.firebaseio.com/v0/item/$id.json?print=pretty"))
        val resp = jsonMapper.read<SingleHackerNewsResp>(story.await().body.orEmpty())
        SingleHackerNews(resp.id, resp.title, resp.url, "https://news.ycombinator.com/item?id=${resp.id}")
    }

    fun askStories(): Deferred<List<SingleHackerNews>> = async(CommonPool) {
        val topIds = ids("https://hacker-news.firebaseio.com/v0/askstories.json")
        stories(topIds.await()).await()
    }

    fun showStories(): Deferred<List<SingleHackerNews>> = async(CommonPool) {
        val topIds = ids("https://hacker-news.firebaseio.com/v0/showstories.json")
        stories(topIds.await()).await()
    }

    fun jobStories(): Deferred<List<JobSingleHackerNews>> = async(CommonPool) {
        val topIds = ids("https://hacker-news.firebaseio.com/v0/jobstories.json")
        val resp = storiesResp(topIds.await())
        resp.await().map { JobSingleHackerNews(it.id, it.title, it.text, it.url, "https://news.ycombinator.com/item?id=${it.id}") }
    }

    fun topStories(): Deferred<List<SingleHackerNews>> = async(CommonPool) {
        val topIds = ids("https://hacker-news.firebaseio.com/v0/topstories.json")
        stories(topIds.await()).await()
    }

    private fun ids(url: String): Deferred<List<Int>> = async(CommonPool) {
        val topStories = httpClient.execute(HttpRequest(HttpMethod.GET, url))
        jsonMapper.read<List<Int>>(topStories.await().body.orEmpty()).subList(0, 10)
    }

    private fun storiesResp(ids: List<Int>): Deferred<List<SingleHackerNewsResp>> = async(CommonPool) {
        val storiesJson = ids.map { id -> httpClient.execute(HttpRequest(HttpMethod.GET, "https://hacker-news.firebaseio.com/v0/item/$id.json?print=pretty")) }
        storiesJson.map { storyJson -> jsonMapper.read<SingleHackerNewsResp>(storyJson.await().body.orEmpty()) }
    }
}
