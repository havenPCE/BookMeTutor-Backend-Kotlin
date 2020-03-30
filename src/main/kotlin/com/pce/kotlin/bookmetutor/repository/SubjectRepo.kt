package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Subject
import com.pce.kotlin.bookmetutor.util.SubjectName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface SubjectRepo : JpaRepository<Subject, Long> {
    fun findByClassNumberAndSubjectName(classNumber: Int, subjectName: SubjectName): Subject?

    fun findAllByClassNumber(classNumber: Int): List<Subject>

    fun findAllBySubjectName(subjectName: SubjectName): List<Subject>?
}