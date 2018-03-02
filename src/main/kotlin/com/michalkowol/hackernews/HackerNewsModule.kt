package com.michalkowol.hackernews

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.softwareberg.HttpClient
import com.softwareberg.JsonMapper
import javax.inject.Singleton

class HackerNewsModule : AbstractModule() {

    override fun configure() {}

    @Singleton
    @Provides
    private fun provideHackerNewsService(httpClient: HttpClient, jsonMapper: JsonMapper): HackerNewsService {
        return HackerNewsService(httpClient, jsonMapper)
    }

    @Singleton
    @Provides
    private fun provideHackerNewsController(
        hackerNewsService: HackerNewsService,
        jsonMapper: JsonMapper
    ): HackerNewsController {
        return HackerNewsController(hackerNewsService, jsonMapper)
    }
}
