package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Tutor

interface TutorRepo {
    fun findByEmail(email: String): Tutor?
    fun findTutorForAssignment(city: String, rejects: List<String>): Tutor?
    fun save(tutor: Tutor): Tutor?
    fun update(tutor: Tutor): Tutor?
    fun deleteByEmail(email: String): Boolean
}