package com.michalkowol.base.web.errors

class NotFoundException(
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

class BadRequestException(
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
