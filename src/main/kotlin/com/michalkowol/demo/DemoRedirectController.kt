package com.michalkowol.demo

import com.michalkowol.base.web.Controller
import spark.Spark.redirect

class DemoRedirectController : Controller {

    override fun start() {
        redirect.get("/redirect", "/health")
    }
}
