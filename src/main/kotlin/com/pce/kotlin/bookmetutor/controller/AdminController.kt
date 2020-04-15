package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dto.admin.AdminDto
import com.pce.kotlin.bookmetutor.model.dto.admin.UpdateAdminDto
import com.pce.kotlin.bookmetutor.model.dto.student.StudentDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.TutorDto
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.service.AdminService
import com.pce.kotlin.bookmetutor.service.StudentService
import com.pce.kotlin.bookmetutor.service.TutorService
import com.pce.kotlin.bookmetutor.util.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admins")
class AdminController(val adminService: AdminService, val tutorService: TutorService, val studentService: StudentService) : HandlesError() {

    @GetMapping("")
    fun retrieveAllAdmin(): ResponseEntity<out Response> {
        val admins: List<AdminDto> = adminService.retrieveAllAdmin()
        return response(status = HttpStatus.OK, message = ADMIN_INFO, payload = admins)
    }

    @GetMapping("/{email}")
    fun retrieveAdmin(@PathVariable email: String): ResponseEntity<out Response> {
        adminService.retrieveAdmin(email)?.let {
            return response(status = HttpStatus.OK, message = ADMIN_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = ADMIN_NOT_FOUND)
    }

    @GetMapping("/tutors")
    fun retrieveAllTutors(): ResponseEntity<out Response> {
        val tutors: List<TutorDto> = tutorService.retrieveAllTutors()
        return response(status = HttpStatus.OK, message = TUTOR_INFO, payload = tutors)
    }

    @GetMapping("/tutors/{email}")
    fun retrieveTutor(@PathVariable email: String): ResponseEntity<out Response> {
        tutorService.retrieveTutor(email)?.let {
            return response(status = HttpStatus.OK, message = TUTOR_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }

    @GetMapping("/students")
    fun retrieveAllStudents(): ResponseEntity<out Response> {
        val students: List<StudentDto> = studentService.retrieveAllStudents()
        return response(status = HttpStatus.OK, message = STUDENT_INFO, payload = students)
    }

    @GetMapping("/students/{email}")
    fun retrieveStudent(@PathVariable email: String): ResponseEntity<out Response> {
        studentService.retrieveStudent(email)?.let {
            return response(status = HttpStatus.OK, message = STUDENT_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

    @PutMapping("/{email}")
    fun updateAdmin(@PathVariable email: String, @RequestBody dto: UpdateAdminDto): ResponseEntity<out Response> {
        adminService.updateAdmin(email, dto)?.let {
            return response(status = HttpStatus.OK, message = ADMIN_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = ADMIN_NOT_FOUND)
    }

    @DeleteMapping("/{email}")
    fun removeAdmin(@PathVariable email: String): ResponseEntity<out Response> {
        return if (adminService.removeAdmin(email)) {
            response(status = HttpStatus.OK, message = TASK_SUCCESSFUL)
        } else response(status = HttpStatus.NOT_FOUND, message = ADMIN_NOT_FOUND)
    }

    @DeleteMapping("/{adminEmail}/students/{studentEmail}")
    fun removeStudent(@PathVariable adminEmail: String, @PathVariable studentEmail: String): ResponseEntity<out Response> {
        return if (studentService.removeStudent(studentEmail)) {
            response(status = HttpStatus.OK, message = TASK_SUCCESSFUL)
        } else response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

    @DeleteMapping("/{adminEmail}/tutors/{tutorEmail}")
    fun removeTutor(@PathVariable adminEmail: String, @PathVariable tutorEmail: String): ResponseEntity<out Response> {
        return if (tutorService.removeTutor(tutorEmail)) {
            response(status = HttpStatus.OK, message = TASK_SUCCESSFUL)
        } else response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }
}