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
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class JpaStudentService(val studentRepo: StudentRepo,
                        val studentAddressRepo: StudentAddressRepo,
                        val encoder: BCryptPasswordEncoder) : StudentService {

    override fun retrieveAllStudents(): List<Student>? {
        return studentRepo.findAll()
    }

    override fun retrieveStudent(email: String): Student? {
        return studentRepo.findByEmail(email)
    }

    override fun createStudent(student: CreateStudentDto): Student {
        return studentRepo.save(Student.fromDto(student).apply {
            this.password = encoder.encode(this.password)
        })
    }

    override fun updateStudent(email: String, update: UpdateStudentDto): Student? {
        val student: Student? = studentRepo.findByEmail(email)
        student?.let {
            return studentRepo.save(Student.fromDto(update, it))
        }
        return null
    }

    override fun removeStudent(email: String): Boolean {
        val student: Student? = studentRepo.findByEmail(email)
        student?.let {
            studentRepo.delete(it)
            return true
        }
        return false
    }

    override fun addStudentAddress(email: String, address: CreateAddressDto): Student? {
        val student: Student? = studentRepo.findByEmail(email)
        student?.let {
            it.addresses.plus(
                    studentAddressRepo.save(StudentAddress.fromDto(address).apply {
                        this.student = it
                    })
            )
            return studentRepo.save(it)
        }
        return null
    }

    override fun updateStudentAddress(id: Long, update: UpdateAddressDto): StudentAddress? {
        studentAddressRepo.findByIdOrNull(id)?.let {
            return studentAddressRepo.save(StudentAddress.fromDto(update, it))
        }
        return null
    }

    override fun removeStudentAddress(id: Long): Boolean {
        studentAddressRepo.findByIdOrNull(id)?.let {
            studentAddressRepo.delete(it)
            return true
        }
        return false
    }

    override fun addStudentPhone(email: String, phone: String): Student? {
        studentRepo.findByEmail(email)?.let {
            it.phones.plus(phone)
            return studentRepo.save(it)
        }
        return null
    }

    override fun removeStudentPhone(email: String, phone: String): Student? {
        studentRepo.findByEmail(email)?.let {
            it.phones.minus(phone)
            return studentRepo.save(it)
        }
        return null
    }
}