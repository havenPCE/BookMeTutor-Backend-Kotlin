package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Booking
import com.pce.kotlin.bookmetutor.model.dao.Student
import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dto.booking.CreateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.repository.BookingRepo
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import com.pce.kotlin.bookmetutor.service.BookingService
import com.pce.kotlin.bookmetutor.util.Gender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JdbcBookingService(val bookingRepo: BookingRepo, val tutorRepo: TutorRepo, val studentRepo: StudentRepo) : BookingService {
    override fun createBooking(email: String, dto: CreateBookingDto): Booking? {
        val student: Student? = studentRepo.findByEmail(email)
        student?.let {
            val booking: Booking = Booking.fromDto(dto)
            val tutor: Tutor? = booking.address?.city?.let { city -> tutorRepo.findTutorForAssignment(it.gender, city, emptyList()) }
            tutor?.let {
                return bookingRepo.save(it.id, tutor.id, booking)
            }
        }
        return null
    }

    override fun updateBooking(id: Long, dto: UpdateBookingDto): Booking? {
        val booking: Booking? = bookingRepo.findById(id)
        booking?.let {
            return bookingRepo.update(Booking.fromDto(dto, booking))
        }
        return null
    }

    override fun removeBooking(id: Long): Boolean {
        return bookingRepo.deleteById(id)
    }

    override fun retrieveBooking(id: Long): Booking? {
        return bookingRepo.findById(id)
    }

    override fun retrieveAllBookings(): List<Booking> {
        return bookingRepo.findAll()
    }

    override fun assignBookingTutor(id: Long, gender: Gender, city: String): Booking? {
        val booking: Booking? = bookingRepo.findById(id)
        booking?.let { b ->
            val tutor: Tutor? = tutorRepo.findTutorForAssignment(gender, city, b.rejects.toList())
            tutor?.let { t ->
                return bookingRepo.update(b, t.id)
            }
        }
        return null
    }
}