package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dao.TutorAddress
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.CreateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto
import com.pce.kotlin.bookmetutor.repository.TutorAddressRepo
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import com.pce.kotlin.bookmetutor.service.TutorService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JdbcTutorService(val tutorRepo: TutorRepo, val tutorAddressRepo: TutorAddressRepo, val passwordEncoder: BCryptPasswordEncoder) : TutorService {
    override fun retrieveAllTutors(): List<Tutor> {
        return tutorRepo.findAll()
    }

    override fun retrieveTutor(email: String): Tutor? {
        return tutorRepo.findByEmail(email)
    }

    override fun createTutor(dto: CreateTutorDto): Tutor? {
        return tutorRepo.save(Tutor.fromDto(dto.copy(password = passwordEncoder.encode(dto.password))))
    }

    override fun updateTutor(email: String, dto: UpdateTutorDto): Tutor? {
        val tutor: Tutor? = tutorRepo.findByEmail(email)
        tutor?.let {
            val password = dto.password?.let { passwordEncoder.encode(it) }
            tutorRepo.save(Tutor.fromDto(dto.copy(password = password), tutor))
        }
        return null
    }

    override fun removeTutor(email: String): Boolean {
        return tutorRepo.deleteByEmail(email)
    }

    override fun addTutorPhone(email: String, phone: String): Tutor? {
        val tutor: Tutor? = tutorRepo.findByEmail(email)
        tutor?.let {
            it.phones.plus(phone)
            return tutorRepo.update(it)
        }
        return null
    }

    override fun removeTutorPhone(email: String, phone: String): Tutor? {
        val tutor: Tutor? = tutorRepo.findByEmail(email)
        tutor?.let {
            it.phones.minus(phone)
            return tutorRepo.update(it)
        }
        return null
    }

    override fun updateTutorAddress(id: Long, dto: UpdateAddressDto): TutorAddress? {
        val address: TutorAddress? = tutorAddressRepo.findById(id)
        address?.let {
            return tutorAddressRepo.update(TutorAddress.fromDto(dto, it))
        }
        return null
    }
}