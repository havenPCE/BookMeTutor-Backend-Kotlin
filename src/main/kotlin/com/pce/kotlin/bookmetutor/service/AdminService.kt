package com.pce.kotlin.bookmetutor.service

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.model.dto.admin.CreateAdminDto
import com.pce.kotlin.bookmetutor.model.dto.admin.UpdateAdminDto

interface AdminService {
    fun createAdmin(dto: CreateAdminDto): Admin?
    fun updateAdmin(email: String, dto: UpdateAdminDto): Admin?
    fun removeAdmin(email: String): Boolean
    fun retrieveAdmin(email: String): Admin?
    fun retrieveAllAdmin(): List<Admin>
}