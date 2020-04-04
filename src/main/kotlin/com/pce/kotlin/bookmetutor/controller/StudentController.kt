package com.pce.kotlin.bookmetutor.controller

import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.booking.CreateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.phone.CreatePhoneDto
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import com.pce.kotlin.bookmetutor.service.BookingService
import com.pce.kotlin.bookmetutor.service.EmailService
import com.pce.kotlin.bookmetutor.service.StudentService
import com.pce.kotlin.bookmetutor.util.Constants
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/student")
class StudentController(val studentService: StudentService, val bookingService: BookingService, val emailService: EmailService) {

    @GetMapping("/{email}")
    fun getStudentInfo(@PathVariable email: String): ResponseEntity<Response> {
        return studentService.retrieveStudent(email)?.let {
            ResponseEntity(Response(description = "student information", payload = it.toDto()), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{email}/address")
    fun getStudentAddressList(@PathVariable email: String): ResponseEntity<Response> {
        return studentService.retrieveStudent(email)?.let { student ->
            ResponseEntity(Response(description = "Student's addresses", payload = student.addresses.mapNotNull { it.toDto() }), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{email}/address/{id}")
    fun getStudentAddress(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<Response> {
        return studentService.retrieveStudent(email)?.let { student ->
            student.addresses.find { it.id == id }?.toDto()?.let {
                ResponseEntity(Response(description = "student's address", payload = it), HttpStatus.OK)
            } ?: ResponseEntity(Response(Constants.ADDRESS_NOT_FOUND), HttpStatus.NOT_FOUND)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{email}/booking")
    fun getStudentBookingList(@PathVariable email: String): ResponseEntity<Response> {
        return studentService.retrieveStudent(email)?.let { student ->
            ResponseEntity(Response(description = "Student's bookings", payload = student.bookings.mapNotNull { it.toDto() }), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{email}/booking/{id}")
    fun getStudentBooking(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<Response> {
        return studentService.retrieveStudent(email)?.let { student ->
            student.bookings.find { it.id == id }?.toDto()?.let {
                ResponseEntity(Response(description = "student's address", payload = it), HttpStatus.OK)
            } ?: ResponseEntity(Response(Constants.BOOKING_NOT_FOUND), HttpStatus.NOT_FOUND)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{email}/phone")
    fun getStudentPhone(@PathVariable email: String): ResponseEntity<Response> {
        return studentService.retrieveStudent(email)?.let { student ->
            ResponseEntity(Response(description = "student's phone numbers", payload = student.phones.map { it }), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @PostMapping("/{email}/address")
    fun createStudentAddress(@PathVariable email: String, @RequestBody request: CreateAddressDto): ResponseEntity<Response> {
        return studentService.addStudentAddress(email, request)?.let { student ->
            ResponseEntity(Response(description = "updated list of addresses", payload = student.addresses.mapNotNull { it.toDto() }), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @PostMapping("/{email}/booking")
    fun createStudentBooking(@PathVariable email: String, @RequestBody request: CreateBookingDto): ResponseEntity<Response> {
        return bookingService.createBooking(email, request)?.let {
            ResponseEntity(Response(description = "booking information", payload = it.toDto()), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @PostMapping("/{email}/phone")
    fun createStudentPhone(@PathVariable email: String, @RequestBody request: CreatePhoneDto): ResponseEntity<Response> {
        return studentService.addStudentPhone(email, request.phone)?.let {
            ResponseEntity(Response(description = "updated phone list", payload = it.phones.map { phone -> phone }), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{email}/address/{id}")
    fun updateStudentAddress(@PathVariable email: String, @PathVariable id: Long, @RequestBody request: UpdateAddressDto): ResponseEntity<Response> {
        return studentService.updateStudentAddress(id, request)?.let {
            ResponseEntity(Response(description = "updated address", payload = it.toDto()), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{email}/booking/{id}")
    fun updateStudentBooking(@PathVariable email: String, @PathVariable id: Long, request: UpdateBookingDto): ResponseEntity<Response> {
        return bookingService.updateBooking(id, request)?.let {
            ResponseEntity(Response(description = "updated booking", payload = it.toDto()), HttpStatus.OK)
        } ?: ResponseEntity(Response(Constants.USER_NOT_FOUND), HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/{email}/address/{id}")
    fun deleteAddress(@PathVariable email: String, @PathVariable id: Long): ResponseEntity<Response> {
        if (studentService.removeStudentAddress(id))
            return ResponseEntity(Response("address deleted"), HttpStatus.OK)
        return ResponseEntity(Response(Constants.ADDRESS_NOT_FOUND), HttpStatus.NOT_FOUND)
    }
}