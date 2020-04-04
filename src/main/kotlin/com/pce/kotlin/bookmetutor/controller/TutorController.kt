package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.phone.CreatePhoneDto
import com.pce.kotlin.bookmetutor.model.dto.util.CompletionRequest
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.service.BookingService
import com.pce.kotlin.bookmetutor.service.EmailService
import com.pce.kotlin.bookmetutor.service.TutorService
import com.pce.kotlin.bookmetutor.util.BookingStatus
import com.pce.kotlin.bookmetutor.util.Constants
import com.pce.kotlin.bookmetutor.util.makeAcceptEmail
import com.pce.kotlin.bookmetutor.util.makeRejectEmail
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/tutor")
class TutorController(val tutorService: TutorService, val bookingService: BookingService, val emailService: EmailService) {

    @GetMapping("/{email}")
    fun getTutorInfo(@PathVariable email: String): ResponseEntity<Response> {
        return tutorService.retrieveTutor(email)?.let {
            ResponseEntity(Response(description = "tutor information", payload = it.toDto()), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.TUTOR_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{email}/address")
    fun getTutorAddress(@PathVariable email: String): ResponseEntity<Response> {
        return tutorService.retrieveTutor(email)?.let {
            ResponseEntity(Response(description = "tutor address", payload = it.address?.toDto()), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.TUTOR_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{email}/booking")
    fun getTutorBookingList(@PathVariable email: String): ResponseEntity<Response> {
        return tutorService.retrieveTutor(email)?.let {
            ResponseEntity(Response(description = "assigned bookings", payload = it.bookings.mapNotNull { booking -> booking.toDto() }), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.TUTOR_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{email}/booking/{id}")
    fun getTutorBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<Response> {
        return tutorService.retrieveTutor(email)?.let { tutor ->
            tutor.bookings.find { it.id == id }?.toDto()?.let {
                ResponseEntity(Response(description = "tutor's address", payload = it), HttpStatus.OK)
            } ?: ResponseEntity(Response(Constants.BOOKING_NOT_FOUND), HttpStatus.NOT_FOUND)
        } ?: ResponseEntity(Response(Constants.TUTOR_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{email}/phone")
    fun getTutorPhone(@PathVariable email: String): ResponseEntity<Response> {
        return tutorService.retrieveTutor(email)?.let {
            ResponseEntity(Response(description = "tutor's phones", payload = it.phones.map { phone -> phone }), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.TUTOR_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @PostMapping("/{email}/phone")
    fun createTutorPhone(@PathVariable email: String, @RequestBody request: CreatePhoneDto): ResponseEntity<Response> {
        return tutorService.addTutorPhone(email, request.phone)?.let {
            ResponseEntity(Response(description = "updated phone list", payload = it.phones.map { phone -> phone }), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.TUTOR_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{email}/booking/{id}/accept")
    fun acceptBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<Response> {
        return bookingService.updateBooking(id,
                UpdateBookingDto(null, null, null, null, null, null, null, null, null, BookingStatus.ACCEPTED.name, null))?.let {
            val (subject, text) = makeAcceptEmail()
            emailService.sendMail(email, subject, text)
            ResponseEntity(Response(description = "booking accepted"), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.BOOKING_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{email}/booking/{id}/reject")
    fun rejectBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<Response> {
        return bookingService.updateBooking(id, UpdateBookingDto(null, null, null, null, null, null, null, null, null, null, email))?.let {
            val booking = bookingService.assignBookingTutor(id)
            booking?.tutor?.let {
                val (subject, text) = makeRejectEmail()
                emailService.sendMail(it.email, subject, text)
            } ?: run {
                booking?.student?.let {
                    emailService.sendMail(it.email, "Notice regarding booking", "Sorry for inconvenience, please issue a refund")
                }
            }
            ResponseEntity(Response(description = "booking rejected"), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.BOOKING_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{email}/booking/{id}/start")
    fun completeBooking(@PathVariable email: String, @PathVariable id: Long, @RequestBody request: CompletionRequest): ResponseEntity<Response>? {
        val secret = bookingService.retrieveBooking(id)?.secret
        secret?.let {
            if (it == request.secret) {
                return bookingService.updateBooking(id,
                        UpdateBookingDto(null, null, LocalDateTime.now(), null, null, null, null, null, null, null, null))?.let {
                    ResponseEntity(Response(description = "booking started"), HttpStatus.OK)
                }
            }
        }
        return ResponseEntity(Response(Constants.BOOKING_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{email}/booking/{id}/end")
    fun startBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<Response> {
        return bookingService.updateBooking(id, UpdateBookingDto(null, null, null, LocalDateTime.now(), null, null, null, null, null, BookingStatus.COMPLETED.name, null))?.let {
            ResponseEntity(Response(description = "booking completed"), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.BOOKING_NOT_FOUND), HttpStatus.NOT_FOUND)
    }
}