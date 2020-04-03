package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.authDetails.AdminDetails
import com.pce.kotlin.bookmetutor.model.authDetails.StudentDetails
import com.pce.kotlin.bookmetutor.model.authDetails.TutorDetails
import com.pce.kotlin.bookmetutor.repository.AdminRepo
import com.pce.kotlin.bookmetutor.service.StudentService
import com.pce.kotlin.bookmetutor.service.TutorService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class HybridDetailsService(private val studentService: StudentService,
                           private val tutorService: TutorService,
                           private val adminRepo: AdminRepo) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        adminRepo.findByEmail(username)?.let {
            return AdminDetails(it)
        }
        studentService.retrieveStudent(username)?.let {
            return StudentDetails(it)
        }
        tutorService.retrieveTutor(username)?.let {
            return TutorDetails(it)
        }
        return User("", "", emptyList())
    }
}