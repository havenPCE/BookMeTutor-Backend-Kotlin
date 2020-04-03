package com.pce.kotlin.bookmetutor.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @GetMapping("/")
    @ResponseBody
    fun hello() = "<h2>Welcome To BookMeTutor Backend Server</h2>"

}