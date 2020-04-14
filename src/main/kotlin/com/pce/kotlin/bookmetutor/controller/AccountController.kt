package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dto.admin.CreateAdminDto
import com.pce.kotlin.bookmetutor.model.dto.student.CreateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.CreateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.service.AccountService
import com.pce.kotlin.bookmetutor.service.AdminService
import com.pce.kotlin.bookmetutor.service.StudentService
import com.pce.kotlin.bookmetutor.service.TutorService
import com.pce.kotlin.bookmetutor.util.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(val studentService: StudentService, val tutorService: TutorService, val adminService: AdminService, val accountService: AccountService) : HandlesError() {

    @PostMapping("/register-admin")
    fun registerAdmin(@RequestBody dto: CreateAdminDto): ResponseEntity<out Response> {
        adminService.createAdmin(dto)?.let {
            return response(status = HttpStatus.OK, message = ADMIN_INFO, payload = it.toDto())
        }
        return response(status = HttpStatus.CONFLICT, message = ACCOUNT_EXISTS)
    }

    @PostMapping("/register-student")
    fun registerStudent(@RequestBody dto: CreateStudentDto): ResponseEntity<out Response> {
        studentService.createStudent(dto)?.let {
            return response(HttpStatus.OK, message = ACCOUNT_CREATED)
        }
        return response(status = HttpStatus.CONFLICT, message = ACCOUNT_EXISTS)
    }

    @PostMapping("/register-tutor")
    fun registerTutor(@RequestBody dto: CreateTutorDto): ResponseEntity<out Response> {
        tutorService.createTutor(dto)?.let {
            return response(status = HttpStatus.OK, message = ACCOUNT_CREATED)
        }
        return response(status = HttpStatus.CONFLICT, message = ACCOUNT_EXISTS)
    }

    @GetMapping("/available")
    fun isAvailable(@RequestParam email: String): ResponseEntity<out Response> {
        return response(status = HttpStatus.OK, message = AVAILABILITY_DESC, payload = !accountService.isPresent(email))
    }

}