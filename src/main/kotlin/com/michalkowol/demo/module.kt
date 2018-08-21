package com.michalkowol.demo

import org.koin.dsl.module.module

val demoModule = module {
    single { DemoErrorHandlingController(get()) }
    single { DemoRedirectController() }
}
