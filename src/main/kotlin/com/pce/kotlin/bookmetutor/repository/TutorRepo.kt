package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dao.User

interface TutorRepo {
    fun findById(id: Long): Tutor?
    fun findByEmail(email: String): Tutor?
    fun findTutorForAssignment(city: String, rejects: List<String>): Tutor?
    fun save(tutor: Tutor): Tutor?
    fun update(tutor: Tutor): Tutor?
    fun deleteByEmail(email: String): Boolean
    fun findAll(): List<Tutor>
    fun findUser(email: String): User?
}