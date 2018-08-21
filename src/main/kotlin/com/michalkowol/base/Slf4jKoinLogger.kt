package com.michalkowol.base

import org.koin.core.Koin
import org.koin.log.Logger
import org.slf4j.LoggerFactory

class Slf4jKoinLogger : Logger {

    val log = LoggerFactory.getLogger(Koin::class.java)

    override fun debug(msg: String) {
        log.debug(msg)
    }

    override fun err(msg: String) {
        log.error(msg)
    }

    override fun info(msg: String) {
        log.info(msg)
    }
}
