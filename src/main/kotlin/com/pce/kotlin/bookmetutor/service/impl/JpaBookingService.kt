package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Booking
import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dto.booking.CreateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.repository.BookingRepo
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import com.pce.kotlin.bookmetutor.service.BookingService
import com.pce.kotlin.bookmetutor.util.BookingStatus
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class JpaBookingService(private val bookingRepo: BookingRepo,
                        private val studentRepo: StudentRepo,
                        private val tutorRepo: TutorRepo) : BookingService {

    override fun createBooking(email: String, booking: CreateBookingDto): Booking? {
        val newBooking: Booking = Booking.fromDto(booking)
        studentRepo.findByEmail(email)?.let { student ->
            val city: String? = newBooking.address?.city
            val tutor: Tutor? = if (city != null) {
                tutorRepo.findFirstByEmailNotInAndAddress_CityOrderByLastPickedAsc(newBooking.rejects.map { it }, city)
            } else null
            tutor?.let {
                return bookingRepo.save(newBooking.apply {
                    this.student = student
                    this.tutor = tutor
                })
            }
        }
        return null
    }

    override fun updateBooking(id: Long, update: UpdateBookingDto): Booking? {
        val booking: Booking? = bookingRepo.findByIdOrNull(id)
        booking?.let {
            return if (it.rescheduled)
                bookingRepo.save(Booking.fromDto(update.copy(scheduleTime = null), it))
            else bookingRepo.save(Booking.fromDto(update, it))
        }
        return null
    }

    override fun removeBooking(id: Long): Boolean {
        val booking: Booking? = bookingRepo.findByIdOrNull(id)
        booking?.let {
            bookingRepo.delete(booking)
            return true
        }
        return false
    }

    override fun retrieveBooking(id: Long): Booking? {
        return bookingRepo.findByIdOrNull(id)
    }

    override fun retrieveAllBookings(): List<Booking>? {
        return bookingRepo.findAll()
    }

    override fun retrieveAllBookingsByStatus(status: BookingStatus): List<Booking>? {
        return bookingRepo.findAllByStatus(status)
    }

    override fun assignBookingTutor(id: Long): Booking? {
        val booking: Booking? = bookingRepo.findByIdOrNull(id)
        val list: List<String>? = booking?.rejects?.map { it }
        val city: String? = booking?.address?.city
        val tutor: Tutor? = if (list != null && city != null) {
            tutorRepo.findFirstByEmailNotInAndAddress_CityOrderByLastPickedAsc(list, city)
        } else null
        tutor?.let {
            return bookingRepo.save(booking!!.apply { this.tutor = it })
        }
        return null
    }
}