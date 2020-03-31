package com.pce.kotlin.bookmetutor.service

import com.pce.kotlin.bookmetutor.model.dao.Student
import com.pce.kotlin.bookmetutor.model.dao.StudentAddress
import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.student.CreateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.UpdateStudentDto

interface StudentService {

    fun retrieveAllStudents(): List<Student>?

    fun retrieveStudent(email: String): Student?

    fun createStudent(student: CreateStudentDto): Student

    fun updateStudent(email: String, update: UpdateStudentDto): Student?

    fun removeStudent(email: String): Boolean

    fun addStudentAddress(email: String, address: CreateAddressDto): Student?

    fun updateStudentAddress(id: Long, update: UpdateAddressDto): StudentAddress?

    fun removeStudentAddress(id: Long): Boolean

    fun addStudentPhone(email: String, phone: String): Student?

    fun removeStudentPhone(email: String, phone: String): Student?

}