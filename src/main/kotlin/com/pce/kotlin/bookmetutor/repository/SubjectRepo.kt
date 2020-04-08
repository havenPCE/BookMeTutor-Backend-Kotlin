package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Subject
import com.pce.kotlin.bookmetutor.util.SubjectName

interface SubjectRepo {
    fun findById(id: Long): Subject?
    fun findBySubjectNameAndClassNumber(subjectName: SubjectName, classNumber: Int): Subject?
    fun save(subject: Subject): Subject?
    fun update(subject: Subject): Subject?
    fun deleteById(id: Long): Boolean
    fun findAll(): List<Subject>
}