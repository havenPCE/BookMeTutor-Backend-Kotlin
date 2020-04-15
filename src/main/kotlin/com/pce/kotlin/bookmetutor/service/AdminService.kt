package com.pce.kotlin.bookmetutor.service

import com.pce.kotlin.bookmetutor.model.dto.admin.AdminDto
import com.pce.kotlin.bookmetutor.model.dto.admin.CreateAdminDto
import com.pce.kotlin.bookmetutor.model.dto.admin.UpdateAdminDto

interface AdminService {
    fun createAdmin(dto: CreateAdminDto): AdminDto?
    fun updateAdmin(email: String, dto: UpdateAdminDto): AdminDto?
    fun removeAdmin(email: String): Boolean
    fun retrieveAdmin(email: String): AdminDto?
    fun retrieveAdmin(id: Long): AdminDto?
    fun retrieveAllAdmin(): List<AdminDto>
}