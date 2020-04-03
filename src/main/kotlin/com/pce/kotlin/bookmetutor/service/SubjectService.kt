package com.pce.kotlin.bookmetutor.service

import com.pce.kotlin.bookmetutor.model.dao.Subject
import com.pce.kotlin.bookmetutor.model.dto.subject.CreateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.UpdateSubjectDto

interface SubjectService {
    fun retrieveAllSubjects(): List<Subject>?
    fun retrieveSubject(classNumber: Int, subjectName: String): Subject?
    fun createSubject(dto: CreateSubjectDto): Subject?
    fun removeSubject(id: Long)
    fun updateSubject(id: Long, update: UpdateSubjectDto): Subject?
}