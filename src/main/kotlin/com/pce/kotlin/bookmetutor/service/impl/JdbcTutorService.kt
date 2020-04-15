package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dao.TutorAddress
import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.CreateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.TutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import com.pce.kotlin.bookmetutor.repository.TutorAddressRepo
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import com.pce.kotlin.bookmetutor.service.TutorService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JdbcTutorService(val tutorRepo: TutorRepo, val tutorAddressRepo: TutorAddressRepo,
                       val studentRepo: StudentRepo, val passwordEncoder: BCryptPasswordEncoder) : TutorService {
    override fun retrieveAllTutors(): List<TutorDto> {
        return tutorRepo.findAll().mapNotNull { toDto(it) }
    }

    override fun retrieveTutor(email: String): TutorDto? {
        return tutorRepo.findByEmail(email)?.let { toDto(it) }
    }

    override fun retrieveTutor(id: Long): TutorDto? {
        return tutorRepo.findById(id)?.let { toDto(it) }
    }

    override fun createTutor(dto: CreateTutorDto): TutorDto? {
        return tutorRepo.save(Tutor.fromDto(dto.copy(
                password = passwordEncoder.encode(dto.password)
        )))?.let { toDto(it) }
    }

    override fun updateTutor(email: String, dto: UpdateTutorDto): TutorDto? {
        return tutorRepo.findByEmail(email)?.let { tutor ->
            val password = dto.password?.let { passwordEncoder.encode(dto.password) }
            tutorRepo.update(Tutor.fromDto(dto.copy(password = password), tutor))
        }?.let { toDto(it) }
    }

    override fun removeTutor(email: String): Boolean {
        return tutorRepo.deleteByEmail(email)
    }

    override fun addTutorPhone(email: String, phone: String): TutorDto? {
        return tutorRepo.findByEmail(email)?.let { tutor ->
            tutorRepo.update(tutor.copy(
                    phones = tutor.phones.plus(phone)
            ))
        }?.let { toDto(it) }
    }

    override fun removeTutorPhone(email: String, phone: String): TutorDto? {
        return tutorRepo.findByEmail(email)?.let { tutor ->
            tutorRepo.update(tutor.copy(
                    phones = tutor.phones.minus(phone)
            ))
        }?.let { toDto(it) }
    }

    override fun updateTutorAddress(id: Long, dto: UpdateAddressDto): AddressDto? {
        return tutorAddressRepo.findById(id)?.let { tutorAddress ->
            tutorAddressRepo.update(TutorAddress.fromDto(dto, tutorAddress))
        }?.toDto()
    }

    fun toDto(tutor: Tutor): TutorDto? {
        return tutor.toDto().copy(
                bookings = tutor.bookings.mapNotNull { booking ->
                    booking.studentId?.let { id ->
                        studentRepo.findById(id)?.let {
                            booking.toDto().copy(
                                    tutorEmail = tutor.email,
                                    tutorPhone = tutor.phones.firstOrNull(),
                                    studentEmail = it.email,
                                    studentPhone = it.phones.firstOrNull()
                            )
                        }
                    }
                }
        )
    }

}