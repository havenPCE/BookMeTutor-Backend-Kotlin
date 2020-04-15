package com.pce.kotlin.bookmetutor.service

import com.pce.kotlin.bookmetutor.model.dto.subject.CreateSubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.SubjectDto
import com.pce.kotlin.bookmetutor.model.dto.subject.UpdateSubjectDto

interface SubjectService {
    fun retrieveAllSubjects(): List<SubjectDto>
    fun retrieveSubject(classNumber: Int, subjectName: String): SubjectDto?
    fun createSubject(dto: CreateSubjectDto): SubjectDto?
    fun removeSubject(id: Long): Boolean
    fun updateSubject(id: Long, dto: UpdateSubjectDto): SubjectDto?
}