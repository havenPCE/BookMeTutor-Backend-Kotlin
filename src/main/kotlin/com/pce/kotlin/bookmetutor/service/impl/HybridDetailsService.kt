package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.auth.AdminDetails
import com.pce.kotlin.bookmetutor.model.auth.StudentDetails
import com.pce.kotlin.bookmetutor.model.auth.TutorDetails
import com.pce.kotlin.bookmetutor.service.AdminService
import com.pce.kotlin.bookmetutor.service.StudentService
import com.pce.kotlin.bookmetutor.service.TutorService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class HybridDetailsService(val studentService: StudentService, val tutorService: TutorService, val adminService: AdminService) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        adminService.retrieveAdmin(username)?.let { return AdminDetails(it) }
        studentService.retrieveStudent(username)?.let { return StudentDetails(it) }
        tutorService.retrieveTutor(username)?.let { return TutorDetails(it) }
        return User("user", "password", emptyList())
    }
}