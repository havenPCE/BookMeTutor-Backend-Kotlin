package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.model.dto.admin.CreateAdminDto
import com.pce.kotlin.bookmetutor.model.dto.student.CreateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.UpdateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.CreateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.util.AuthRequest
import com.pce.kotlin.bookmetutor.model.dto.util.ResetRequest
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.repository.AdminRepo
import com.pce.kotlin.bookmetutor.service.EmailService
import com.pce.kotlin.bookmetutor.service.JwtTokenService
import com.pce.kotlin.bookmetutor.service.StudentService
import com.pce.kotlin.bookmetutor.service.TutorService
import com.pce.kotlin.bookmetutor.util.Authority
import com.pce.kotlin.bookmetutor.util.Constants
import com.pce.kotlin.bookmetutor.util.makeResetRequest
import com.pce.kotlin.bookmetutor.util.makeVerifyRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(private val authenticationManager: AuthenticationManager,
                        private val jwtTokenService: JwtTokenService,
                        @Qualifier("hybridDetailsService")
                        private val userDetailsService: UserDetailsService,
                        private val studentService: StudentService,
                        private val tutorService: TutorService,
                        private val adminRepo: AdminRepo,
                        private val emailService: EmailService,
                        private val encoder: BCryptPasswordEncoder) {

    companion object {
        val resetRequest = emptyMap<String, String>()
    }

    @PostMapping("/register-student")
    fun registerStudent(@RequestBody request: CreateStudentDto): ResponseEntity<Response> {
        if (request.email.isRegistered())
            return ResponseEntity(
                    Response(description = Constants.USER_EXISTS),
                    HttpStatus.CONFLICT
            )
        studentService.createStudent(request)
        val token: String = jwtTokenService.generate(request.email)
        val (subject, text) = makeVerifyRequest(request.email, token, Authority.STUDENT)
        emailService.sendMail(request.email, subject, text)
        return ResponseEntity(Response(
                description = Constants.ACCOUNT_CREATED
        ), HttpStatus.OK)
    }

    @PostMapping("/register-tutor")
    fun registerTutor(@RequestBody request: CreateTutorDto): ResponseEntity<Response> {
        if (request.email.isRegistered())
            return ResponseEntity(
                    Response(description = Constants.USER_EXISTS),
                    HttpStatus.CONFLICT
            )
        tutorService.createTutor(request)
        val token: String = jwtTokenService.generate(request.email)
        val (subject, text) = makeVerifyRequest(request.email, token, Authority.TUTOR)
        emailService.sendMail(request.email, subject, text)
        return ResponseEntity(Response(
                description = Constants.ACCOUNT_CREATED
        ), HttpStatus.OK)
    }

    @PostMapping("/register-420-admin")
    fun registerAdmin(@RequestBody request: CreateAdminDto): ResponseEntity<Response> {
        val admin: Admin = adminRepo.save(Admin(email = request.email, password = encoder.encode(request.password)))
        return ResponseEntity(Response(description = Constants.REQUEST_FULFILLED, payload = admin), HttpStatus.OK)
    }

    @GetMapping("/verify")
    fun verifyEmail(@RequestParam email: String, @RequestParam jwt: String, @RequestParam role: String): ResponseEntity<String> {
        val userName: String? = jwtTokenService.getUserNameFromToken(jwt)
        return if (userName != null && userName == email) {
            when (role) {
                Authority.STUDENT.name -> {
                    studentService.updateStudent(email, UpdateStudentDto(null, null, null, true))?.let {
                        ResponseEntity(Constants.REQUEST_FULFILLED, HttpStatus.OK)
                    }
                    ResponseEntity(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND)
                }
                Authority.TUTOR.name -> {
                    tutorService.updateTutor(email, UpdateTutorDto(null, null, null, true))?.let {
                        ResponseEntity(Constants.REQUEST_FULFILLED, HttpStatus.OK)
                    }
                    ResponseEntity(Constants.TUTOR_NOT_FOUND, HttpStatus.NOT_FOUND)
                }
                else -> {
                    ResponseEntity(Constants.INVALID_REQUEST, HttpStatus.BAD_REQUEST)
                }
            }
        } else {
            ResponseEntity(Constants.INVALID_REQUEST, HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/available")
    fun isAvailable(@RequestParam email: String): ResponseEntity<Response> {
        return ResponseEntity(Response(description = Constants.AVAILABILITY_DESC, payload = !email.isRegistered()), HttpStatus.OK)
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthRequest): ResponseEntity<Response> {
        return try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.email, request.password))
            val token: String = jwtTokenService.generate(request.email)
            ResponseEntity(Response(description = Constants.TOKEN_GENERATED,
                    payload = mapOf(token to "token")), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(Response(description = Constants.INVALID_REQUEST), HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/forgot-password")
    fun requestPasswordReset(@RequestBody request: ResetRequest): ResponseEntity<Response> {
        val password: String = encoder.encode(request.password)
        return when (request.role) {
            Authority.STUDENT.name -> {
                studentService.retrieveStudent(request.email)?.let {
                    addResetRequest(request.email, password, Authority.valueOf(request.role))
                }
                ResponseEntity(Response(description = Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
            }
            Authority.TUTOR.name -> {
                tutorService.retrieveTutor(request.email)?.let {
                    addResetRequest(request.email, password, Authority.valueOf(request.role))
                }
                ResponseEntity(Response(description = Constants.TUTOR_NOT_FOUND), HttpStatus.NOT_FOUND)
            }
            else -> {
                ResponseEntity(Response(description = Constants.INVALID_REQUEST), HttpStatus.BAD_REQUEST)
            }
        }
    }

    @GetMapping("/reset")
    fun resetThePassword(@RequestParam email: String, @RequestParam jwt: String, @RequestParam role: String): ResponseEntity<String> {
        val userName: String? = jwtTokenService.getUserNameFromToken(jwt)
        return if (userName != null && userName == email) {
            when (role) {
                Authority.STUDENT.name -> {
                    studentService.updateStudent(email, UpdateStudentDto(resetRequest[email], null, null, null))?.let {
                        ResponseEntity(Constants.REQUEST_FULFILLED, HttpStatus.OK)
                    }
                    ResponseEntity(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND)
                }
                Authority.TUTOR.name -> {
                    tutorService.updateTutor(email, UpdateTutorDto(resetRequest[email], null, null, null))?.let {
                        ResponseEntity(Constants.REQUEST_FULFILLED, HttpStatus.OK)
                    }
                    ResponseEntity(Constants.TUTOR_NOT_FOUND, HttpStatus.NOT_FOUND)
                }
                else -> {
                    ResponseEntity(Constants.INVALID_REQUEST, HttpStatus.BAD_REQUEST)
                }
            }
        } else {
            ResponseEntity(Constants.INVALID_REQUEST, HttpStatus.BAD_REQUEST)
        }
    }

    private fun String.isRegistered(): Boolean {
        studentService.retrieveStudent(this)?.let { return true }
        tutorService.retrieveTutor(this)?.let { return true }
        adminRepo.findByEmail(this)?.let { return true }
        return false
    }

    private fun addResetRequest(email: String, password: String, role: Authority): ResponseEntity<Response> {
        resetRequest.plus(Pair(email, password))
        val token = jwtTokenService.generate(email)
        val (subject, text) = makeResetRequest(email, token, role = role)
        emailService.sendMail(email, subject, text)
        return ResponseEntity(Response(description = Constants.REQUEST_FULFILLED), HttpStatus.OK)
    }
}