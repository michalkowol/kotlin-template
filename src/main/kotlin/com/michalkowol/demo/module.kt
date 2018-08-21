package com.michalkowol.demo

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val demoModule = Kodein.Module("demoModule") {
    bind<DemoErrorHandlingController>() with singleton { DemoErrorHandlingController(instance()) }
    bind<DemoRedirectController>() with singleton { DemoRedirectController() }
}
