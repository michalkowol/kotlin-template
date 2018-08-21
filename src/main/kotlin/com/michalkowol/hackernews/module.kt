package com.michalkowol.hackernews

import org.koin.dsl.module.module

val hackerNewsModule = module {
    single { HackerNewsService(get(), get()) }
    single { HackerNewsController(get(), get()) }
}
