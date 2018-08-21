package com.michalkowol.hackernews

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val hackerNewsModule = Kodein.Module("hackerNewsModule") {
    bind<HackerNewsService>() with singleton { HackerNewsService(instance(), instance()) }
    bind<HackerNewsController>() with singleton { HackerNewsController(instance(), instance()) }
}
