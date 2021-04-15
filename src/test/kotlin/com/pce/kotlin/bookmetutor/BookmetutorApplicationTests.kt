package com.pce.kotlin.bookmetutor

import com.pce.kotlin.bookmetutor.model.dao.*
import com.pce.kotlin.bookmetutor.repository.*
import com.pce.kotlin.bookmetutor.service.AccountService
import com.pce.kotlin.bookmetutor.util.Board
import com.pce.kotlin.bookmetutor.util.Gender
import com.pce.kotlin.bookmetutor.util.PaymentMethod
import com.pce.kotlin.bookmetutor.util.SubjectName
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors.assertEquals
import java.time.LocalDateTime

@SpringBootTest
class BookmetutorApplicationTests {

    @Autowired
    lateinit var adminRepo: AdminRepo

    @Autowired
    lateinit var subjectRepo: SubjectRepo

    @Autowired
    lateinit var tutorQualificationRepo: TutorQualificationRepo

    @Autowired
    lateinit var tutorAddressRepo: TutorAddressRepo

    @Autowired
    lateinit var invoiceRepo: InvoiceRepo

    @Autowired
    lateinit var bookingAddressRepo: BookingAddressRepo

    @Autowired
    lateinit var bookingRepo: BookingRepo

    @Autowired
    lateinit var tutorRepo: TutorRepo

    @Autowired
    lateinit var studentRepo: StudentRepo

    @Autowired
    lateinit var accountService: AccountService


    @Test
    @Disabled
    fun `Test For AdminRepo`() {
        val admin = Admin(id = 1000, email = "def@gmail.com", password = "password")
        val updatedAdmin = admin.copy(password = "new-pass")
        val falseAdmin = admin.copy(id = 420, email = "abc")
        assertAll(
            {
                assertEquals("SAVE METHOD SUCCESS", admin, adminRepo.save(admin))
                assertEquals("SAVE METHOD FAILURE", null, adminRepo.save(admin))
                assertEquals("FIND METHOD SUCCESS", admin, adminRepo.findByEmail(admin.email))
                assertEquals("FIND METHOD FAILURE", null, adminRepo.findByEmail(falseAdmin.email))
                assertEquals("UPDATE METHOD SUCCESS", updatedAdmin, adminRepo.update(updatedAdmin))
                assertEquals("UPDATE METHOD FAILURE", null, adminRepo.update(falseAdmin))
                assertEquals("DELETE METHOD SUCCESS", true, adminRepo.deleteByEmail(admin.email))
                assertEquals("DELETE METHOD FAILURE", false, adminRepo.deleteByEmail(admin.email))
            }
        )
    }

    @Test
    @Disabled
    fun `Test For SubjectRepo`() {
        val subject = Subject(
            id = 2000,
            subjectName = SubjectName.PHYSICS,
            classNumber = 8,
            topics = mutableSetOf("1st", "2nd", "3rd")
        )
        val updatedSubject = subject.copy(topics = mutableSetOf("new", "topics"))
        val falseSubject = subject.copy(id = 420, classNumber = 17)
        assertAll(
            {
                assertEquals("SAVE METHOD SUCCESS", subject, subjectRepo.save(subject))
                assertEquals("SAVE METHOD FAILURE", null, subjectRepo.save(subject))
                assertEquals("FIND METHOD SUCCESS", subject, subjectRepo.findById(subject.id))
                assertEquals("FIND METHOD FAILURE", null, subjectRepo.findById(falseSubject.id))
                assertEquals(
                    "SPECIAL FIND METHOD SUCCESS",
                    subject,
                    subjectRepo.findBySubjectNameAndClassNumber(subject.subjectName, subject.classNumber)
                )
                assertEquals(
                    "SPECIAL FIND METHOD FAILURE",
                    null,
                    subjectRepo.findBySubjectNameAndClassNumber(falseSubject.subjectName, falseSubject.classNumber)
                )
                assertEquals("UPDATE METHOD SUCCESS", updatedSubject, subjectRepo.update(updatedSubject))
                assertEquals("UPDATE METHOD FAILURE", null, subjectRepo.update(falseSubject))
                assertEquals("DELETE METHOD SUCCESS", true, subjectRepo.deleteById(subject.id))
                assertEquals("DELETE METHOD FAILURE", false, subjectRepo.deleteById(subject.id))
            }
        )
    }

    @Test
    @Disabled
    fun `Test For TutorQualificationRepo`() {
        val qualification = TutorQualification(
            id = 3000,
            degree = "B.Sc",
            university = "Sambalpur University",
            percentile = 72.50,
            tutorId = 999L
        )
        val updatedQualification = qualification.copy(percentile = 66.60)
        val falseQualification = qualification.copy(id = 420)
        val tutorId = 999L
        val falseTutorId = 420L
        assertAll(
            {
                assertEquals("SAVE METHOD SUCCESS", qualification, tutorQualificationRepo.save(tutorId, qualification))
                assertEquals("SAVE METHOD FAILURE", null, tutorQualificationRepo.save(tutorId, qualification))
                assertEquals("FIND METHOD SUCCESS", qualification, tutorQualificationRepo.findById(qualification.id))
                assertEquals("FIND METHOD FAILURE", null, tutorQualificationRepo.findById(falseQualification.id))
                assertEquals(
                    "UPDATE METHOD SUCCESS",
                    updatedQualification,
                    tutorQualificationRepo.update(updatedQualification)
                )
                assertEquals("UPDATE METHOD FAILURE", null, tutorQualificationRepo.update(falseQualification))
                assertEquals(
                    "FIND BY TUTOR SUCCESS",
                    updatedQualification,
                    tutorQualificationRepo.findByTutorId(tutorId)
                )
                assertEquals("FIND BY TUTOR FAILURE", null, tutorQualificationRepo.findByTutorId(falseTutorId))
                assertEquals("DELETE METHOD SUCCESS", true, tutorQualificationRepo.deleteById(qualification.id))
                assertEquals("DELETE METHOD FAILURE", false, tutorQualificationRepo.deleteById(qualification.id))
            }
        )
    }

    @Test
    @Disabled
    fun `Test For TutorAddressRepo`() {
        val address = TutorAddress(id = 4000, line1 = "A", line2 = "B", city = "E", pinCode = "12345", tutorId = 999L)
        val updateAddress = address.copy(landmark = "C")
        val falseAddress = address.copy(id = 420)
        val tutorId = 999L
        val falseTutorId = 420L

        assertAll(
            {
                assertEquals("SAVE METHOD SUCCESS", address, tutorAddressRepo.save(tutorId, address))
                assertEquals("SAVE METHOD FAILURE", null, tutorAddressRepo.save(tutorId, address))
                assertEquals("FIND METHOD SUCCESS", address, tutorAddressRepo.findById(address.id))
                assertEquals("FIND METHOD FAILURE", null, tutorAddressRepo.findById(falseAddress.id))
                assertEquals("UPDATE METHOD SUCCESS", updateAddress, tutorAddressRepo.update(updateAddress))
                assertEquals("UPDATE METHOD FAILURE", null, tutorAddressRepo.update(falseAddress))
                assertEquals("FIND BY TUTOR SUCCESS", updateAddress, tutorAddressRepo.findByTutorId(tutorId))
                assertEquals("FIND BY TUTOR FAILURE", null, tutorAddressRepo.findByTutorId(falseTutorId))
                assertEquals("DELETE METHOD SUCCESS", true, tutorAddressRepo.deleteById(address.id))
                assertEquals("DELETE METHOD FAILURE", false, tutorAddressRepo.deleteById(address.id))
            }
        )
    }

    @Test
    @Disabled
    fun `Test For InvoiceRepo`() {
        val invoice =
            Invoice(id = 5000, amount = 200.00, method = PaymentMethod.CREDIT_CARD, summary = "abc", bookingId = 999L)
        val updatedInvoice = invoice.copy(amount = 300.00, method = PaymentMethod.UPI)
        val falseInvoice = invoice.copy(id = 420)
        val bookingId = 999L
        val falseBookingId = 420L
        assertAll(
            {
                assertEquals("SAVE METHOD SUCCESS", invoice, invoiceRepo.save(bookingId, invoice))
                assertEquals("SAVE METHOD FAILURE", null, invoiceRepo.save(bookingId, invoice))
                assertEquals("FIND METHOD SUCCESS", invoice, invoiceRepo.findById(invoice.id))
                assertEquals("FIND METHOD FAILURE", null, invoiceRepo.findById(falseInvoice.id))
                assertEquals("UPDATE METHOD SUCCESS", updatedInvoice, invoiceRepo.update(updatedInvoice))
                assertEquals("UPDATE METHOD FAILURE", null, invoiceRepo.update(falseInvoice))
                assertEquals("FIND BY TUTOR SUCCESS", updatedInvoice, invoiceRepo.findByBookingId(bookingId))
                assertEquals("FIND BY TUTOR FAILURE", null, invoiceRepo.findByBookingId(falseBookingId))
                assertEquals("DELETE METHOD SUCCESS", true, invoiceRepo.deleteById(invoice.id))
                assertEquals("DELETE METHOD FAILURE", false, invoiceRepo.deleteById(invoice.id))
            }
        )
    }

    @Test
    @Disabled
    fun `Test For BookingAddressRepo`() {
        val address =
            BookingAddress(id = 6000, line1 = "A", line2 = "B", city = "E", pinCode = "12345", bookingId = 999L)
        val updateAddress = address.copy(landmark = "C")
        val falseAddress = address.copy(id = 420)
        val bookingId = 999L
        val falseBookingId = 420L

        assertAll(
            {
                assertEquals("SAVE METHOD SUCCESS", address, bookingAddressRepo.save(bookingId, address))
                assertEquals("SAVE METHOD FAILURE", null, bookingAddressRepo.save(bookingId, address))
                assertEquals("FIND METHOD SUCCESS", address, bookingAddressRepo.findById(address.id))
                assertEquals("FIND METHOD FAILURE", null, bookingAddressRepo.findById(falseAddress.id))
                assertEquals("UPDATE METHOD SUCCESS", updateAddress, bookingAddressRepo.update(updateAddress))
                assertEquals("UPDATE METHOD FAILURE", null, bookingAddressRepo.update(falseAddress))
                assertEquals("FIND BY TUTOR SUCCESS", updateAddress, bookingAddressRepo.findByBookingId(bookingId))
                assertEquals("FIND BY TUTOR FAILURE", null, bookingAddressRepo.findByBookingId(falseBookingId))
                assertEquals("DELETE METHOD SUCCESS", true, bookingAddressRepo.deleteById(address.id))
                assertEquals("DELETE METHOD FAILURE", false, bookingAddressRepo.deleteById(address.id))
            }
        )
    }

    @Test
    @Disabled
    fun `Test For Booking`() {
        val booking = Booking(
            id = 7000,
            board = Board.CBSE,
            classNumber = 8,
            rejects = setOf("abc"),
            deadline = LocalDateTime.of(2020, 4, 11, 3, 30, 0),
            scheduledTime = LocalDateTime.of(2020, 4, 11, 3, 30, 30),
            subject = SubjectName.PHYSICS.name,
            topics = setOf("topic 1", "topic 2"),
            invoice = Invoice(
                id = 1,
                amount = 200.00,
                method = PaymentMethod.CREDIT_CARD,
                summary = "abc",
                bookingId = 7000
            ),
            address = BookingAddress(id = 2, line1 = "A", line2 = "B", city = "E", pinCode = "12345", bookingId = 7000),
            studentId = 999L,
            tutorId = 999L
        )
        val updatedBooking = booking.copy(topics = setOf("new Topic"))
        val falseBooking = booking.copy(id = 420)
        val studentId = 999L
        val tutorId = 999L
        val falseStudentId = 420L
        val falseTutorId = 420L

        assertAll(
            {
                assertEquals("SAVE METHOD SUCCESS", booking, bookingRepo.save(studentId, tutorId, booking))
                assertEquals("SAVE METHOD FAILURE", null, bookingRepo.save(studentId, tutorId, booking))
                assertEquals("FIND METHOD SUCCESS", booking, bookingRepo.findById(booking.id))
                assertEquals("FIND METHOD FAILURE", null, bookingRepo.findById(falseBooking.id))
                assertEquals("UPDATE METHOD SUCCESS", updatedBooking, bookingRepo.update(updatedBooking))
                assertEquals("UPDATE METHOD FAILURE", null, bookingRepo.update(falseBooking))
                assertEquals(
                    "FIND BY TUTOR METHOD SUCCESS",
                    true,
                    bookingRepo.findByTutorId(tutorId).contains(updatedBooking)
                )
                assertEquals(
                    "FIND BY TUTOR METHOD FAILURE",
                    false,
                    bookingRepo.findByTutorId(falseTutorId).contains(updatedBooking)
                )
                assertEquals(
                    "FIND BY STUDENT ID METHOD SUCCESS",
                    true,
                    bookingRepo.findByStudentId(studentId).contains(updatedBooking)
                )
                assertEquals(
                    "FIND BY STUDENT ID FAILURE",
                    false,
                    bookingRepo.findByStudentId(falseStudentId).contains(updatedBooking)
                )
                assertEquals("FIND ALL METHOD", true, bookingRepo.findAll().contains(updatedBooking))
                assertEquals("DELETE METHOD SUCCESS", true, bookingRepo.deleteById(booking.id))
                assertEquals("DELETE METHOD FAILURE", false, bookingRepo.deleteById(booking.id))
            }
        )
    }

    @Test
    @Disabled
    fun `Test For TutorRepo`() {
        val maleTutor1 = Tutor(
            id = 8000,
            email = "m1@email.com",
            password = "password",
            gender = Gender.MALE,
            lastPicked = LocalDateTime.of(2020, 4, 11, 3, 30, 0),
            firstName = "male1",
            phones = setOf("m1"),
            registered = LocalDateTime.of(2020, 4, 11, 3, 30, 0),
            address = TutorAddress(id = 101, line1 = "m1 l1", city = "ROURKELA", pinCode = "m1 p1", tutorId = 8000),
            qualification = TutorQualification(
                id = 201,
                degree = "m1 d1",
                university = "m1 u1",
                percentile = 50.00,
                tutorId = 8000
            )
        )
        val femaleTutor1 = Tutor(
            id = 9000,
            email = "f1@email.com",
            password = "password",
            gender = Gender.FEMALE,
            lastPicked = LocalDateTime.of(2020, 4, 11, 3, 40, 0),
            firstName = "female1",
            phones = setOf("f1"),
            registered = LocalDateTime.of(2020, 4, 11, 3, 40, 0),
            address = TutorAddress(id = 102, line1 = "f1 l1", city = "ROURKELA", pinCode = "f1 p1", tutorId = 9000),
            qualification = TutorQualification(
                id = 202,
                degree = "f1 d1",
                university = "f1 u1",
                percentile = 50.00,
                tutorId = 9000
            )
        )
        val maleTutor2 = Tutor(
            id = 10000,
            email = "m2@email.com",
            password = "password",
            gender = Gender.MALE,
            lastPicked = LocalDateTime.of(2020, 4, 11, 3, 50, 0),
            firstName = "male2",
            phones = setOf("m2"),
            registered = LocalDateTime.of(2020, 4, 11, 3, 50, 0),
            address = TutorAddress(id = 103, line1 = "m2 l1", city = "ROURKELA", pinCode = "m2 p1", tutorId = 10000),
            qualification = TutorQualification(
                id = 203,
                degree = "m2 d1",
                university = "m2 u1",
                percentile = 50.00,
                tutorId = 10000
            )
        )
        val femaleTutor2 = Tutor(
            id = 11000,
            email = "f2@email.com",
            password = "password",
            gender = Gender.FEMALE,
            lastPicked = LocalDateTime.of(2020, 4, 11, 4, 0, 0),
            firstName = "female2",
            phones = setOf("f2"),
            registered = LocalDateTime.of(2020, 4, 11, 4, 0, 0),
            address = TutorAddress(id = 104, line1 = "m1 l1", city = "ROURKELA", pinCode = "m1 p1", tutorId = 11000),
            qualification = TutorQualification(
                id = 204,
                degree = "m1 d1",
                university = "m1 u1",
                percentile = 50.00,
                tutorId = 11000
            )
        )
        val falseTutor = maleTutor1.copy(id = 420, email = "false")
        val updatedTutor = maleTutor1.copy(firstName = "updatedM1")
        assertAll({
            assertEquals("M1 SAVE METHOD", maleTutor1, tutorRepo.save(maleTutor1))
            assertEquals("M2 SAVE METHOD", maleTutor2, tutorRepo.save(maleTutor2))
            assertEquals("F1 SAVE METHOD", femaleTutor1, tutorRepo.save(femaleTutor1))
            assertEquals("F2 SAVE METHOD", femaleTutor2, tutorRepo.save(femaleTutor2))
            assertEquals("SAVE METHOD FAILURE", null, tutorRepo.save(maleTutor1))
            assertEquals("UPDATE METHOD SUCCESS", updatedTutor, tutorRepo.update(updatedTutor))
            assertEquals("FIND BY EMAIL METHOD SUCCESS", updatedTutor, tutorRepo.findByEmail(updatedTutor.email))
            assertEquals("FIND BY EMAIL METHOD FAILURE", null, tutorRepo.findByEmail(falseTutor.email))
            assertEquals(
                "FIND SUITABLE M1 SUCCESS",
                updatedTutor,
                tutorRepo.findTutorForAssignment("ROURKELA", rejects = emptyList())
            )
            assertEquals(
                "FIND SUITABLE M1 FAILURE",
                maleTutor2,
                tutorRepo.findTutorForAssignment("ROURKELA", rejects = listOf(updatedTutor.email, femaleTutor1.email))
            )
            assertEquals("DELETE METHOD FAILURE", false, tutorRepo.deleteByEmail(falseTutor.email))
            assertEquals("DELETE METHOD SUCCESS", true, tutorRepo.deleteByEmail(updatedTutor.email))
            assertEquals("DELETE METHOD SUCCESS", true, tutorRepo.deleteByEmail(maleTutor2.email))
            assertEquals("DELETE METHOD SUCCESS", true, tutorRepo.deleteByEmail(femaleTutor1.email))
            assertEquals("DELETE METHOD SUCCESS", true, tutorRepo.deleteByEmail(femaleTutor2.email))
        })
    }

    @Test
    @Disabled
    fun `Test For StudentRepo`() {
        val student = Student(
            id = 12000,
            email = "student@email.com",
            password = "password",
            firstName = "student",
            gender = Gender.FEMALE,
            phones = mutableSetOf("12345")
        )
        val updatedStudent = student.copy(
            phones = mutableSetOf("12345", "6789")
        )
        val falseStudent = student.copy(
            id = 420,
            email = "false@gmail.com"
        )
        assertAll(
            {
                assertEquals("SAVE METHOD SUCCESS", student, studentRepo.save(student))
                assertEquals("SAVE METHOD FAILURE", null, studentRepo.save(student))
                assertEquals("UPDATE METHOD SUCCESS", updatedStudent, studentRepo.update(updatedStudent))
                assertEquals("UPDATED METHOD FAILURE", null, studentRepo.update(falseStudent))
                assertEquals("FIND METHOD SUCCESS", updatedStudent, studentRepo.findByEmail(student.email))
                assertEquals("FIND METHOD FAILURE", null, studentRepo.findByEmail(falseStudent.email))
                assertEquals("DELETE METHOD SUCCESS", true, studentRepo.deleteByEmail(student.email))
                assertEquals("DELETE METHOD FAILURE", false, studentRepo.deleteByEmail(student.email))
            }
        )
    }

    @Test
    @Disabled
    fun `Check For Availability`() {
        val admin = Admin(
            id = 1,
            email = "admin@gmail.com",
            password = "password"
        )
        adminRepo.save(admin)

        val student = Student(
            id = 13000,
            email = "student@gmail.com",
            password = "password",
            firstName = "student",
            gender = Gender.MALE,
            phones = mutableSetOf("12345")
        )
        studentRepo.save(student)

        assertEquals("IS TAKEN", true, accountService.isPresent(admin.email))
        assertEquals("IS TAKEN", true, accountService.isPresent(student.email))
        assertEquals("NOT TAKEN", false, accountService.isPresent("new@gmail.com"))

        adminRepo.deleteByEmail(admin.email)
        studentRepo.deleteByEmail(student.email)
    }
}
