package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.TutorAddress

interface TutorAddressRepo {
    fun findById(id: Long): TutorAddress?
    fun save(tutorId: Long, address: TutorAddress): TutorAddress?
    fun update(address: TutorAddress): TutorAddress?
    fun deleteById(id: Long): Boolean
}