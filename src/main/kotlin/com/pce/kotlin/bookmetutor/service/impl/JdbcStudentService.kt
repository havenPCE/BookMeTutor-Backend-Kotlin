package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Student
import com.pce.kotlin.bookmetutor.model.dao.StudentAddress
import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.student.CreateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.UpdateStudentDto
import com.pce.kotlin.bookmetutor.repository.StudentAddressRepo
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import com.pce.kotlin.bookmetutor.service.StudentService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JdbcStudentService(val studentRepo: StudentRepo, val studentAddressRepo: StudentAddressRepo) : StudentService {
    override fun retrieveAllStudents(): List<Student> {
        return studentRepo.findAll()
    }

    override fun retrieveStudent(email: String): Student? {
        return studentRepo.findByEmail(email)
    }

    override fun createStudent(dto: CreateStudentDto): Student? {
        return studentRepo.save(Student.fromDto(dto))
    }

    override fun updateStudent(email: String, dto: UpdateStudentDto): Student? {
        val student: Student? = studentRepo.findByEmail(email)
        student?.let {
            return studentRepo.update(Student.fromDto(dto, student))
        }
        return null
    }

    override fun removeStudent(email: String): Boolean {
        return studentRepo.deleteByEmail(email)
    }

    override fun addStudentAddress(email: String, dto: CreateAddressDto): Student? {
        val student: Student? = studentRepo.findByEmail(email)
        student?.let {
            studentAddressRepo.save(it.id, StudentAddress.fromDto(dto))
            return studentRepo.findByEmail(it.email)
        }
        return null
    }

    override fun updateStudentAddress(id: Long, dto: UpdateAddressDto): StudentAddress? {
        val address: StudentAddress? = studentAddressRepo.findById(id)
        address?.let {
            return studentAddressRepo.update(StudentAddress.fromDto(dto, address))
        }
        return null
    }

    override fun removeStudentAddress(id: Long): Boolean {
        return studentAddressRepo.deleteById(id)
    }

    override fun addStudentPhone(email: String, phone: String): Student? {
        val student: Student? = studentRepo.findByEmail(email)
        student?.let {
            it.phones.plus(phone)
            return studentRepo.update(it)
        }
        return null
    }

    override fun removeStudentPhone(email: String, phone: String): Student? {
        val student: Student? = studentRepo.findByEmail(email)
        student?.let {
            it.phones.minus(phone)
            return studentRepo.update(it)
        }
        return null
    }

}