package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.StudentAddress

interface StudentAddressRepo {
    fun findById(id: Long): StudentAddress?
    fun save(address: StudentAddress): StudentAddress?
    fun update(address: StudentAddress): StudentAddress?
    fun deleteById(id: Long): Boolean
}