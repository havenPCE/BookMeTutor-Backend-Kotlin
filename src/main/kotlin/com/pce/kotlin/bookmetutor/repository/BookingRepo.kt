package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Booking
import com.pce.kotlin.bookmetutor.model.dao.Student
import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.util.BookingStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface BookingRepo : JpaRepository<Booking, Long> {

    fun findAllByStudent(student: Student): List<Booking>?

    fun findAllByTutor(tutor: Tutor): List<Booking>?

    fun findAllByStatus(status: BookingStatus): List<Booking>?
}