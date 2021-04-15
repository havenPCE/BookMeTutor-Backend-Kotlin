package com.pce.kotlin.bookmetutor.service

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.student.CreateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.StudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.UpdateStudentDto

interface StudentService {
    fun retrieveAllStudents(): List<StudentDto>
    fun retrieveStudent(email: String): StudentDto?
    fun retrieveStudent(id: Long): StudentDto?
    fun createStudent(dto: CreateStudentDto): StudentDto?
    fun updateStudent(email: String, dto: UpdateStudentDto): StudentDto?
    fun removeStudent(email: String): Boolean
    fun addStudentAddress(email: String, dto: CreateAddressDto): AddressDto?
    fun updateStudentAddress(id: Long, dto: UpdateAddressDto): AddressDto?
    fun removeStudentAddress(id: Long): Boolean
    fun addStudentPhone(email: String, phone: String): StudentDto?
    fun removeStudentPhone(email: String, phone: String): StudentDto?
}