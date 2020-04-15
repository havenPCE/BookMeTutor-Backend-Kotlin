package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Student
import com.pce.kotlin.bookmetutor.model.dao.StudentAddress
import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.student.CreateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.StudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.UpdateStudentDto
import com.pce.kotlin.bookmetutor.repository.StudentAddressRepo
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import com.pce.kotlin.bookmetutor.service.StudentService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JdbcStudentService(val studentRepo: StudentRepo, val studentAddressRepo: StudentAddressRepo,
                         val passwordEncoder: BCryptPasswordEncoder, val tutorRepo: TutorRepo) : StudentService {

    override fun retrieveAllStudents(): List<StudentDto> {
        return studentRepo.findAll().mapNotNull { toDto(it) }
    }

    override fun retrieveStudent(email: String): StudentDto? {
        return studentRepo.findByEmail(email)?.let { toDto(it) }
    }

    override fun retrieveStudent(id: Long): StudentDto? {
        return studentRepo.findById(id)?.let { toDto(it) }
    }

    override fun createStudent(dto: CreateStudentDto): StudentDto? {
        return studentRepo.save(Student.fromDto(dto.copy(
                password = passwordEncoder.encode(dto.password)
        )))?.let { toDto(it) }
    }

    override fun updateStudent(email: String, dto: UpdateStudentDto): StudentDto? {
        return studentRepo.findByEmail(email)?.let { student ->
            val password = dto.password?.let { passwordEncoder.encode(dto.password) }
            studentRepo.update(Student.fromDto(dto.copy(password = password), student))?.let { toDto(it) }
        }
    }

    override fun removeStudent(email: String): Boolean {
        return studentRepo.deleteByEmail(email)
    }

    override fun addStudentAddress(email: String, dto: CreateAddressDto): AddressDto? {
        return studentRepo.findByEmail(email)?.let { student ->
            studentAddressRepo.save(student.id, StudentAddress.fromDto(dto))?.toDto()
        }
    }

    override fun updateStudentAddress(id: Long, dto: UpdateAddressDto): AddressDto? {
        return studentAddressRepo.findById(id)?.let {
            studentAddressRepo.update(StudentAddress.fromDto(dto, it))?.toDto()
        }
    }

    override fun removeStudentAddress(id: Long): Boolean {
        return studentAddressRepo.deleteById(id)
    }

    override fun addStudentPhone(email: String, phone: String): StudentDto? {
        return studentRepo.findByEmail(email)?.let { student ->
            studentRepo.update(student.copy(
                    phones = student.phones.plus(phone)
            ))
        }?.let { toDto(it) }
    }

    override fun removeStudentPhone(email: String, phone: String): StudentDto? {
        return studentRepo.findByEmail(email)?.let { student ->
            studentRepo.update(student.copy(
                    phones = student.phones.minus(phone)
            ))
        }?.let { toDto(it) }
    }

    fun toDto(student: Student): StudentDto? {
        return student.toDto().copy(
                bookings = student.bookings.mapNotNull { booking ->
                    booking.tutorId?.let { id ->
                        tutorRepo.findById(id)?.let {
                            booking.toDto().copy(
                                    studentEmail = student.email,
                                    studentPhone = student.phones.firstOrNull(),
                                    tutorEmail = it.email,
                                    tutorPhone = it.phones.firstOrNull()
                            )
                        }
                    }
                }
        )
    }

}