package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.model.dao.User

interface AdminRepo {
    fun save(admin: Admin): Admin?
    fun update(admin: Admin): Admin?
    fun findByEmail(email: String): Admin?
    fun findById(id: Long): Admin?
    fun deleteByEmail(email: String): Boolean
    fun findAll(): List<Admin>
    fun findUser(email: String): User?
}