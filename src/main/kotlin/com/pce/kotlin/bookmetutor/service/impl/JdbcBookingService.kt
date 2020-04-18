package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.model.dao.Booking
import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dto.booking.BookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.CreateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto
import com.pce.kotlin.bookmetutor.repository.BookingRepo
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import com.pce.kotlin.bookmetutor.service.BookingService
import com.pce.kotlin.bookmetutor.service.EmailService
import com.pce.kotlin.bookmetutor.util.makeApology
import com.pce.kotlin.bookmetutor.util.makeAvailableEmail
import com.pce.kotlin.bookmetutor.util.makeBookingChangeMail
import com.pce.kotlin.bookmetutor.util.makeThanksMail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class JdbcBookingService(val bookingRepo: BookingRepo, val tutorRepo: TutorRepo, val studentRepo: StudentRepo, val emailService: EmailService) : BookingService {
    override fun createBooking(email: String, dto: CreateBookingDto): BookingDto? {
        return studentRepo.findByEmail(email)?.let { student ->
            tutorRepo.findTutorForAssignment(dto.address.city, emptyList())?.let { tutor ->
                bookingRepo.save(student.id, tutor.id, Booking.fromDto(dto))?.let { booking ->
                    sendCreation(student.email, tutor.email, student.firstName, tutor.firstName, booking.id)
                    tutorRepo.update(Tutor.fromDto(UpdateTutorDto(lastPicked = LocalDateTime.now()), tutor))
                    booking.toDto().copy(
                            studentEmail = student.email,
                            studentPhone = student.phones.firstOrNull(),
                            tutorEmail = tutor.email,
                            tutorPhone = tutor.phones.firstOrNull()
                    )
                }
            } ?: run {
                sendApology(student.email, student.firstName)
                null
            }
        }
    }

    override fun updateBooking(id: Long, dto: UpdateBookingDto): BookingDto? {
        val update = bookingRepo.findById(id)?.let { booking ->
            if (booking.rescheduled) {
                bookingRepo.update(Booking.fromDto(dto.copy(scheduledTime = null), booking))
            } else bookingRepo.update(Booking.fromDto(dto, booking))
        }?.let { toDto(it) }
        update?.tutorEmail?.let {
            sendUpdate(it, id)
        }
        return update
    }

    override fun removeBooking(id: Long): Boolean {
        return bookingRepo.deleteById(id)
    }

    override fun retrieveBooking(id: Long): BookingDto? {
        return bookingRepo.findById(id)?.let { toDto(it) }
    }

    override fun retrieveAllBookings(): List<BookingDto> {
        return bookingRepo.findAll().mapNotNull { toDto(it) }
    }

    override fun reassignTutor(id: Long): BookingDto? {
        return bookingRepo.findById(id)?.let { booking ->
            booking.address?.let { address ->
                booking.studentId?.let { studentRepo.findById(it) }
                        ?.let { student ->
                            tutorRepo.findTutorForAssignment(address.city, booking.rejects.toList())?.let { tutor ->
                                bookingRepo.update(booking, tutor.id)?.let {
                                    sendAvailable(tutor.email, tutor.firstName, booking.id)
                                    tutorRepo.update(Tutor.fromDto(UpdateTutorDto(lastPicked = LocalDateTime.now()), tutor))
                                    it.toDto().copy(
                                            studentEmail = student.email,
                                            studentPhone = student.phones.firstOrNull(),
                                            tutorEmail = tutor.email,
                                            tutorPhone = tutor.phones.firstOrNull()
                                    )
                                }
                            } ?: run {
                                bookingRepo.deleteById(id)
                                sendApology(student.email, student.firstName)
                                null
                            }
                        }
            }
        }
    }

    fun toDto(booking: Booking): BookingDto? {
        return booking.studentId?.let { studentId ->
            booking.tutorId?.let { tutorId ->
                studentRepo.findById(studentId)?.let { student ->
                    tutorRepo.findById(tutorId)?.let { tutor ->
                        booking.toDto().copy(
                                studentEmail = student.email,
                                studentPhone = student.phones.firstOrNull(),
                                tutorEmail = tutor.email,
                                tutorPhone = tutor.phones.firstOrNull()
                        )
                    }
                }
            }
        }
    }

    fun sendApology(studentMail: String, studentName: String) {
        val (subject, text) = makeApology(studentName)
        emailService.sendMail(studentMail, subject, text)
    }

    fun sendCreation(studentMail: String, tutorMail: String, studentName: String, tutorName: String, bookingId: Long) {
        val (studentSubject, studentText) = makeThanksMail(bookingId, studentName)
        val (tutorSubject, tutorText) = makeAvailableEmail(bookingId, tutorName)
        emailService.sendMail(studentMail, studentSubject, studentText)
        emailService.sendMail(tutorMail, tutorSubject, tutorText)
    }

    fun sendUpdate(tutorMail: String, bookingId: Long) {
        val (subject, text) = makeBookingChangeMail(bookingId)
        emailService.sendMail(tutorMail, subject, text)
    }

    fun sendAvailable(tutorEmail: String, tutorName: String, bookingId: Long) {
        val (subject, text) = makeAvailableEmail(bookingId, tutorName)
        emailService.sendMail(tutorEmail, subject, text)
    }

}