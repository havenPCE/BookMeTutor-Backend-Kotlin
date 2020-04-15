package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dto.admin.CreateAdminDto
import com.pce.kotlin.bookmetutor.model.dto.student.CreateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.UpdateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.CreateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.util.AuthRequest
import com.pce.kotlin.bookmetutor.model.dto.util.ResetRequest
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.service.*
import com.pce.kotlin.bookmetutor.util.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(val studentService: StudentService, val tutorService: TutorService,
                        val adminService: AdminService, val accountService: AccountService,
                        val emailService: EmailService, val jwtTokenService: JwtTokenService, val authenticationManager: AuthenticationManager) : HandlesError() {

    companion object {
        val resetRequest: Map<String, String> = mutableMapOf()
    }

    @PostMapping("/register-admin")
    fun registerAdmin(@RequestBody dto: CreateAdminDto): ResponseEntity<out Response> {
        adminService.createAdmin(dto)?.let {
            return response(status = HttpStatus.OK, message = ADMIN_INFO, payload = it)
        }
        return response(status = HttpStatus.CONFLICT, message = ACCOUNT_EXISTS)
    }

    @PostMapping("/register-student")
    fun registerStudent(@RequestBody dto: CreateStudentDto): ResponseEntity<out Response> {
        studentService.createStudent(dto)?.let {
            val token: String = jwtTokenService.generate(it.email)
            val (subject, text) = makeVerifyRequest(it.email, token, Authority.STUDENT)
            emailService.sendMail(it.email, subject, text)
            return response(HttpStatus.OK, message = ACCOUNT_CREATED)
        }
        return response(status = HttpStatus.CONFLICT, message = ACCOUNT_EXISTS)
    }

    @PostMapping("/register-tutor")
    fun registerTutor(@RequestBody dto: CreateTutorDto): ResponseEntity<out Response> {
        tutorService.createTutor(dto)?.let {
            val token: String = jwtTokenService.generate(it.email)
            val (subject, text) = makeVerifyRequest(it.email, token, Authority.TUTOR)
            emailService.sendMail(it.email, subject, text)
            return response(status = HttpStatus.OK, message = ACCOUNT_CREATED)
        }
        return response(status = HttpStatus.CONFLICT, message = ACCOUNT_EXISTS)
    }

    @GetMapping("/verify")
    fun verify(@RequestParam email: String, @RequestParam jwt: String, @RequestParam role: String): ResponseEntity<String> {
        val userName: String? = jwtTokenService.retrieveUserNameFromToken(jwt)
        userName?.let {
            return if (userName == email) {
                when (role) {
                    Authority.STUDENT.name -> {
                        studentService.updateStudent(email, UpdateStudentDto(verified = true))?.let {
                            ResponseEntity(ACCOUNT_VERIFIED.h4(), HttpStatus.OK)
                        } ?: ResponseEntity(STUDENT_NOT_FOUND.h4(), HttpStatus.NOT_FOUND)
                    }
                    Authority.TUTOR.name -> {
                        tutorService.updateTutor(email, UpdateTutorDto(verified = true))?.let {
                            ResponseEntity(ACCOUNT_VERIFIED.h4(), HttpStatus.OK)
                        } ?: ResponseEntity(TUTOR_NOT_FOUND.h4(), HttpStatus.NOT_FOUND)
                    }
                    else -> ResponseEntity(INVALID_REQUEST.h4(), HttpStatus.BAD_REQUEST)
                }
            } else ResponseEntity(INVALID_TOKEN.h4(), HttpStatus.UNAUTHORIZED)
        }
        return ResponseEntity(INVALID_TOKEN.h4(), HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody dto: AuthRequest): ResponseEntity<out Response> {
        return try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(dto.email, dto.password))
            val token: String = jwtTokenService.generate(dto.email)
            response(status = HttpStatus.OK, message = TOKEN_GENERATED, payload = token)
        } catch (e: BadCredentialsException) {
            response(status = HttpStatus.UNAUTHORIZED, message = INVALID_CREDENTIALS)
        } catch (e: DisabledException) {
            response(status = HttpStatus.UNAUTHORIZED, message = NOT_VERIFIED)
        }
    }

    @PostMapping("/forgot")
    fun forgotRequest(@RequestBody dto: ResetRequest): ResponseEntity<out Response> {
        return when (dto.role) {
            Authority.STUDENT.name -> {
                studentService.retrieveStudent(dto.email)?.let {
                    addResetRequest(dto.email, dto.password, Authority.valueOf(dto.role))
                } ?: response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
            }
            Authority.TUTOR.name -> {
                tutorService.retrieveTutor(dto.email)?.let {
                    addResetRequest(dto.email, dto.password, Authority.valueOf(dto.role))
                } ?: response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
            }
            else -> response(status = HttpStatus.BAD_REQUEST, message = INVALID_REQUEST)
        }
    }

    @GetMapping("/reset")
    fun resetPassword(@RequestParam email: String, @RequestParam jwt: String, @RequestParam role: String): ResponseEntity<String> {
        val userName = jwtTokenService.retrieveUserNameFromToken(jwt)
        userName?.let {
            return if (userName == email) {
                when (role) {
                    Authority.STUDENT.name -> {
                        studentService.updateStudent(email, UpdateStudentDto(password = resetRequest[email]))?.let {
                            ResponseEntity(TASK_SUCCESSFUL.h4(), HttpStatus.OK)
                        } ?: ResponseEntity(STUDENT_NOT_FOUND.h4(), HttpStatus.NOT_FOUND)
                    }
                    Authority.TUTOR.name -> {
                        tutorService.updateTutor(email, UpdateTutorDto(password = resetRequest[email]))?.let {
                            ResponseEntity(TASK_SUCCESSFUL.h4(), HttpStatus.OK)
                        } ?: ResponseEntity(TUTOR_NOT_FOUND.h4(), HttpStatus.NOT_FOUND)
                    }
                    else -> ResponseEntity(INVALID_REQUEST.h4(), HttpStatus.BAD_REQUEST)
                }
            } else ResponseEntity(INVALID_TOKEN.h4(), HttpStatus.UNAUTHORIZED)
        }
        return ResponseEntity(INVALID_TOKEN.h4(), HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("/available")
    fun isAvailable(@RequestParam email: String): ResponseEntity<out Response> {
        return response(status = HttpStatus.OK, message = AVAILABILITY_DESC, payload = !accountService.isPresent(email))
    }

    fun addResetRequest(email: String, password: String, role: Authority): ResponseEntity<out Response> {
        resetRequest.plus(Pair(email, password))
        val token = jwtTokenService.generate(email)
        val (subject, text) = makeResetRequest(email, token, role)
        emailService.sendMail(email, subject, text)
        return response(status = HttpStatus.OK, message = TASK_SUCCESSFUL)
    }

}