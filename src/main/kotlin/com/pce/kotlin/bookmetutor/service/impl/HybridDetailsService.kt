package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.auth.AdminDetails
import com.pce.kotlin.bookmetutor.model.auth.StudentDetails
import com.pce.kotlin.bookmetutor.model.auth.TutorDetails
import com.pce.kotlin.bookmetutor.repository.AdminRepo
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class HybridDetailsService(val adminRepo: AdminRepo, val studentRepo: StudentRepo, val tutorRepo: TutorRepo) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        adminRepo.findByEmail(username)?.let { return AdminDetails(it) }
        studentRepo.findByEmail(username)?.let { return StudentDetails(it) }
        tutorRepo.findByEmail(username)?.let { return TutorDetails(it) }
        return User("user", "password", emptyList())
    }
}