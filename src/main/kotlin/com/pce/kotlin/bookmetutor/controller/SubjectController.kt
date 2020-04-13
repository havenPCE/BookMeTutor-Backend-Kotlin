package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dto.subject.CreateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.SubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.UpdateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.service.SubjectService
import com.pce.kotlin.bookmetutor.util.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/subjects")
class SubjectController(val subjectService: SubjectService) {

    @PostMapping("")
    fun createSubject(@RequestBody dto: CreateSubjectDto): ResponseEntity<Response> {
        subjectService.createSubject(dto)?.let {
            return response(status = HttpStatus.OK, description = SUBJECT_INFO, payload = it.toDto())
        }
        return response(status = HttpStatus.INTERNAL_SERVER_ERROR, description = TASK_FAILED)
    }

    @GetMapping("")
    fun retrieveAllSubject(): ResponseEntity<Response> {
        val subjects: List<SubjectDto> = subjectService.retrieveAllSubjects().map { it.toDto() }
        return response(status = HttpStatus.OK, description = SUBJECT_INFO, payload = subjects)
    }

    @GetMapping(params = ["classNumber", "subjectName"])
    fun retrieveSubject(@RequestParam classNumber: Int, @RequestParam subjectName: String): ResponseEntity<Response> {
        subjectService.retrieveSubject(classNumber, subjectName)?.let {
            return response(status = HttpStatus.OK, description = SUBJECT_INFO, payload = it.toDto())
        }
        return response(status = HttpStatus.NOT_FOUND, description = SUBJECT_NOT_FOUND)
    }

    @PutMapping("/{id}")
    fun updateSubject(@PathVariable id: Long, @RequestBody dto: UpdateSubjectDto): ResponseEntity<Response> {
        subjectService.updateSubject(id, dto)?.let {
            return response(status = HttpStatus.OK, description = SUBJECT_INFO, payload = it.toDto())
        }
        return response(status = HttpStatus.NOT_FOUND, description = SUBJECT_NOT_FOUND)
    }

    @DeleteMapping("/{id}")
    fun removeSubject(@PathVariable id: Long): ResponseEntity<Response> {
        return if (subjectService.removeSubject(id)) {
            response(status = HttpStatus.OK, description = TASK_SUCCESSFUL)
        } else response(status = HttpStatus.NOT_FOUND, description = SUBJECT_NOT_FOUND)
    }

}