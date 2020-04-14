package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.model.dto.admin.CreateAdminDto
import com.pce.kotlin.bookmetutor.model.dto.admin.UpdateAdminDto
import com.pce.kotlin.bookmetutor.repository.AdminRepo
import com.pce.kotlin.bookmetutor.service.AdminService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JdbcAdminService(val adminRepo: AdminRepo, val passwordEncoder: BCryptPasswordEncoder) : AdminService {
    override fun createAdmin(dto: CreateAdminDto): Admin? {
        return adminRepo.save(Admin.fromDto(dto.copy(password = passwordEncoder.encode(dto.password))))
    }

    override fun updateAdmin(email: String, dto: UpdateAdminDto): Admin? {
        val admin: Admin? = adminRepo.findByEmail(email)
        admin?.let {
            val password = dto.password?.let { passwordEncoder.encode(it) }
            return adminRepo.update(Admin.fromDto(dto.copy(password = password), admin))
        }
        return null
    }

    override fun removeAdmin(email: String): Boolean {
        return adminRepo.deleteByEmail(email)
    }

    override fun retrieveAdmin(email: String): Admin? {
        return adminRepo.findByEmail(email)
    }

    override fun retrieveAllAdmin(): List<Admin> {
        return adminRepo.findAll()
    }
}