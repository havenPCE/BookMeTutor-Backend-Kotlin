package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.model.dto.admin.AdminDto
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
    override fun createAdmin(dto: CreateAdminDto): AdminDto? {
        return adminRepo.save(Admin.fromDto(dto.copy(password = passwordEncoder.encode(dto.password))))?.toDto()
    }

    override fun updateAdmin(email: String, dto: UpdateAdminDto): AdminDto? {
        return adminRepo.findByEmail(email)?.let {
            val password = dto.password?.let { passwordEncoder.encode(dto.password) }
            adminRepo.update(Admin.fromDto(dto.copy(password = password), it))?.toDto()
        }
    }

    override fun removeAdmin(email: String): Boolean {
        return adminRepo.deleteByEmail(email)
    }

    override fun retrieveAdmin(email: String): AdminDto? {
        return adminRepo.findByEmail(email)?.toDto()
    }

    override fun retrieveAdmin(id: Long): AdminDto? {
        return adminRepo.findById(id)?.toDto()
    }

    override fun retrieveAllAdmin(): List<AdminDto> {
        return adminRepo.findAll().map { it.toDto() }
    }

}