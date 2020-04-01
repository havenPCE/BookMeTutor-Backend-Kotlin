package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface TutorRepo : JpaRepository<Tutor, Long> {

    fun findByEmail(email: String): Tutor?

    fun findFirstByEmailNotInAndAddress_CityOrderByLastPickedAsc(rejectList: List<String>, city: String): Tutor?

}