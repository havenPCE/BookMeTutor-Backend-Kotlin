package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.phone.CreatePhoneDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.util.CompletionRequest
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.service.BookingService
import com.pce.kotlin.bookmetutor.service.EmailService
import com.pce.kotlin.bookmetutor.service.TutorService
import com.pce.kotlin.bookmetutor.util.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/tutors")
class TutorController(
    val tutorService: TutorService,
    val bookingService: BookingService,
    val emailService: EmailService
) : HandlesError() {

    @GetMapping("/{email}/")
    fun retrieveTutor(@PathVariable email: String): ResponseEntity<out Response> {
        tutorService.retrieveTutor(email)?.let {
            return response(status = HttpStatus.OK, message = TUTOR_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }

    @GetMapping("/{email}/phones")
    fun retrieveTutorPhones(@PathVariable email: String): ResponseEntity<out Response> {
        tutorService.retrieveTutor(email)?.let {
            return response(status = HttpStatus.OK, message = PHONE_INFO, payload = it.phones)
        }
        return response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }

    @GetMapping("/{email}/address")
    fun retrieveTutorAddress(@PathVariable email: String): ResponseEntity<out Response> {
        tutorService.retrieveTutor(email)?.let {
            return response(status = HttpStatus.OK, message = ADDRESS_INFO, payload = it.address)
        }
        return response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }

    @GetMapping("/{email}/qualification")
    fun retrieveTutorQualification(@PathVariable email: String): ResponseEntity<out Response> {
        tutorService.retrieveTutor(email)?.let {
            return response(status = HttpStatus.OK, message = QUALIFICATION_INFO, payload = it.qualification)
        }
        return response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }

    @GetMapping("/{email}/bookings")
    fun retrieveTutorBookings(@PathVariable email: String): ResponseEntity<out Response> {
        tutorService.retrieveTutor(email)?.let {
            return response(status = HttpStatus.OK, message = BOOKING_INFO, payload = it.bookings)
        }
        return response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }

    @GetMapping("/{email}/bookings/{id}")
    fun retrieveParticularBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<out Response> {
        bookingService.retrieveBooking(id)?.let {
            return response(status = HttpStatus.OK, message = BOOKING_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = BOOKING_NOT_FOUND)
    }

    @PostMapping("/{email}/phones")
    fun addTutorPhone(@PathVariable email: String, @RequestBody dto: CreatePhoneDto): ResponseEntity<out Response> {
        tutorService.addTutorPhone(email, dto.phone)?.let {
            return response(status = HttpStatus.OK, message = PHONE_INFO, payload = it.phones)
        }
        return response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }

    @PutMapping("/{email}/")
    fun updateTutorInfo(@PathVariable email: String, @RequestBody dto: UpdateTutorDto): ResponseEntity<out Response> {
        tutorService.updateTutor(email, dto)?.let {
            return response(status = HttpStatus.OK, message = TUTOR_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }

    @PutMapping("/{email}/address")
    fun updateTutorAddress(
        @PathVariable email: String,
        @RequestBody dto: UpdateAddressDto
    ): ResponseEntity<out Response> {
        tutorService.retrieveTutor(email)?.let { tutor ->
            tutor.address?.id?.let {
                tutorService.updateTutorAddress(it, dto)?.let { address ->
                    return response(status = HttpStatus.OK, message = ADDRESS_INFO, payload = address)
                }
            }
        }
        return response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }

    @PutMapping("/{email}/bookings/{id}/accept")
    fun acceptBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<out Response> {
        bookingService.updateBooking(id, UpdateBookingDto(status = BookingStatus.ACCEPTED.name))?.let { booking ->
            booking.studentEmail?.let {
                sendAcceptMail(email, it, booking.id)
                return response(status = HttpStatus.OK, message = TASK_SUCCESSFUL)
            }
        }
        return response(status = HttpStatus.NOT_FOUND, message = BOOKING_NOT_FOUND)
    }

    @PutMapping("/{email}/bookings/{id}/reject")
    fun rejectBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<out Response> {
        bookingService.updateBooking(id, UpdateBookingDto(reject = email))?.let {
            bookingService.reassignTutor(id)?.let {
                return response(status = HttpStatus.OK, message = TASK_SUCCESSFUL)
            }
            return response(status = HttpStatus.EXPECTATION_FAILED, message = TUTOR_NOT_FOUND)
        }
        return response(status = HttpStatus.NOT_FOUND, message = BOOKING_NOT_FOUND)
    }

    @PutMapping("/{email}/bookings/{id}/start")
    fun startBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<out Response> {
        bookingService.updateBooking(id, UpdateBookingDto(startTime = LocalDateTime.now()))?.let {
            return response(status = HttpStatus.OK, message = TASK_SUCCESSFUL)
        }
        return response(status = HttpStatus.NOT_FOUND, message = BOOKING_NOT_FOUND)
    }

    @PutMapping("/{email}/bookings/{id}/complete")
    fun completeBooking(
        @PathVariable email: String,
        @PathVariable id: Long,
        @RequestBody dto: CompletionRequest
    ): ResponseEntity<out Response> {
        return if (bookingService.retrieveBooking(id)?.secret == dto.secret) {
            bookingService.updateBooking(
                id,
                UpdateBookingDto(endTime = LocalDateTime.now(), status = BookingStatus.COMPLETED.name)
            )
            response(status = HttpStatus.OK, message = TASK_SUCCESSFUL)
        } else response(status = HttpStatus.FORBIDDEN, message = SECRET_UNMATCHED)
    }

    @DeleteMapping("/{email}/phones/{phone}")
    fun removeTutorPhone(@PathVariable email: String, @PathVariable phone: String): ResponseEntity<out Response> {
        tutorService.removeTutorPhone(email, phone)?.let {
            return response(status = HttpStatus.OK, message = PHONE_INFO, payload = it.phones)
        }
        return response(status = HttpStatus.NOT_FOUND, message = TUTOR_NOT_FOUND)
    }

    private fun sendAcceptMail(tutorEmail: String, studentEmail: String, bookingId: Long) {
        val (tutorSubject, tutorText) = makeAcceptEmail(bookingId)
        val (studentSubject, studentText) = makeAcceptMailStudent(bookingId)
        emailService.sendMail(tutorEmail, tutorSubject, tutorText)
        emailService.sendMail(studentEmail, studentSubject, studentText)
    }

}