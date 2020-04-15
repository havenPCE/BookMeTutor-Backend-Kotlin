package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.booking.CreateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.phone.CreatePhoneDto
import com.pce.kotlin.bookmetutor.model.dto.student.UpdateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.service.BookingService
import com.pce.kotlin.bookmetutor.service.StudentService
import com.pce.kotlin.bookmetutor.util.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/students")
class StudentController(val studentService: StudentService, val bookingService: BookingService) : HandlesError() {

    @GetMapping("/{email}")
    fun retrieveStudent(@PathVariable email: String): ResponseEntity<out Response> {
        studentService.retrieveStudent(email)?.let {
            return response(status = HttpStatus.OK, message = STUDENT_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

    @GetMapping("/{email}/phones")
    fun retrievePhones(@PathVariable email: String): ResponseEntity<out Response> {
        studentService.retrieveStudent(email)?.let {
            return response(status = HttpStatus.OK, message = PHONE_INFO, payload = it.phones)
        }
        return response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

    @GetMapping("/{email}/addresses")
    fun retrieveAddressList(@PathVariable email: String): ResponseEntity<out Response> {
        studentService.retrieveStudent(email)?.let { student ->
            return response(status = HttpStatus.OK, message = ADDRESS_INFO, payload = student.addresses)
        }
        return response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

    @GetMapping("/{email}/addresses/{id}")
    fun retrieveAddress(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<out Response> {
        bookingService.retrieveBooking(id)?.let {
            return response(status = HttpStatus.OK, message = ADDRESS_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = ADDRESS_NOT_FOUND)
    }

    @GetMapping("/{email}/bookings")
    fun retrieveBookingsList(@PathVariable email: String): ResponseEntity<out Response> {
        studentService.retrieveStudent(email)?.let { student ->
            return response(status = HttpStatus.OK, message = BOOKING_INFO, payload = student.bookings)
        }
        return response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

    @GetMapping("/{email}/bookings/{id}")
    fun retrieveBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<out Response> {
        bookingService.retrieveBooking(id)?.let {
            return response(status = HttpStatus.OK, message = BOOKING_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = BOOKING_NOT_FOUND)
    }

    @PostMapping("/{email}/phones")
    fun addNewPhone(@PathVariable email: String, @RequestBody dto: CreatePhoneDto): ResponseEntity<out Response> {
        studentService.addStudentPhone(email, dto.phone)?.let {
            return response(status = HttpStatus.OK, message = PHONE_INFO, payload = it.phones)
        }
        return response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

    @PostMapping("/{email}/addresses")
    fun addNewAddress(@PathVariable email: String, @RequestBody dto: CreateAddressDto): ResponseEntity<out Response> {
        studentService.addStudentAddress(email, dto)?.let {
            return response(status = HttpStatus.OK, message = ADDRESS_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

    @PostMapping("/{email}/bookings")
    fun addNewBooking(@PathVariable email: String, @RequestBody dto: CreateBookingDto): ResponseEntity<out Response> {
        bookingService.createBooking(email, dto)?.let {
            return response(status = HttpStatus.OK, message = BOOKING_INFO, payload = it)
        }
        return response(status = HttpStatus.INTERNAL_SERVER_ERROR, message = TASK_FAILED)
    }

    @PutMapping("/{email}")
    fun updateStudentInfo(@PathVariable email: String, @RequestBody dto: UpdateStudentDto): ResponseEntity<out Response> {
        studentService.updateStudent(email, dto)?.let {
            return response(status = HttpStatus.OK, message = STUDENT_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

    @PutMapping("/{email}/addresses/{id}")
    fun updateStudentAddress(@PathVariable email: String, @PathVariable id: Long, @RequestBody dto: UpdateAddressDto): ResponseEntity<out Response> {
        studentService.updateStudentAddress(id, dto)?.let {
            return response(status = HttpStatus.OK, message = ADDRESS_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = ADDRESS_NOT_FOUND)
    }

    @PutMapping("/{email}/bookings/{id}/reschedule")
    fun rescheduleBooking(@PathVariable email: String, @PathVariable id: Long, @RequestBody dto: UpdateBookingDto): ResponseEntity<out Response> {
        bookingService.updateBooking(id, dto.copy(rescheduled = true))?.let {
            return response(status = HttpStatus.OK, message = BOOKING_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = BOOKING_NOT_FOUND)
    }

    @PutMapping("/{email}/bookings/{id}/cancel")
    fun cancelBooking(@PathVariable email: String, @PathVariable id: Long, @RequestBody dto: UpdateBookingDto): ResponseEntity<out Response> {
        bookingService.updateBooking(id, dto.copy(status = BookingStatus.CANCELLED.name))?.let {
            return response(status = HttpStatus.OK, message = BOOKING_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = BOOKING_NOT_FOUND)
    }

    @PutMapping("/{email}/bookings/{id}/review")
    fun reviewBooking(@PathVariable email: String, @PathVariable id: Long, @RequestBody dto: UpdateBookingDto): ResponseEntity<out Response> {
        bookingService.updateBooking(id, dto)?.let {
            return response(status = HttpStatus.OK, message = BOOKING_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = BOOKING_NOT_FOUND)
    }

    @PutMapping("/{email}/bookings/{id}/topics")
    fun changeBookingTopics(@PathVariable email: String, @PathVariable id: Long, @RequestBody dto: UpdateBookingDto): ResponseEntity<out Response> {
        bookingService.updateBooking(id, dto)?.let {
            return response(status = HttpStatus.OK, message = BOOKING_INFO, payload = it)
        }
        return response(status = HttpStatus.NOT_FOUND, message = BOOKING_NOT_FOUND)
    }

    @DeleteMapping("/{email}/bookings/{id}")
    fun removeBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<out Response> {
        return if (bookingService.removeBooking(id)) {
            response(status = HttpStatus.OK, message = TASK_SUCCESSFUL)
        } else response(status = HttpStatus.NOT_FOUND, message = BOOKING_NOT_FOUND)
    }

    @DeleteMapping("/{email}/phones/{phone}")
    fun removePhone(@PathVariable email: String, @PathVariable phone: String): ResponseEntity<out Response> {
        studentService.removeStudentPhone(email, phone)?.let {
            return response(status = HttpStatus.OK, message = PHONE_INFO, payload = it.phones)
        }
        return response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

    @DeleteMapping("/{email}/addresses/{id}")
    fun removeAddress(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<out Response> {
        return if (studentService.removeStudentAddress(id)) {
            response(status = HttpStatus.OK, message = TASK_SUCCESSFUL)
        } else response(status = HttpStatus.NOT_FOUND, message = ADDRESS_NOT_FOUND)
    }

    @DeleteMapping("/{email}")
    fun disableStudent(@PathVariable email: String): ResponseEntity<out Response> {
        studentService.updateStudent(email, UpdateStudentDto(verified = false))?.let {
            return response(status = HttpStatus.OK, message = ACCOUNT_DISABLED)
        }
        return response(status = HttpStatus.NOT_FOUND, message = STUDENT_NOT_FOUND)
    }

}