package com.pce.kotlin.bookmetutor.util

import com.pce.kotlin.bookmetutor.model.dao.*
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource


object AdminQuery {
    fun insert(admin: Admin) = Pair(
            "INSERT INTO public.admin(admin_id, admin_email, admin_password) VALUES (:id, :email, :password);",
            MapSqlParameterSource(mutableMapOf("id" to admin.id, "email" to admin.email, "password" to admin.password))
    )

    fun update(admin: Admin) = Pair(
            "UPDATE public.admin SET admin_email=:email, admin_password=:password WHERE admin_id = :id;",
            MapSqlParameterSource(mutableMapOf("id" to admin.id, "email" to admin.email, "password" to admin.password))
    )

    fun deleteByEmail(email: String) = Pair(
            "DELETE FROM public.admin WHERE admin_email = :email;",
            MapSqlParameterSource("email", email)
    )

    fun selectById(id: Long) = Pair(
            "SELECT admin_id, admin_email, admin_password FROM public.admin WHERE admin_id = :id;",
            MapSqlParameterSource("id", id)
    )

    fun selectByEmail(email: String) = Pair(
            "SELECT admin_id, admin_email, admin_password FROM public.admin WHERE admin_email = :email;",
            MapSqlParameterSource("email", email)
    )
}

object SubjectQuery {

    fun insertIntoSubject(subject: Subject) = Pair(
            "INSERT INTO public.subject(subject_id, subject_name, class_number) VALUES (:id, :subjectName, :classNumber);",
            MapSqlParameterSource(mutableMapOf("id" to subject.id, "subjectName" to subject.subjectName.name, "classNumber" to subject.classNumber))
    )

    fun insertIntoTopic(subjectId: Long) = "INSERT INTO public.subject_topic(subject_id, topic) VALUES (${subjectId},:topic);"
    fun updateToSubject(subject: Subject) = Pair(
            "UPDATE public.subject SET subject_name=:subjectName, class_number=:classNumber WHERE subject_id = :id;",
            MapSqlParameterSource(mutableMapOf("id" to subject.id, "subjectName" to subject.subjectName.name, "classNumber" to subject.classNumber))
    )

    fun deleteById(id: Long) = Pair(
            "DELETE FROM public.subject WHERE subject_id = :id",
            MapSqlParameterSource("id", id)
    )

    fun deleteByIdFromTopic(id: Long) = Pair(
            "DELETE FROM public.subject_topic WHERE subject_id = :id",
            MapSqlParameterSource("id", id)
    )

    fun selectByIdFromSubject(id: Long) = Pair(
            "SELECT subject_id, subject_name, class_number FROM public.subject WHERE subject_id = :id",
            MapSqlParameterSource("id", id)
    )

    fun selectByIdFromTopic(id: Long) = Pair(
            "SELECT topic FROM public.subject_topic WHERE subject_id = :id",
            MapSqlParameterSource("id", id)
    )

    fun selectBySubjectNameAndClassNumberFromSubject(subjectName: SubjectName, classNumber: Int) = Pair(
            "SELECT subject_id, subject_name, class_number FROM public.subject WHERE subject_name = :subjectName AND class_number = :classNumber",
            MapSqlParameterSource(mutableMapOf("subjectName" to subjectName.name, "classNumber" to classNumber))
    )
}

object StudentAddressQuery {
    fun selectById(id: Long) = Pair(
            "SELECT address_id, line_1, line_2, landmark, city, pin_code FROM public.student_address WHERE address_id = :id;",
            MapSqlParameterSource("id", id)
    )

    fun insert(studentId: Long, address: StudentAddress) = Pair(
            """INSERT INTO public.student_address (address_id, line_1, line_2, landmark, city, pin_code, student_id) VALUES (:id,:line1,:line2,:landmark,:city,:pinCode,:studentId);""",
            MapSqlParameterSource(mutableMapOf("id" to address.id, "line1" to address.line1, "line2" to address.line2, "landmark" to address.landmark, "city" to address.city, "pinCode" to address.pinCode, "studentId" to studentId))
    )

    fun update(address: StudentAddress) = Pair(
            """UPDATE public.student_address SET line_1=:line1, line_2=:line2, landmark=:landmark, city=:city, pin_code=:pinCode WHERE address_id = :id;""",
            MapSqlParameterSource(mutableMapOf("id" to address.id, "line1" to address.line1, "line2" to address.line2, "landmark" to address.landmark, "city" to address.city, "pinCode" to address.pinCode))
    )

    fun deleteById(id: Long) = Pair(
            """DELETE FROM public.student_address WHERE address_id = :id""",
            MapSqlParameterSource("id", id)
    )

    fun selectByStudentId(studentId: Long) = Pair(
            """SELECT address_id, line_1, line_2, landmark, city, pin_code FROM public.student_address WHERE student_id = :studentId;""",
            MapSqlParameterSource("studentId", studentId)
    )
}

object BookingAddressQuery {
    fun selectById(id: Long) = Pair(
            """SELECT address_id, line_1, line_2, landmark, city, pin_code FROM public.booking_address WHERE address_id = :id;""",
            MapSqlParameterSource("id", id)
    )

    fun selectByBookingId(bookingId: Long) = Pair(
            """SELECT address_id, line_1, line_2, landmark, city, pin_code FROM public.booking_address WHERE booking_id = :bookingId;""",
            MapSqlParameterSource("bookingId", bookingId)
    )

    fun insert(bookingId: Long, address: BookingAddress) = Pair(
            """INSERT INTO public.booking_address(address_id, line_1, line_2, landmark, city, pin_code, booking_id) VALUES (:id,:line1,:line2,:landmark,:city,:pinCode,:bookingId);""",
            MapSqlParameterSource(mutableMapOf("id" to address.id, "line1" to address.line1, "line2" to address.line2, "landmark" to address.landmark, "city" to address.city, "pinCode" to address.pinCode, "bookingId" to bookingId))
    )

    fun update(address: BookingAddress) = Pair(
            """UPDATE public.booking_address SET line_1=:line1, line_2=:line2, landmark=:landmark, city=:city, pin_code=:pinCode WHERE address_id = :id;""",
            MapSqlParameterSource(mutableMapOf("id" to address.id, "line1" to address.line1, "line2" to address.line2, "landmark" to address.landmark, "city" to address.city, "pinCode" to address.pinCode))
    )

    fun deleteById(id: Long) = Pair(
            """DELETE FROM public.booking_address WHERE address_id = :id""",
            MapSqlParameterSource("id", id)
    )
}

object TutorAddressQuery {
    fun selectById(id: Long) = Pair(
            """SELECT address_id, line_1, line_2, landmark, city, pin_code FROM public.tutor_address WHERE address_id = :id;""",
            MapSqlParameterSource("id", id)
    )

    fun insert(tutorId: Long, address: TutorAddress) = Pair(
            """INSERT INTO public.tutor_address(address_id, line_1, line_2, landmark, city, pin_code, tutor_id) VALUES (:id,:line1,:line2,:landmark,:city,:pinCode,:tutorId);""",
            MapSqlParameterSource(mutableMapOf("id" to address.id, "line1" to address.line1, "line2" to address.line2, "landmark" to address.landmark, "city" to address.city, "pinCode" to address.pinCode, "tutorId" to tutorId))
    )

    fun update(address: TutorAddress) = Pair(
            """UPDATE public.tutor_address SET line_1=:line1, line_2=:line2, landmark=:landmark, city=:city, pin_code=:pinCode WHERE address_id = :id;""",
            MapSqlParameterSource(mutableMapOf("id" to address.id, "line1" to address.line1, "line2" to address.line2, "landmark" to address.landmark, "city" to address.city, "pinCode" to address.pinCode))
    )

    fun deleteById(id: Long) = Pair(
            """DELETE FROM public.tutor_address WHERE address_id = :id""",
            MapSqlParameterSource("id", id)
    )

    fun selectByTutorId(tutorId: Long) = Pair(
            """SELECT address_id, line_1, line_2, landmark, city, pin_code FROM public.tutor_address WHERE tutor_id = :tutorId;""",
            MapSqlParameterSource("tutorId", tutorId)
    )
}

object TutorQualificationQuery {
    fun selectById(id: Long) = Pair(
            """SELECT qualification_id, degree, university, percentile FROM public.tutor_qualification WHERE qualification_id = :id;""",
            MapSqlParameterSource("id", id)
    )

    fun insert(tutorId: Long, qualification: TutorQualification) = Pair(
            """INSERT INTO public.tutor_qualification(qualification_id, degree, university, percentile, tutor_id) VALUES (:id, :degree, :university, :percentile, :tutorId);""",
            MapSqlParameterSource(mutableMapOf("id" to qualification.id, "degree" to qualification.degree, "university" to qualification.university, "percentile" to qualification.percentile, "tutorId" to tutorId))
    )

    fun update(qualification: TutorQualification) = Pair(
            """UPDATE public.tutor_qualification SET degree=:degree, university=:university, percentile=:percentile WHERE qualification_id = :id;""",
            MapSqlParameterSource(mutableMapOf("id" to qualification.id, "degree" to qualification.degree, "university" to qualification.university, "percentile" to qualification.percentile))
    )

    fun deleteById(id: Long) = Pair(
            """DELETE FROM public.tutor_qualification WHERE qualification_id = :id;""",
            MapSqlParameterSource("id", id)
    )

    fun selectByTutorId(tutorId: Long) = Pair(
            """SELECT qualification_id, degree, university, percentile FROM public.tutor_qualification WHERE tutor_id = :tutorId;""",
            MapSqlParameterSource("tutorId", tutorId)
    )
}

object InvoiceQuery {
    fun selectById(id: Long) = Pair(
            """SELECT invoice_id, amount, method, summary FROM public.booking_invoice WHERE invoice_id = :id;""",
            MapSqlParameterSource("id", id)
    )

    fun insert(bookingId: Long, invoice: Invoice) = Pair(
            """INSERT INTO public.booking_invoice(invoice_id, amount, method, summary, booking_id) VALUES (:id,:amount,:method,:summary,:bookingId);""",
            MapSqlParameterSource(mutableMapOf("id" to invoice.id, "amount" to invoice.amount, "method" to invoice.method.name, "summary" to invoice.summary, "bookingId" to bookingId))
    )

    fun update(invoice: Invoice) = Pair(
            """UPDATE public.booking_invoice SET amount=:amount, method=:method, summary=:summary, WHERE invoice_id = :id;""",
            MapSqlParameterSource(mutableMapOf("id" to invoice.id, "amount" to invoice.amount, "method" to invoice.method.name, "summary" to invoice.summary))
    )

    fun deleteById(id: Long) = Pair(
            """DELETE FROM public.booking_invoice WHERE invoice_id = :id;""",
            MapSqlParameterSource("id", id)
    )

    fun selectByBookingId(bookingId: Long) = Pair(
            """SELECT invoice_id, amount, method, summary FROM public.booking_invoice WHERE booking_id = :bookingId;""",
            MapSqlParameterSource("bookingId", bookingId)
    )
}

object BookingQuery {
    fun selectById(id: Long) = Pair(
            """SELECT booking_id, board, cancellation_reason, class_number, rescheduled, rescheduling_reason, comment, deadline, scheduled_time, score, secret, start_time, end_time, status, subject FROM public.booking WHERE booking_id = :id;""",
            MapSqlParameterSource("id", id)
    )

    fun selectReject(id: Long) = Pair(
            """SELECT reject FROM public.booking_reject WHERE booking_id = :id""",
            MapSqlParameterSource("id", id)
    )

    fun selectTopic(id: Long) = Pair(
            """SELECT topic FROM public.booking_topic WHERE booking_id = :id""",
            MapSqlParameterSource("id", id)
    )

    fun selectByStudentId(studentId: Long) = Pair(
            """SELECT booking_id FROM public.booking WHERE student_id = :studentId""",
            MapSqlParameterSource("studentId", studentId)
    )

    fun selectByTutorId(tutorId: Long) = Pair(
            """SELECT booking_id FROM public.booking WHERE tutor_id = :tutorId""",
            MapSqlParameterSource("tutorId", tutorId)
    )

    fun insertIntoBooking(studentId: Long, tutorId: Long, booking: Booking) = Pair(
            """INSERT INTO public.booking(booking_id, board, cancellation_reason, class_number, rescheduled, rescheduling_reason, comment, deadline, scheduled_time, score, secret, start_time, end_time, status, subject, tutor_id, student_id) 
                VALUES (:id,:board,:cancellationReason,:classNumber,:rescheduled,:reschedulingReason,:comment,:deadline,:scheduledTime,:score,:secret,:startTime,:endTime,:status,:subject,:tutorId,:studentId);""".trimIndent(),
            MapSqlParameterSource(mutableMapOf(
                    "id" to booking.id,
                    "board" to booking.board.name,
                    "cancellationReason" to booking.cancellationReason,
                    "classNumber" to booking.classNumber,
                    "rescheduled" to booking.rescheduled,
                    "reschedulingReason" to booking.reschedulingReason,
                    "comment" to booking.comment,
                    "deadline" to booking.deadline,
                    "scheduledTime" to booking.scheduledTime,
                    "score" to booking.score,
                    "secret" to booking.secret,
                    "startTime" to booking.startTime,
                    "endTime" to booking.endTime,
                    "status" to booking.status.name,
                    "subject" to booking.subject,
                    "tutorId" to tutorId,
                    "studentId" to studentId
            ))
    )

    fun insertIntoReject(bookingId: Long) = """INSERT INTO public.booking_reject(booking_id, reject) VALUES (${bookingId}, :reject);"""
    fun deleteFromReject(bookingId: Long) = Pair(
            """DELETE FROM public.booking_reject WHERE booking_id = :bookingId""",
            MapSqlParameterSource("bookingId", bookingId)
    )

    fun insertIntoTopic(bookingId: Long) = """INSERT INTO public.booking_topic(booking_id, topic) VALUES (${bookingId}, :topic);"""
    fun deleteFromTopic(bookingId: Long) = Pair(
            """DELETE FROM public.booking_topic WHERE booking_id = :bookingId""",
            MapSqlParameterSource("bookingId", bookingId)
    )

    fun deleteById(bookingId: Long) = Pair(
            """DELETE FROM public.booking WHERE booking_id = :bookingId""",
            MapSqlParameterSource("bookingId", bookingId)
    )

    fun updateBooking(tutorId: Long, booking: Booking) = Pair(
            """UPDATE public.booking
                SET board=:board, cancellation_reason=:cancellationReason, class_number=:classNumber, rescheduled=:rescheduled, rescheduling_reason=:reschedulingReason, comment=:comment, deadline=:deadline, scheduled_time=:scheduledTime, score=:score, secret=:secret, start_time=:startTime, end_time=:endTime, status=:status, subject=:subject, tutor_id=:tutorId
	            WHERE booking_id = :id;""".trimIndent(),
            MapSqlParameterSource(mutableMapOf(
                    "id" to booking.id,
                    "board" to booking.board.name,
                    "cancellationReason" to booking.cancellationReason,
                    "classNumber" to booking.classNumber,
                    "rescheduled" to booking.rescheduled,
                    "reschedulingReason" to booking.reschedulingReason,
                    "comment" to booking.comment,
                    "deadline" to booking.deadline,
                    "scheduledTime" to booking.scheduledTime,
                    "score" to booking.score,
                    "secret" to booking.secret,
                    "startTime" to booking.startTime,
                    "endTime" to booking.endTime,
                    "status" to booking.status.name,
                    "subject" to booking.subject,
                    "tutorId" to tutorId
            ))
    )
}
