package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Student

interface StudentRepo {
    fun findById(id: Long): Student?
    fun findByEmail(email: String): Student?
    fun save(student: Student): Student?
    fun update(student: Student): Student?
    fun deleteByEmail(email: String): Boolean
    fun findAll(): List<Student>
}