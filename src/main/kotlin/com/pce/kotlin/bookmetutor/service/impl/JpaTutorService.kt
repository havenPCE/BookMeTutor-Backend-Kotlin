package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dao.TutorAddress
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.CreateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto
import com.pce.kotlin.bookmetutor.repository.TutorAddressRepo
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import com.pce.kotlin.bookmetutor.service.TutorService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class JpaTutorService(private val tutorRepo: TutorRepo,
                      private val tutorAddressRepo: TutorAddressRepo,
                      private val encoder: BCryptPasswordEncoder) : TutorService {

    override fun retrieveAllTutors(): List<Tutor>? {
        return tutorRepo.findAll()
    }

    override fun retrieveTutor(email: String): Tutor? {
        return tutorRepo.findByEmail(email)
    }

    override fun createTutor(tutor: CreateTutorDto): Tutor {
        return tutorRepo.save(Tutor.fromDto(tutor).apply {
            this.password = encoder.encode(this.password)
        })
    }

    override fun updateTutor(email: String, update: UpdateTutorDto): Tutor? {
        val tutor: Tutor? = tutorRepo.findByEmail(email)
        tutor?.let {
            return tutorRepo.save(Tutor.fromDto(update, it))
        }
        return null
    }

    override fun removeTutor(email: String): Boolean {
        val tutor: Tutor? = tutorRepo.findByEmail(email)
        tutor?.let {
            tutorRepo.delete(it)
            return true
        }
        return false
    }

    override fun addTutorPhone(email: String, phone: String): Tutor? {
        val tutor: Tutor? = tutorRepo.findByEmail(email)
        tutor?.let {
            it.phones.plus(phone)
            return tutorRepo.save(it)
        }
        return null
    }

    override fun removeTutorPhone(email: String, phone: String): Tutor? {
        val tutor: Tutor? = tutorRepo.findByEmail(email)
        tutor?.let {
            it.phones.minus(phone)
            return tutorRepo.save(it)
        }
        return null
    }

    override fun updateTutorAddress(id: Long, update: UpdateAddressDto): TutorAddress? {
        val tutorAddress: TutorAddress? = tutorAddressRepo.findByIdOrNull(id)
        tutorAddress?.let {
            return tutorAddressRepo.save(TutorAddress.fromDto(update, it))
        }
        return null
    }

}