package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dto.subject.CreateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.SubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.UpdateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.service.SubjectService
import com.pce.kotlin.bookmetutor.util.Constants
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/subject")
class SubjectController(val subjectService: SubjectService) {

    @GetMapping("/")
    fun getAllSubjects(): ResponseEntity<Response> {
        return ResponseEntity(Response(description = "list of subjects", payload = subjectService.retrieveAllSubjects()?.map { it.toDto() }
                ?: emptyList<SubjectDto>()), HttpStatus.OK)
    }

    @GetMapping("/")
    fun getSubject(@RequestParam classNumber: Int, @RequestParam subjectName: String): ResponseEntity<Response> {
        return ResponseEntity(Response(description = "subject information", payload = subjectService.retrieveSubject(classNumber, subjectName)), HttpStatus.OK)
    }

    @PostMapping("/")
    fun createSubject(@RequestBody request: CreateSubjectDto): ResponseEntity<Response> {
        return ResponseEntity(Response(description = "subject information", payload = subjectService.createSubject(request)), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun updateSubject(@PathVariable id: Long, @RequestBody update: UpdateSubjectDto): ResponseEntity<Response> {
        return ResponseEntity(Response(description = "subject information", payload = subjectService.updateSubject(id, update)), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteSubject(@PathVariable id: Long): ResponseEntity<Response> {
        subjectService.removeSubject(id)
        return ResponseEntity(Response(description = Constants.REQUEST_FULFILLED), HttpStatus.OK)
    }

}