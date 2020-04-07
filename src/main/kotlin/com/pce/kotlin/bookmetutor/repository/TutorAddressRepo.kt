package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dao.TutorAddress

interface TutorAddressRepo {
    fun findById(id: Long): Tutor?
    fun save(address: TutorAddress): TutorAddress?
    fun update(address: TutorAddress): TutorAddress?
    fun deleteById(id: Long): Boolean
}