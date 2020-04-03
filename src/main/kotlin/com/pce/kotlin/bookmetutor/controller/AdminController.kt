package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dto.student.StudentDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.TutorDto
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.service.StudentService
import com.pce.kotlin.bookmetutor.service.TutorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bmt-admin")
class AdminController(val studentService: StudentService, val tutorService: TutorService) {
    @GetMapping("/student")
    fun getAllStudents(): ResponseEntity<Response> {
        val students: List<StudentDto> = (studentService.retrieveAllStudents()?.mapNotNull { it.toDto() }
                ?: emptyList())
        return ResponseEntity(Response(description = "list of students", payload = students), HttpStatus.OK)
    }

    @GetMapping("/tutor")
    fun getAllTutors(): ResponseEntity<Response> {
        val tutors: List<TutorDto> = (tutorService.retrieveAllTutors()?.mapNotNull { it.toDto() }
                ?: emptyList())
        return ResponseEntity(Response(description = "list of tutors", payload = tutors), HttpStatus.OK)
    }
}