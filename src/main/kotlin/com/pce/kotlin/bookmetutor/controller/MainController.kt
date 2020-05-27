package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.util.HandlesError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController : HandlesError() {

    @GetMapping("")
    fun welcome(): ResponseEntity<String> {
        return ResponseEntity("Welcome to BookMeTutor", HttpStatus.OK)
    }
}